package com.revolut.dbconn;

/**
 * Created by adnan on 8/17/2018.
 */
public class H2DBInstance extends DBInstance {

    @Override
    protected String getDBName() {
        return "h2";
    }
}