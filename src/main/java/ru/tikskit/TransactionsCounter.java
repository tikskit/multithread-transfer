package ru.tikskit;

public class TransactionsCounter {
    public static final int VOID = -100500;

    private int count = 0;
    private final int maxTransactionsCount;

    public TransactionsCounter(int maxTransactionsCount) {
        this.maxTransactionsCount = maxTransactionsCount;
    }

    public synchronized int request() {
        if (count < maxTransactionsCount) {
            return count++;
        } else {
            return VOID;
        }
    }
}
