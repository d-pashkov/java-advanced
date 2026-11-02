package info.kgeorgiy.ja.pashkov.iterative;

import info.kgeorgiy.java.advanced.iterative.AdvancedIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterativeParallelism implements AdvancedIP {
    private static final String INTERRUPTED_MESSAGE = "Computation was interrupted";

    private final ParallelMapper mapper;

    public IterativeParallelism() {
        this(null);
    }

    public IterativeParallelism(final ParallelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <T> int argMax(
            final int threads,
            final List<T> values,
            final Comparator<? super T> comparator
    ) throws InterruptedException {
        return argMax(threads, values, comparator, 1);
    }

    @Override
    public <T> int argMin(
            final int threads,
            final List<T> values,
            final Comparator<? super T> comparator
    ) throws InterruptedException {
        return argMin(threads, values, comparator, 1);
    }

    @Override
    public <T> int indexOf(
            final int threads,
            final List<T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return indexOf(threads, values, predicate, 1);
    }

    @Override
    public <T> int lastIndexOf(
            final int threads,
            final List<T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return lastIndexOf(threads, values, predicate, 1);
    }

    @Override
    public <T> long sumIndices(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return sumIndices(threads, values, predicate, 1);
    }

    @Override
    public <T> int[] indices(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return indices(threads, values, predicate, 1);
    }

    @Override
    public <T> List<T> filter(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return filter(threads, values, predicate, 1);
    }

    @Override
    public <T, U> List<U> map(
            final int threads,
            final List<? extends T> values,
            final Function<? super T, ? extends U> f
    ) throws InterruptedException {
        return map(threads, values, f, 1);
    }

    public <T> int argMax(
            final int threads,
            final List<T> values,
            final Comparator<? super T> comparator,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(comparator, "comparator");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            throw new NoSuchElementException("No values");
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<IndexedValue<T>> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            int bestIndex = range.from();
            T bestValue = values.get(bestIndex * step);
            for (int i = range.from() + 1; i < range.to(); i++) {
                ensureNotInterrupted();
                final T current = values.get(i * step);
                if (comparator.compare(current, bestValue) > 0) {
                    bestIndex = i;
                    bestValue = current;
                }
            }
            return new IndexedValue<>(bestIndex, bestValue);
        });

        IndexedValue<T> best = partial.getFirst();
        for (int i = 1; i < partial.size(); i++) {
            final IndexedValue<T> candidate = partial.get(i);
            final int cmp = comparator.compare(candidate.value(), best.value());
            if (cmp > 0 || (cmp == 0 && candidate.index() < best.index())) {
                best = candidate;
            }
        }
        return best.index() * step;
    }

    public <T> int argMin(
            final int threads,
            final List<T> values,
            final Comparator<? super T> comparator,
            final int step
    ) throws InterruptedException {
        return argMax(threads, values, comparator.reversed(), step);
    }

    public <T> int indexOf(
            final int threads,
            final List<T> values,
            final Predicate<? super T> predicate,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(predicate, "predicate");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return -1;
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<Integer> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                if (predicate.test(values.get(i * step))) {
                    return i;
                }
            }
            return -1;
        });

        int best = -1;
        for (final int candidate : partial) {
            if (candidate != -1 && (best == -1 || candidate < best)) {
                best = candidate;
            }
        }
        return best == -1 ? -1 : best * step;
    }

    public <T> int lastIndexOf(
            final int threads,
            final List<T> values,
            final Predicate<? super T> predicate,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(predicate, "predicate");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return -1;
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<Integer> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            for (int i = range.to() - 1; i >= range.from(); i--) {
                ensureNotInterrupted();
                if (predicate.test(values.get(i * step))) {
                    return i;
                }
            }
            return -1;
        });

        int best = -1;
        for (final int candidate : partial) {
            if (candidate > best) {
                best = candidate;
            }
        }
        return best == -1 ? -1 : best * step;
    }

    public <T> long sumIndices(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(predicate, "predicate");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return 0;
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<Long> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            long sum = 0;
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                if (predicate.test(values.get(i * step))) {
                    sum += (long) i * step;
                }
            }
            return sum;
        });

        long sum = 0;
        for (final long value : partial) {
            sum += value;
        }
        return sum;
    }

    public <T> int[] indices(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(predicate, "predicate");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return new int[0];
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<int[]> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            final int[] temp = new int[range.to() - range.from()];
            int size = 0;
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                if (predicate.test(values.get(i * step))) {
                    temp[size++] = i * step;
                }
            }
            return Arrays.copyOf(temp, size);
        });

        int total = 0;
        for (final int[] part : partial) {
            total += part.length;
        }
        final int[] result = new int[total];
        int pos = 0;
        for (final int[] part : partial) {
            System.arraycopy(part, 0, result, pos, part.length);
            pos += part.length;
        }
        return result;
    }

    public <T> List<T> filter(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values);
        Objects.requireNonNull(predicate);
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return List.of();
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<List<T>> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            final List<T> current = new ArrayList<>(range.to() - range.from());
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                final T value = values.get(i * step);
                if (predicate.test(value)) {
                    current.add(value);
                }
            }
            return current;
        });

        final List<T> result = new ArrayList<>();
        for (final List<T> part : partial) {
            result.addAll(part);
        }
        return result;
    }

    public <T, R> List<R> map(
            final int threads,
            final List<? extends T> values,
            final Function<? super T, ? extends R> f,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(f, "f");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return List.of();
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<List<R>> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            final List<R> current = new ArrayList<>(range.to() - range.from());
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                current.add(f.apply(values.get(i * step)));
            }
            return current;
        });

        final List<R> result = new ArrayList<>(sampledSize);
        for (final List<R> part : partial) {
            result.addAll(part);
        }
        return result;
    }

    public <T> T reduce(
            final int threads,
            final List<T> values,
            final T identity,
            final BinaryOperator<T> operator,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(operator, "operator");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return identity;
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<T> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            T current = identity;
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                current = operator.apply(current, values.get(i * step));
            }
            return current;
        });

        T result = identity;
        for (final T value : partial) {
            result = operator.apply(result, value);
        }
        return result;
    }

    public <T, R> R mapReduce(
            final int threads,
            final List<T> values,
            final Function<T, R> lift,
            final R identity,
            final BinaryOperator<R> operator,
            final int step
    ) throws InterruptedException {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(lift, "lift");
        Objects.requireNonNull(operator, "operator");
        validateParameters(threads, step);

        final int sampledSize = sampledSize(values.size(), step);
        if (sampledSize == 0) {
            return identity;
        }

        final List<Range> ranges = split(sampledSize, threads);
        final List<R> partial = execute(ranges, range -> {
            ensureNotInterrupted();
            R current = identity;
            for (int i = range.from(); i < range.to(); i++) {
                ensureNotInterrupted();
                current = operator.apply(current, lift.apply(values.get(i * step)));
            }
            return current;
        });

        R result = identity;
        for (final R value : partial) {
            result = operator.apply(result, value);
        }
        return result;
    }

    private static void validateParameters(final int threads, final int step) {
        if (threads <= 0) {
            throw new IllegalArgumentException("threads must be positive");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }
    }

    private static int sampledSize(final int size, final int step) {
        return (size + step - 1) / step;
    }

    private static List<Range> split(final int size, final int threads) {
        final int parts = Math.min(size, threads);
        final List<Range> ranges = new ArrayList<>(parts);
        int left = 0;
        for (int i = 0; i < parts; i++) {
            final int right = left + (size - left + (parts - i) - 1) / (parts - i);
            ranges.add(new Range(left, right));
            left = right;
        }
        return ranges;
    }

    private <R> List<R> execute(
            final List<Range> ranges,
            final Function<Range, R> task
    ) throws InterruptedException {
        if (ranges.isEmpty()) {
            return List.of();
        }

        if (mapper != null) {
            return mapper.map(task, ranges);
        }

        final Object[] results = new Object[ranges.size()];
        final ThrowableHolder throwableHolder = new ThrowableHolder();
        final List<Thread> workers = new ArrayList<>(ranges.size());

        for (int i = 0; i < ranges.size(); i++) {
            final int index = i;
            final Thread thread = new Thread(() -> {
                try {
                    results[index] = task.apply(ranges.get(index));
                } catch (final RuntimeException | Error e) {
                    throwableHolder.add(e);
                }
            });
            workers.add(thread);
            thread.start();
        }

        InterruptedException interrupted = null;
        for (final Thread worker : workers) {
            try {
                worker.join();
            } catch (final InterruptedException e) {
                if (interrupted == null) {
                    interrupted = e;
                    for (final Thread another : workers) {
                        another.interrupt();
                    }
                } else {
                    interrupted.addSuppressed(e);
                }
            }
        }

        if (interrupted != null) {
            throw interrupted;
        }

        throwableHolder.throwIfNeeded();
        final List<R> casted = new ArrayList<>(results.length);
        for (final Object result : results) {
            @SuppressWarnings("unchecked") final R value = (R) result;
            casted.add(value);
        }
        return casted;
    }

    private static void ensureNotInterrupted() {
        if (Thread.currentThread().isInterrupted()) {
            throw new IllegalStateException(INTERRUPTED_MESSAGE);
        }
    }

    private record Range(int from, int to) {
    }

    private record IndexedValue<T>(int index, T value) {
    }

    private static final class ThrowableHolder {
        private Throwable throwable;

        private synchronized void add(final Throwable t) {
            if (throwable == null) {
                throwable = t;
            } else {
                throwable.addSuppressed(t);
            }
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
