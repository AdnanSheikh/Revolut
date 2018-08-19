package com.revolut.dbconn;

import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.revolut.config.ApplicationConfig.getProperty;

/**
 * Created by adnan on 8/17/2018.
 */
public abstract class DBInstance {

    private static final String SCRIPT_FILENAME = "tables.sql";

    private static final String DRIVER_CLASS_NAME = "db.driverClassName";
    private static final String URL = "db.url";
    private static final String USER_NAME = "db.username";
    private static final String PASSWORD = "db.password";

    public DBInstance(){
        DbUtils.loadDriver(getProperty(getDBName(), DRIVER_CLASS_NAME));
    }

    public Connection getConnection() throws SQLException {
        String url = getProperty(getDBName(), URL);
        String username = getProperty(getDBName(), USER_NAME);
        String password = getProperty(getDBName(), PASSWORD);
        return DriverManager.getConnection(url, username, password);
    }

    public void createTables() throws SQLException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(SCRIPT_FILENAME);
        try (Connection con = getConnection()) {
            RunScript.execute(con, new InputStreamReader(inputStream));
        }
    }

    protected abstract String getDBName();
}