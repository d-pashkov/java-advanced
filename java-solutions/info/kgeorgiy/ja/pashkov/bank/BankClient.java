package info.kgeorgiy.ja.pashkov.bank;

import java.rmi.RemoteException;

public final class BankClient {
    private BankClient() {
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 5) {
            System.err.println("Usage: BankClient firstName lastName passport accountSubId amountDelta");
            return;
        }
        for (final String arg : args) {
            if (arg == null) {
                System.err.println("All arguments must be non-null");
                return;
            }
        }
        Client.main(args);
    }

    public static Long updateAmount(
            final Bank bank,
            final String firstName,
            final String lastName,
            final String passport,
            final String subId,
            final long amountDelta
    ) throws RemoteException {
        Person person = bank.getRemotePerson(passport);
        if (person == null) {
            person = bank.createPerson(firstName, lastName, passport);
        } else if (!person.getFirstName().equals(firstName) || !person.getLastName().equals(lastName)) {
            System.err.println("Person data mismatch for passport " + passport);
            return null;
        }

        Account account = person.getAccount(subId);
        if (account == null) {
            account = person.createAccount(subId);
        }

        final long current = account.getAmount();
        final long next = current + amountDelta;
        if (next < 0) {
            System.err.println("Result amount cannot be negative");
            return null;
        }
        account.setAmount(next);
        return account.getAmount();
    }
}
