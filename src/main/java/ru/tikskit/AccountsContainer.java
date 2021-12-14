package ru.tikskit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccountsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AccountsContainer.class);

    private final List<Account> accounts;

    public AccountsContainer(Set<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public synchronized TransferAccounts requestAccounts() throws InterruptedException {
        while (accounts.size() < 2 || getFirstWithNotEmptyMoney() < 0) {
            wait();
        }
        int notEmptyMoneyIndex = getFirstWithNotEmptyMoney();
        Account from = accounts.remove(notEmptyMoneyIndex);
        Account to = accounts.remove(notEmptyMoneyIndex == accounts.size() ? 0 : notEmptyMoneyIndex);

        return new TransferAccounts(from, to);
    }

    public synchronized void returnAccounts(TransferAccounts transferAccounts) {
        accounts.add(transferAccounts.getFrom());
        accounts.add(transferAccounts.getTo());

        notifyAll();
    }

    public synchronized long getTotalMoney() {
        return accounts.stream().map(Account::getMoney).reduce(Integer::sum).orElse(0);
    }

    private int getFirstWithNotEmptyMoney() {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getMoney() > 0) {
                return i;
            }
        }
        return -1;
    }
}
