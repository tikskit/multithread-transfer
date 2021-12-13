package ru.tikskit;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ToString
public class Account {
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    private static final int DEFAULT_MONEY_AMOUNT = 10000;

    private final String ID;
    private int money = DEFAULT_MONEY_AMOUNT;

    public Account(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public int getMoney() {
        return money;
    }

    public void inc(int delta) {
        money = money + delta;
        logger.info("Сумма на счете {} была увеличена на {} и составляет теперь {}", ID, delta, money);
    }

    public void dec(int delta) {
        if (delta > money) {
            throw new IllegalArgumentException(String.format("Списываемое количество %s превышает сумму %s на счете %s", delta, money, ID));
        }
        money = money - delta;
        logger.info("Сумма на счете {} была уменьшена на {} и составляет теперь {}", ID, delta, money);
    }
}
