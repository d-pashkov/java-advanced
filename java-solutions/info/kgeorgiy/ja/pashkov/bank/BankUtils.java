package info.kgeorgiy.ja.pashkov.bank;

final class BankUtils {
    private BankUtils() {
    }

    static String composeAccountId(final String passport, final String subId) {
        return passport + ":" + subId;
    }

    static void require(final String value, final String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must be non-empty");
        }
    }

    static long parseLong(final String value, final String name) {
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be long", e);
        }
    }

    static int parsePort(final String value, final String name) {
        final long parsed = parseLong(value, name);
        if (parsed < 1 || parsed > 65535) {
            throw new IllegalArgumentException(name + " must be in range [1, 65535]");
        }
        return (int) parsed;
    }

    static String[] splitAccountId(final String accountId) {
        require(accountId, "accountId");
        final int separator = accountId.indexOf(':');
        if (separator <= 0 || separator == accountId.length() - 1 || accountId.indexOf(':', separator + 1) >= 0) {
            throw new IllegalArgumentException("accountId must have format <passport>:<subId>");
        }
        return new String[] {
                accountId.substring(0, separator),
                accountId.substring(separator + 1)
        };
    }
}
