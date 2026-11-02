package info.kgeorgiy.ja.pashkov.bank;

public final class BankServer {
    public static final int DEFAULT_REGISTRY_PORT = 1099;
    public static final int DEFAULT_EXPORT_PORT = 8888;
    public static final String DEFAULT_BINDING = "bank";
    public static final String NAMING_URL = "//localhost/" + DEFAULT_BINDING;

    private BankServer() {
    }

    public static void main(final String[] args) {
        Server.main(args);
    }
}
