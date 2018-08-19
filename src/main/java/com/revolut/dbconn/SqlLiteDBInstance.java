package com.revolut.dbconn;

/**
 * Created by adnan on 8/17/2018.
 */
public class SqlLiteDBInstance extends DBInstance {
    @Override
    protected String getDBName() {
        return "sqllite";
    }
}