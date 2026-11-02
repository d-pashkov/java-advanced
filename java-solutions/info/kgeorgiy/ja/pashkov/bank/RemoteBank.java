package info.kgeorgiy.ja.pashkov.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteBank implements Bank {
    private final int exportPort;
    private final ConcurrentHashMap<String, RemotePerson> persons = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, RemoteAccount>> accountsByPerson = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RemoteAccount> accountsById = new ConcurrentHashMap<>();

    public RemoteBank(final int exportPort) throws RemoteException {
        this.exportPort = exportPort;
        UnicastRemoteObject.exportObject(this, exportPort);
    }

    @Override
    public Person createPerson(final String firstName, final String lastName, final String passport) throws RemoteException {
        BankUtils.require(firstName, "firstName");
        BankUtils.require(lastName, "lastName");
        BankUtils.require(passport, "passport");

        final RemotePerson existing = persons.get(passport);
        if (existing != null) {
            if (!existing.getFirstName().equals(firstName) || !existing.getLastName().equals(lastName)) {
                throw new IllegalArgumentException("person data mismatch for passport " + passport);
            }
            return existing;
        }

        final RemotePerson created = new RemotePerson(firstName, lastName, passport, this);
        final RemotePerson previous = persons.putIfAbsent(passport, created);
        final RemotePerson result = previous == null ? created : previous;
        if (!result.getFirstName().equals(firstName) || !result.getLastName().equals(lastName)) {
            throw new IllegalArgumentException("person data mismatch for passport " + passport);
        }
        if (previous == null) {
            UnicastRemoteObject.exportObject(created, exportPort);
            accountsByPerson.putIfAbsent(passport, new ConcurrentHashMap<>());
        }
        return result;
    }

    @Override
    public Person getRemotePerson(final String passport) {
        BankUtils.require(passport, "passport");
        return persons.get(passport);
    }

    @Override
    public Person getLocalPerson(final String passport) throws RemoteException {
        BankUtils.require(passport, "passport");
        final RemotePerson person = persons.get(passport);
        if (person == null) {
            return null;
        }
        final Map<String, LocalAccount> localAccounts = new HashMap<>();
        final ConcurrentHashMap<String, RemoteAccount> remoteAccounts = accountsByPerson.get(passport);
        if (remoteAccounts != null) {
            for (final Map.Entry<String, RemoteAccount> entry : remoteAccounts.entrySet()) {
                final RemoteAccount account = entry.getValue();
                localAccounts.put(entry.getKey(), new LocalAccount(account.getId(), account.getAmount()));
            }
        }
        return new LocalPerson(person.getFirstName(), person.getLastName(), person.getPassport(), localAccounts);
    }

    @Override
    public Account createAccount(final String passport, final String subId) throws RemoteException {
        BankUtils.require(passport, "passport");
        BankUtils.require(subId, "subId");
        if (!persons.containsKey(passport)) {
            return null;
        }

        final ConcurrentHashMap<String, RemoteAccount> personAccounts =
                accountsByPerson.computeIfAbsent(passport, key -> new ConcurrentHashMap<>());

        final String globalId = BankUtils.composeAccountId(passport, subId);
        final RemoteAccount existing = personAccounts.get(subId);
        if (existing != null) {
            return existing;
        }

        final RemoteAccount created = new RemoteAccount(globalId);
        final RemoteAccount previous = personAccounts.putIfAbsent(subId, created);
        if (previous == null) {
            accountsById.put(globalId, created);
            UnicastRemoteObject.exportObject(created, exportPort);
            return created;
        }
        return previous;
    }

    @Override
    public Account getAccount(final String passport, final String subId) {
        BankUtils.require(passport, "passport");
        BankUtils.require(subId, "subId");
        final Map<String, RemoteAccount> personAccounts = accountsByPerson.get(passport);
        if (personAccounts == null) {
            return null;
        }
        return personAccounts.get(subId);
    }

    @Override
    public Account getAccount(final String accountId) {
        BankUtils.require(accountId, "accountId");
        return accountsById.get(accountId);
    }

    public Map<String, Account> getAccountsByPassport(final String passport) {
        final Map<String, RemoteAccount> personAccounts = accountsByPerson.get(passport);
        if (personAccounts == null) {
            return Map.of();
        }
        return new HashMap<>(personAccounts);
    }

    public void shutdown() {
        for (final RemoteAccount account : accountsById.values()) {
            try {
                UnicastRemoteObject.unexportObject(account, true);
            } catch (final Exception ignored) {
            }
        }
        for (final RemotePerson person : persons.values()) {
            try {
                UnicastRemoteObject.unexportObject(person, true);
            } catch (final Exception ignored) {
            }
        }
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (final Exception ignored) {
        }
    }
}
