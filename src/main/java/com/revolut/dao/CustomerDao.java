package com.revolut.dao;

import com.revolut.model.Customer;
import com.revolut.model.CustomerResponse;

/**
 * Created by adnan on 8/18/2018.
 */
public interface CustomerDao {

    CustomerResponse createCustomer(final Customer customer);

    CustomerResponse getCustomer(final Long customerId);

}