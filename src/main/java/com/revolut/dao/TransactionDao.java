package com.revolut.dao;

import com.revolut.model.Transaction;
import com.revolut.model.TransactionResponse;

/**
 * Created by adnan on 8/18/2018.
 */
public interface TransactionDao {

    TransactionResponse transferFunds(final Transaction transaction);

    TransactionResponse getTransaction(final Long transactionId);
}