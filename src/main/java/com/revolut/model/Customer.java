package com.revolut.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Created by adnan on 8/18/2018.
 */
public class Customer {

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String lastName;

    @JsonProperty(required = true)
    private String personalIdentityNo;

    public Customer() {
        super();
    }

    public Customer(final String firstName, final String lastName, final String personalIdentityNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalIdentityNo = personalIdentityNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalIdentityNo() {
        return personalIdentityNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(personalIdentityNo, customer.personalIdentityNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, personalIdentityNo);
    }
}