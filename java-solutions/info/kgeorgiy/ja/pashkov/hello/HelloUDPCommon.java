package info.kgeorgiy.ja.pashkov.hello;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

final class HelloUDPCommon {
    static final int SO_TIMEOUT_MS = 200;

    private HelloUDPCommon() {
    }

    static Integer parsePositiveInt(final String value) {
        try {
            final int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    static boolean isValidResponse(
            final String response,
            final String request,
            final int requestId,
            final int threadId
    ) {
        if (response == null || request == null) {
            return false;
        }
        final List<Integer> numbers = extractNumbers(response);
        return numbers.size() == 2
                && numbers.get(0) == requestId
                && numbers.get(1) == threadId;
    }

    static String request(final String prefix, final int threadId, final int requestId) {
        return prefix + requestId + "_" + threadId;
    }

    static byte[] requestBytes(final String request) {
        return request.getBytes(StandardCharsets.UTF_8);
    }

    static void awaitTermination(final ExecutorService pool) {
        pool.shutdown();
        try {
            while (!pool.awaitTermination(1_000, TimeUnit.MILLISECONDS)) {
            }
        } catch (final InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static List<Integer> extractNumbers(final String text) {
        final List<Integer> result = new ArrayList<>();
        long current = -1;
        for (int i = 0; i < text.length(); ) {
            final int codePoint = text.codePointAt(i);
            final int digit = Character.digit(codePoint, 10);
            if (digit >= 0) {
                current = current < 0 ? digit : current * 10 + digit;
                if (current > Integer.MAX_VALUE) {
                    current = Integer.MAX_VALUE;
                }
            } else if (current >= 0) {
                result.add((int) current);
                current = -1;
            }
            i += Character.charCount(codePoint);
        }
        if (current >= 0) {
            result.add((int) current);
        }
        return result;
    }
}
