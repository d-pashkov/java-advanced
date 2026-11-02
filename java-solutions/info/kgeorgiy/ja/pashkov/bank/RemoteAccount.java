package info.kgeorgiy.ja.pashkov.bank;

public class RemoteAccount implements Account {
    private final String id;
    private long amount;

    public RemoteAccount(final String id) {
        this.id = id;
    }

    @Override
    public synchronized String getId() {
        return id;
    }

    @Override
    public synchronized long getAmount() {
        return amount;
    }

    @Override
    public synchronized void setAmount(final long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        this.amount = amount;
    }
}
