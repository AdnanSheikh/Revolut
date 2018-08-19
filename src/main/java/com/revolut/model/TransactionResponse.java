package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.model.enums.StatusCode;

import java.util.Objects;

/**
 * Created by adnan on 8/18/2018.
 */
@JsonInclude(Include.NON_NULL)
public class TransactionResponse {
    @JsonIgnore
    private StatusCode code;

    @JsonProperty(required = true)
    private String message;

    @JsonProperty
    private Long transactionId;

    @JsonProperty
    private Transaction transaction;

    public TransactionResponse() {
        super();
    }

    public TransactionResponse(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public TransactionResponse(StatusCode code, String message, Long transactionId, Transaction transaction) {
        this.code = code;
        this.message = message;
        this.transactionId = transactionId;
        this.transaction = transaction;
    }

    public StatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionResponse)) return false;
        TransactionResponse that = (TransactionResponse) o;
        return code == that.code &&
                Objects.equals(message, that.message) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, transactionId, transaction);
    }
}