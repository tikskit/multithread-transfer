package ru.tikskit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountsContainer {

    private final List<Account> accounts;

    public AccountsContainer(Collection<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public synchronized TransferAccounts requestAccounts() throws InterruptedException {
        while (accounts.size() < 2) {
            wait();
        }
        Account source = accounts.remove(0);
        Account target = accounts.remove(0);

        return new TransferAccounts(source, target);
    }

    public synchronized void returnAccounts(TransferAccounts transferAccounts) {
        accounts.add(transferAccounts.getFrom());
        accounts.add(transferAccounts.getTo());

        notifyAll();
    }
}
