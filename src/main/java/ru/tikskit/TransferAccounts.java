package ru.tikskit;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public class TransferAccounts {
    private final Account from;
    private final Account to;

    public TransferAccounts(Account from, Account to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from == to) {
            throw new IllegalArgumentException();
        }
        this.from = from;
        this.to = to;
    }
}
