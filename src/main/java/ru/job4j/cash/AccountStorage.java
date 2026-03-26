package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (accounts.containsKey(account.id())) {
            return false;
        }
        accounts.put(account.id(), account);
        return true;
    }

    public synchronized boolean update(Account account) {
        // заменить только если уже существует
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (amount <= 0) {
            return false;
        }
        if (fromId == toId) {
            return false;
        }
        if (!accounts.containsKey(fromId) || !accounts.containsKey(toId)) {
            return false;
        }
        Account from = accounts.get(fromId);
        if (from.amount() < amount) {
            return false;
        }
        from = new Account(from.id(), from.amount() - amount);
        Account to = accounts.get(toId);
        to = new Account(to.id(), to.amount() + amount);
        accounts.replace(from.id(), from);
        accounts.replace(to.id(), to);
        return true;
    }
}
