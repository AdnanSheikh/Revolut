package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.model.enums.StatusCode;

import java.util.Objects;

/**
 * Created by adnan on 8/19/2018.
 */
@JsonInclude(Include.NON_NULL)
public class AccountResponse {
    @JsonIgnore
    private StatusCode code;

    @JsonProperty(required = true)
    private String message;

    @JsonProperty
    private Long accountId;

    @JsonProperty
    private Account account;

    public AccountResponse() {
        super();
    }

    public AccountResponse(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public AccountResponse(StatusCode code, String message, Long accountId, Account account) {
        this.code = code;
        this.message = message;
        this.accountId = accountId;
        this.account = account;
    }

    public StatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountResponse)) return false;
        AccountResponse response = (AccountResponse) o;
        return code == response.code &&
                Objects.equals(message, response.message) &&
                Objects.equals(accountId, response.accountId) &&
                Objects.equals(account, response.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, accountId, account);
    }
}
