package info.kgeorgiy.ja.pashkov.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//копипаста с RemotePerson
public class LocalPerson implements Person {
    private final String firstName;
    private final String lastName;
    private final String passport;
    private final ConcurrentHashMap<String, LocalAccount> accounts;

    public LocalPerson(
            final String firstName,
            final String lastName,
            final String passport,
            final Map<String, LocalAccount> accounts
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.accounts = new ConcurrentHashMap<>(accounts);
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
    public Account getAccount(final String subId) {
        return accounts.get(subId);
    }

    @Override
    public Account createAccount(final String subId) {
        BankUtils.require(subId, "subId");
        return accounts.computeIfAbsent(subId, id -> new LocalAccount(BankUtils.composeAccountId(passport, id), 0));
    }

    @Override
    public Map<String, Account> getAccounts() {
        return new HashMap<>(accounts);
    }
}
