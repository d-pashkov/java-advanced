package info.kgeorgiy.ja.pashkov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public final class Server {
    private static final int DEFAULT_EXPORT_PORT = 8888;

    private Server() {
    }

    public static void main(final String... args) {
        final int exportPort;
        try {
            exportPort = args != null && args.length > 0
                    ? BankUtils.parsePort(args[0], "exportPort")
                    : DEFAULT_EXPORT_PORT;
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }

        try {
            try {
                LocateRegistry.createRegistry(BankServer.DEFAULT_REGISTRY_PORT);
            } catch (final RemoteException ignored) {
            }

            final RemoteBank bank = new RemoteBank(exportPort);
            Naming.rebind(BankServer.NAMING_URL, bank);
            System.out.println("Server started");
        } catch (final RemoteException e) {
            System.err.println("Cannot export object: " + e.getMessage());
        } catch (final MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getMessage());
        }
    }
}
