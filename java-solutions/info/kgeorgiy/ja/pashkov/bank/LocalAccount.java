package info.kgeorgiy.ja.pashkov.bank;

public class LocalAccount implements Account {
    private final String id;
    private long amount;

    public LocalAccount(final String id, final long amount) {
        this.id = id;
        setAmount(amount);
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
