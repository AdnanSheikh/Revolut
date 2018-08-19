package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by adnan on 8/18/2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    @JsonProperty(required = true)
    private Long fromAccountId;

    @JsonProperty(required = true)
    private Long toAccountId;

    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty
    private BigDecimal rate;

    public Transaction() {
        super();
    }

    public Transaction(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Transaction(Long fromAccountId, Long toAccountId, BigDecimal amount, BigDecimal rate) {
        this(fromAccountId, toAccountId, amount);
        this.rate = rate;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(fromAccountId, that.fromAccountId) &&
                Objects.equals(toAccountId, that.toAccountId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAccountId, toAccountId, amount, rate);
    }
}
