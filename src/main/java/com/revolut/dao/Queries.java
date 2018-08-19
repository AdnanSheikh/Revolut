package com.revolut.dao;

/**
 * Created by adnan on 8/18/2018.
 */
public class Queries {

    public final static String CREATE_CUSTOMER = "INSERT INTO CUSTOMER (first_name, last_name, personal_identity_no) VALUES (?, ?, ?)";

    public final static String GET_CUSTOMER_BY_PERSONAL_ID = "SELECT first_name, last_name from CUSTOMER where personal_identity_no = ?";

    public final static String GET_CUSTOMER_BY_ID = "SELECT first_name, last_name, personal_identity_no from CUSTOMER where customer_id = ?";

    public final static String GET_CURRENCY_BY_CODE = "SELECT name, symbol from CURRENCY where UPPER(code) = ?";

    public final static String CREATE_ACCOUNT = "INSERT INTO ACCOUNT (customer_id, currency_code, amount) VALUES (?, ?, ?)";

    public final static String GET_ACCOUNT = "SELECT customer_id, currency_code, amount from ACCOUNT where account_id = ?";

    public final static String GET_ACCOUNT_BY_CUSTOMER = "SELECT account_id from ACCOUNT where customer_id = ? and UPPER(currency_code) = ?";

    public final static String GET_ACCOUNTS = "SELECT account_id, customer_id, currency_code, amount from ACCOUNT where account_id in (?, ?)";

    public final static String EXCHANGE_RATE = "SELECT code_from, code_to, rate from EXCHANGE_RATE " +
                                                    "where (UPPER(code_from) = ? and UPPER(code_to) = ?) OR (UPPER(code_from) = ? and UPPER(code_to) = ?)";

    public final static String UPDATE_BALANCE = "update ACCOUNT set amount = ? where account_id = ?";

    public final static String TRANSACTION = "INSERT INTO TRANSACTION (from_account_id, to_account_id, amount, rate) VALUES (?, ?, ?, ?)";

    public final static String GET_TRANSACTION_BY_ID = "SELECT from_account_id, to_account_id, amount, rate from TRANSACTION where transaction_id = ?";
}