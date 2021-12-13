package ru.tikskit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class App {
    private final static int ACCOUNT_COUNT = 4;
    private final static int TRANSACTION_COUNT = 30;
    private final static int THREAD_COUNT = 4;

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {
        Set<Account> accounts = new HashSet<>();

        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account(String.format("ACC-%s", i + 1)));
        }

        AccountsContainer accountsContainer = new AccountsContainer(accounts);
        TransactionsCounter transactionCounter = new TransactionsCounter(TRANSACTION_COUNT);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new Thread(new TransferJob(accountsContainer, transactionCounter), String.format("Transfer thread %s", i)));
        }

        logger.info("Общая сумма до {}", accountsContainer.getTotalMoney());
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("Общая сумма после {}", accountsContainer.getTotalMoney());
    }
}
