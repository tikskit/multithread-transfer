package ru.tikskit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class TransferJob implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TransferJob.class);

    private static final int MIN_SLEEP = 1000;
    private static final int MAX_SLEEP = 2001;

    private final AccountsContainer accountsContainer;
    private final TransactionsCounter transactionCounter;

    public TransferJob(AccountsContainer accountsContainer, TransactionsCounter transactionCounter) {
        this.accountsContainer = accountsContainer;
        this.transactionCounter = transactionCounter;
    }

    @Override
    public void run() {
        int transactionNo;
        TransferAccounts transferAccounts;
        while (!Thread.currentThread().isInterrupted()) {
            transactionNo = transactionCounter.request();
            if (transactionNo == TransactionsCounter.VOID) {
                return;
            }
            try {
                transferAccounts = accountsContainer.requestAccounts();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            doTransfer(transferAccounts.getFrom(), transferAccounts.getTo(), transactionNo);

            accountsContainer.returnAccounts(transferAccounts);

            if (!randomSleep()) {
                return;
            }
        }
    }

    private void doTransfer(Account from, Account to, int transactionNo) {
        int delta = getMoneyDelta(from);
        from.dec(delta);
        to.inc(delta);
        logger.info("Транзакция №{} переведено {} со счета {} на счет {}", transactionNo + 1, delta,
                from, to);
    }

    private int getMoneyDelta(Account account) {
        if (account.getMoney() <= 0) {
            logger.error("Некорректное значение суммы денег на счете: {}", account.getMoney());
            throw new IllegalArgumentException("Not enough money");
        }
        return ThreadLocalRandom.current().nextInt(1, account.getMoney() + 1);
    }

    private boolean randomSleep() {
        int sleep = ThreadLocalRandom.current().nextInt(MIN_SLEEP, MAX_SLEEP);
        try {
            Thread.sleep(sleep);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

}
