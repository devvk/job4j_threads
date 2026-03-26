package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    /**
     * Перевод, если не хватает денег.
     */
    @Test
    void whenTransferAndNotEnoughMoneyThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 50));
        storage.add(new Account(2, 100));
        var result = storage.transfer(1, 2, 60);
        assertThat(result).isFalse();
        assertThat(storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"))
                .amount()).isEqualTo(50);
        assertThat(storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"))
                .amount()).isEqualTo(100);
    }

    /**
     * Перевод на несуществующий счёт.
     */
    @Test
    void whenTransferToMissingAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
    }

    /**
     * Перевод с несуществующего счёта.
     */
    @Test
    void whenTransferFromMissingAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(2, 100));
        var result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
    }

    /**
     * Перевод самому себе.
     */
    @Test
    void whenTransferToSameAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var result = storage.transfer(1, 1, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"))
                .amount()).isEqualTo(100);
    }

    /**
     * Перевод нулевой суммы.
     */
    @Test
    void whenTransferZeroAmountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        var result = storage.transfer(1, 2, 0);
        assertThat(result).isFalse();
    }

    /**
     * Перевод отрицательной суммы.
     */
    @Test
    void whenTransferNegativeAmountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        var result = storage.transfer(1, 2, -100);
        assertThat(result).isFalse();
    }
}
