package com.revolut.dbconn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by adnan on 8/17/2018.
 */
public class DBInstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DBInstanceFactory.class);

    public static DBInstance getDBInstance(final String dbName){
        DBInstance dbConnection = null;
        logger.info(String.format("Creating database connection for DB: %s...", dbName));
        switch (dbName){
            case "h2":
                dbConnection = new H2DBInstance();
                break;
            case "sqllite":
                dbConnection = new SqlLiteDBInstance();
                break;
            default:
                dbConnection = new H2DBInstance();
        }
        return dbConnection;
    }
}