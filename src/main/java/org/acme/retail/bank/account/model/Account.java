package org.acme.retail.bank.account.model;

public class Account {

    private long id;
    private String accountId;

    public Account(){}

    public Account(String accountId) {
        super();
        this.accountId = accountId;
    }

    public Account(String accountId, long id) {
        super();
        this.accountId = accountId;
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public long getId() {
        return id;
    }

    public void setId(long key) {
        id = key;
    }
}
