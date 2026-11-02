package info.kgeorgiy.ja.pashkov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class Client {
    private static final int ARGUMENTS_COUNT = 5;

    private Client() {
    }

    public static void main(final String... args) {
        if (args == null || args.length != ARGUMENTS_COUNT) {
            System.err.println("Usage: Client firstName lastName passport accountSubId amountDelta");
            return;
        }
        for (final String arg : args) {
            if (arg == null) {
                System.err.println("All arguments must be non-null");
                return;
            }
        }

        final long amountDelta;
        try {
            amountDelta = BankUtils.parseLong(args[4], "amountDelta");
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }

        final Bank bank;
        try {
            bank = (Bank) Naming.lookup(BankServer.NAMING_URL);
        } catch (final NotBoundException e) {
            System.err.println("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            System.err.println("Bank URL is invalid");
            return;
        } catch (final RemoteException e) {
            System.err.println("Remote error: " + e.getMessage());
            return;
        }

        try {
            final Long balance = BankClient.updateAmount(
                    bank,
                    args[0],
                    args[1],
                    args[2],
                    args[3],
                    amountDelta
            );
            if (balance != null) {
                System.out.println(balance);
            }
        } catch (final RemoteException e) {
            System.err.println("Remote error: " + e.getMessage());
        }
    }
}
