package info.kgeorgiy.ja.pashkov.bank;

import java.rmi.RemoteException;
import java.util.Map;

public class RemotePerson implements Person {
    private final String firstName;
    private final String lastName;
    private final String passport;
    private final RemoteBank bank;

    public RemotePerson(
            final String firstName,
            final String lastName,
            final String passport,
            final RemoteBank bank
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.bank = bank;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassport() {
        return passport;
    }

    @Override
    public Account getAccount(final String subId) throws RemoteException {
        return bank.getAccount(passport, subId);
    }

    @Override
    public Account createAccount(final String subId) throws RemoteException {
        return bank.createAccount(passport, subId);
    }

    @Override
    public Map<String, Account> getAccounts() throws RemoteException {
        return bank.getAccountsByPassport(passport);
    }
}
