package com.revolut.dao;

import com.revolut.model.Account;
import com.revolut.model.AccountResponse;

/**
 * Created by adnan on 8/18/2018.
 */
public interface AccountDao {

    AccountResponse createAccount(final Account account);

    AccountResponse getAccount(final Long accountId);

}