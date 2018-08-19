package com.revolut.dao.impl;

import com.revolut.ApplicationContext;
import com.revolut.dao.CurrencyDao;
import com.revolut.dao.Queries;
import com.revolut.dbconn.DBInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by adnan on 8/19/2018.
 */
public class CurrencyDaoImpl implements CurrencyDao{

    private static final Logger logger = LoggerFactory.getLogger(CurrencyDaoImpl.class);

    private final DBInstance dbInstance;

    public CurrencyDaoImpl(){
        dbInstance = ApplicationContext.getInstance().getDBInstance();
    }

    @Override
    public boolean isCurrencyCodeValid(final String code){
        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStatement = getCurrencyPS(conn, code);
             ResultSet rs = pStatement.executeQuery()) {
             if (rs.next()) {
                logger.info(String.format("Currency code %s valid", code));
                return true;
             }
        } catch (SQLException ex){
            logger.error("Exception while fetching currency code: ", ex);
        }
        return false;
    }

    private PreparedStatement getCurrencyPS(final Connection conn, final String code) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_CURRENCY_BY_CODE);
        pStatement.setString(1, code.toUpperCase());
        return pStatement;
    }
}