package com.revolut.dao;

/**
 * Created by adnan on 8/19/2018.
 */
public interface CurrencyDao {

    boolean isCurrencyCodeValid(final String code);

}