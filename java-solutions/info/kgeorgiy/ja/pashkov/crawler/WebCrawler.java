package info.kgeorgiy.ja.pashkov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class WebCrawler implements AdvancedCrawler {
    private static final int DEFAULT_DEPTH = 1;
    private static final int DEFAULT_DOWNLOADERS = 8;
    private static final int DEFAULT_EXTRACTORS = 8;
    private static final int DEFAULT_PER_HOST = 8;

    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final ConcurrentMap<String, HostQueue> hosts = new ConcurrentHashMap<>();
    private final int perHost;
    private volatile boolean closed;

    public WebCrawler(
            final Downloader downloader,
            final int downloaders,
            final int extractors,
            final int perHost
    ) {
        this.downloader = Objects.requireNonNull(downloader);
        if (downloaders <= 0 || extractors <= 0 || perHost <= 0) {
            throw new IllegalArgumentException("All limits must be positive");
        }
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
    }

    @Override
    public Result download(final String url, final int depth) {
        return download(url, depth, List.of(""));
    }

    @Override
    public Result download(final String url, final int depth, final List<String> includes) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(includes);
        final List<String> allowedSubstrings = List.copyOf(includes);
        return crawl(url, depth, page -> allowedSubstrings.stream().anyMatch(page::contains));
    }

    @Override
    public Result advancedDownload(final String url, final int depth, final List<String> hosts) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(hosts);

        final Predicate<String> hostAllowed;
        if (hosts.size() > 1_000_000) {
            hostAllowed = hosts::contains;
        } else {
            final Set<String> hostSet = Set.copyOf(hosts);
            hostAllowed = hostSet::contains;
        }

        return crawl(url, depth, page -> {
            try {
                return hostAllowed.test(URLUtils.getHost(page));
            } catch (final MalformedURLException e) {
                return false;
            }
        });
    }

    private Result crawl(final String url, final int depth, final Predicate<String> allowed) {
        if (depth <= 0 || closed || !allowed.test(url)) {
            return new Result(List.of(), Map.of());
        }

        final Set<String> visited = ConcurrentHashMap.newKeySet();
        final Set<String> downloaded = ConcurrentHashMap.newKeySet();
        final ConcurrentMap<String, IOException> errors = new ConcurrentHashMap<>();

        Set<String> currentLevel = ConcurrentHashMap.newKeySet();
        currentLevel.add(url);

        for (int level = 1; level <= depth && !currentLevel.isEmpty(); level++) {
            final Set<String> nextLevel = ConcurrentHashMap.newKeySet();
            final Phaser phaser = new Phaser(1);

            for (final String page : currentLevel) {
                if (!visited.add(page)) {
                    continue;
                }
                phaser.register();
                submitDownload(
                        page,
                        level,
                        depth,
                        allowed,
                        visited,
                        nextLevel,
                        downloaded,
                        errors,
                        phaser
                );
            }

            phaser.arriveAndAwaitAdvance();
            currentLevel = nextLevel;
        }

        return new Result(new ArrayList<>(downloaded), errors);
    }

    private void submitDownload(
            final String url,
            final int currentDepth,
            final int maxDepth,
            final Predicate<String> allowed,
            final Set<String> visited,
            final Set<String> nextLevel,
            final Set<String> downloaded,
            final ConcurrentMap<String, IOException> errors,
            final Phaser phaser
    ) {
        final String host;
        try {
            host = URLUtils.getHost(url);
        } catch (final MalformedURLException e) {
            errors.put(url, e);
            phaser.arriveAndDeregister();
            return;
        }

        final HostQueue queue = hosts.computeIfAbsent(host, h -> new HostQueue());
        queue.submit(() -> {
            try {
                final Document document = downloader.download(url);
                downloaded.add(url);
                if (currentDepth < maxDepth) {
                    submitExtract(url, document, allowed, visited, nextLevel, errors, phaser);
                }
            } catch (final IOException e) {
                errors.put(url, e);
            } finally {
                phaser.arriveAndDeregister();
            }
        });
    }

    private void submitExtract(
            final String url,
            final Document document,
            final Predicate<String> allowed,
            final Set<String> visited,
            final Set<String> nextLevel,
            final ConcurrentMap<String, IOException> errors,
            final Phaser phaser
    ) {
        phaser.register();
        final Runnable task = () -> {
            try {
                final List<String> links = document.extractLinks();
                for (final String link : links) {
                    if (allowed.test(link) && !visited.contains(link)) {
                        nextLevel.add(link);
                    }
                }
            } catch (final IOException e) {
                errors.put(url, e);
            } finally {
                phaser.arriveAndDeregister();
            }
        };

        try {
            extractors.submit(task);
        } catch (final RejectedExecutionException e) {
            task.run();
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        downloaders.shutdownNow();
        extractors.shutdownNow();
    }

    private final class HostQueue {
        private final ArrayDeque<Runnable> waiting = new ArrayDeque<>();
        private int running;

        private void submit(final Runnable task) {
            final Runnable toRun;
            synchronized (this) {
                if (running < perHost) {
                    running++;
                    toRun = task;
                } else {
                    waiting.addLast(task);
                    return;
                }
            }
            execute(toRun);
        }

        private void execute(final Runnable task) {
            try {
                downloaders.submit(() -> {
                    try {
                        task.run();
                    } finally {
                        scheduleNext();
                    }
                });
            } catch (final RejectedExecutionException e) {
                try {
                    task.run();
                } finally {
                    scheduleNext();
                }
            }
        }

        private void scheduleNext() {
            final Runnable next;
            synchronized (this) {
                next = waiting.pollFirst();
                if (next == null) {
                    running--;
                    return;
                }
            }
            execute(next);
        }
    }

    public static void main(final String[] args) {
        if (args == null || args.length == 0 || args[0] == null) {
            System.err.println("Usage: WebCrawler url [depth [downloaders [extractors [perHost]]]]");
            return;
        }

        final String url = args[0];
        final int depth = parseInt(args, 1, DEFAULT_DEPTH);
        final int downloaders = parseInt(args, 2, DEFAULT_DOWNLOADERS);
        final int extractors = parseInt(args, 3, DEFAULT_EXTRACTORS);
        final int perHost = parseInt(args, 4, DEFAULT_PER_HOST);

        try (WebCrawler crawler = new WebCrawler(
                new CachingDownloader(1.0),
                downloaders,
                extractors,
                perHost
        )) {
            final Result result = crawler.download(url, depth);
            System.out.println("Downloaded:");
            result.downloaded().forEach(System.out::println);
            System.out.println("Errors:");
            result.errors().forEach((key, value) -> System.out.println(key + " -> " + value.getMessage()));
        } catch (final IOException e) {
            System.err.println("Failed to initialize crawler: " + e.getMessage());
        }
    }

    private static int parseInt(final String[] args, final int index, final int defaultValue) {
        if (index >= args.length || args[index] == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(args[index]);
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }
}
