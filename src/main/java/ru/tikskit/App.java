package ru.tikskit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class App {
    private final static int ACCOUNT_COUNT = 40;
    private final static int TRANSACTION_COUNT = 30000;
    private final static int THREAD_COUNT = 4000;


    public static void main(String[] args) {
        Set<Account> accounts = new HashSet<>();

        for (int i = 1; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account(String.format("ACC-%s", i)));
        }

        AccountsContainer accountsContainer = new AccountsContainer(accounts);

        TransactionsCounter transactionCounter = new TransactionsCounter(TRANSACTION_COUNT);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new Thread(new TransferJob(accountsContainer, transactionCounter), String.format("Transfer thread %s", i)));
        }

        threads.forEach(Thread::start);
    }
}
