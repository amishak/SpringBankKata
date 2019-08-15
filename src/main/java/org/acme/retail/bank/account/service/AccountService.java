package org.acme.retail.bank.account.service;

import org.acme.retail.bank.account.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private AtomicLong sequence = new AtomicLong(1);
    private Map<Long, Account> repository = new HashMap<>();

    public Account register(Account account) {
        long key = sequence.incrementAndGet();
        account.setId(key);
        repository.put(key, account);
        return account;
    }
}
