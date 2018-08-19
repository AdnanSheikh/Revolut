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
public class CustomerResponse {
    @JsonIgnore
    private StatusCode code;

    @JsonProperty(required = true)
    private String message;

    @JsonProperty
    private Long customerId;

    @JsonProperty
    private Customer customer;

    public CustomerResponse() {
        super();
    }

    public CustomerResponse(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomerResponse(StatusCode code, String message, Long customerId, Customer customer) {
        this.code = code;
        this.message = message;
        this.customerId = customerId;
        this.customer = customer;
    }

    public StatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerResponse)) return false;
        CustomerResponse that = (CustomerResponse) o;
        return code == that.code &&
                Objects.equals(message, that.message) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, customerId, customer);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerResponse{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", customerId=").append(customerId);
        sb.append(", customer=").append(customer);
        sb.append('}');
        return sb.toString();
    }
}