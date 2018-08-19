package com.revolut;

import com.revolut.dbconn.DBInstance;
import com.revolut.dbconn.DBInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.revolut.config.ApplicationConfig.getProperty;

/**
 * Created by adnan on 8/18/2018.
 */
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private static final String DB_NAME = "db.name";

    private static ApplicationContext instance;

    private DBInstance dbInstance;

    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (ApplicationContext .class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

    private ApplicationContext() {
        try {
            logger.info("Initializing in-memory database...");
            dbInstance = DBInstanceFactory.getDBInstance(getProperty(DB_NAME));
            logger.info("Initialized in-memory database successfully...");

            logger.info("Creating tables ...");
            dbInstance.createTables();
            logger.info("Table created ...");
        } catch (SQLException ex){
            logger.error("Unable to create tables in database", ex);
        }
    }

    public DBInstance getDBInstance(){
        return dbInstance;
    }
}