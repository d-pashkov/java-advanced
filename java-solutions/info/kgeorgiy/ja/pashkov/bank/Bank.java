package info.kgeorgiy.ja.pashkov.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    Person createPerson(String firstName, String lastName, String passport) throws RemoteException;

    Person getRemotePerson(String passport) throws RemoteException;

    Person getLocalPerson(String passport) throws RemoteException;

    Account createAccount(String passport, String subId) throws RemoteException;

    default Account createAccount(final String id) throws RemoteException {
        final String[] parts = BankUtils.splitAccountId(id);
        return createAccount(parts[0], parts[1]);
    }

    Account getAccount(String passport, String subId) throws RemoteException;

    Account getAccount(String accountId) throws RemoteException;
}
