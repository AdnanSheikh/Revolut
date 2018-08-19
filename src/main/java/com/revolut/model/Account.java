package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by adnan on 8/18/2018.
 */
public class Account {

    @JsonProperty(required = true)
    private Long customerId;

    @JsonProperty(required = true)
    private String currencyCode;

    @JsonProperty(required = true)
    private BigDecimal amount;

    public Account(){
        super();
    }

    public Account(final Long customerId, final String currencyCode, final BigDecimal amount) {
        this.customerId = customerId;
        this.currencyCode = currencyCode;
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return customerId == account.customerId &&
                Objects.equals(currencyCode, account.currencyCode) &&
                Objects.equals(amount, account.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, currencyCode, amount);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append(", customerId=").append(customerId);
        sb.append(", currencyCode='").append(currencyCode).append('\'');
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}