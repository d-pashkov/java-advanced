package info.kgeorgiy.ja.pashkov.iterative;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private static final String CLOSED_MESSAGE = "Parallel mapper is closed";

    private final ArrayDeque<Task> queue = new ArrayDeque<>();
    private final List<Thread> workers;
    private volatile boolean closed;

    public ParallelMapperImpl(final int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("threads must be positive");
        }
        workers = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            final Thread worker = new Thread(this::workerLoop, "ParallelMapper-worker-" + i);
            workers.add(worker);
            worker.start();
        }
    }

    @Override
    public <T, R> List<R> map(
            final Function<? super T, ? extends R> f,
            final List<? extends T> args
    ) throws InterruptedException {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(args, "args");
        final List<? extends T> items = new ArrayList<>(args);
        final TaskGroup<R> group = new TaskGroup<>(items.size());

        synchronized (queue) {
            ensureOpen();
            for (int i = 0; i < items.size(); i++) {
                final int index = i;
                queue.addLast(new Task(
                        () -> {
                            try {
                                group.setResult(index, f.apply(items.get(index)));
                            } catch (final RuntimeException | Error e) {
                                group.addException(e);
                            } finally {
                                group.countDown();
                            }
                        },
                        () -> {
                            group.addException(new IllegalStateException(CLOSED_MESSAGE));
                            group.countDown();
                        }
                ));
            }
            queue.notifyAll();
        }

        group.await();
        if (closed) {
            throw new IllegalStateException(CLOSED_MESSAGE);
        }
        group.throwIfNeeded();
        return group.results();
    }

    @Override
    public void close() {
        synchronized (queue) {
            if (closed) {
                return;
            }
            closed = true;
            while (!queue.isEmpty()) {
                queue.pollFirst().execute(true);
            }
            queue.notifyAll();
        }

        for (final Thread worker : workers) {
            worker.interrupt();
        }

        boolean interrupted = false;
        for (final Thread worker : workers) {
            while (true) {
                try {
                    worker.join();
                    break;
                } catch (final InterruptedException e) {
                    interrupted = true;
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private void workerLoop() {
        while (true) {
            final Task task;
            synchronized (queue) {
                while (queue.isEmpty() && !closed) {
                    try {
                        queue.wait();
                    } catch (final InterruptedException e) {
                        if (closed) {
                            return;
                        }
                    }
                }
                if (closed && queue.isEmpty()) {
                    return;
                }
                task = queue.pollFirst();
            }
            if (task != null) {
                task.execute(false);
            }
        }
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException(CLOSED_MESSAGE);
        }
    }

    private static final class Task {
        private final Runnable action;
        private final Runnable onCancel;
        private boolean executed;

        private Task(final Runnable action, final Runnable onCancel) {
            this.action = action;
            this.onCancel = onCancel;
        }

        private void execute(final boolean cancelled) {
            synchronized (this) {
                if (executed) {
                    return;
                }
                executed = true;
            }
            if (cancelled) {
                onCancel.run();
            } else {
                action.run();
            }
        }
    }

    private static final class TaskGroup<R> {
        private final List<R> results;
        private int remaining;
        private Throwable throwable;

        private TaskGroup(final int size) {
            results = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                results.add(null);
            }
            remaining = size;
        }

        private synchronized void setResult(final int index, final R value) {
            results.set(index, value);
        }

        private synchronized void addException(final Throwable t) {
            if (throwable == null) {
                throwable = t;
            } else {
                throwable.addSuppressed(t);
            }
        }

        private synchronized void countDown() {
            remaining--;
            if (remaining == 0) {
                notifyAll();
            }
        }

        private synchronized void await() throws InterruptedException {
            while (remaining > 0) {
                wait();
            }
        }

        private synchronized List<R> results() {
            return results;
        }

        private synchronized void throwIfNeeded() {
            if (throwable == null) {
                return;
            }
            if (throwable instanceof final RuntimeException e) {
                throw e;
            }
            if (throwable instanceof final Error e) {
                throw e;
            }
            throw new RuntimeException(throwable);
        }
    }
}
