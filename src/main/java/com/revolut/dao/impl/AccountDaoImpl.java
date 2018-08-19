package com.revolut.dao.impl;

import com.revolut.ApplicationContext;
import com.revolut.dao.AccountDao;
import com.revolut.dao.Queries;
import com.revolut.dbconn.DBInstance;
import com.revolut.model.Account;
import com.revolut.model.AccountResponse;
import com.revolut.model.CustomerResponse;
import com.revolut.model.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by adnan on 8/18/2018.
 */
public class AccountDaoImpl implements AccountDao {

    private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final DBInstance dbInstance;

    public AccountDaoImpl(){
        dbInstance = ApplicationContext.getInstance().getDBInstance();
    }

    @Override
    public AccountResponse createAccount(Account account) {
        CustomerResponse response = new CustomerDaoImpl().getCustomer(account.getCustomerId());
        if(response.getCode() != StatusCode.SUCCESS){
            return new AccountResponse(response.getCode(), response.getMessage());
        }

        boolean isValid = new CurrencyDaoImpl().isCurrencyCodeValid(account.getCurrencyCode());
        if(!isValid){
            return new AccountResponse(StatusCode.FAIL, "Invalid currency code provided");
        }

        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStat = getAccountPS(conn, account);
             ResultSet rStat = pStat.executeQuery()) {
             if(rStat.next()){
                return new AccountResponse(StatusCode.CONFLICT, "Account already exist");
             }

            try (PreparedStatement pStatement = createAccountPS(conn, account)) {
                pStatement.executeUpdate();
                try (ResultSet rs = pStatement.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        Long accountId = rs.getLong(1);
                        logger.info("Account successfully created: ", accountId);
                        return new AccountResponse(StatusCode.SUCCESS, "Account created ...", accountId, account);
                    } else {
                        logger.warn("Unable to create account...");
                        return new AccountResponse(StatusCode.FAIL, "Unable to create account");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Unable to create account: ", ex);
            return new AccountResponse(StatusCode.SYSTEM_ERROR, "Error while account creation");
        }
    }

    @Override
    public AccountResponse getAccount(Long accountId) {
        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStatement = getAccountPS(conn, accountId);
             ResultSet rs = pStatement.executeQuery()) {
             if(rs.next()){
                 return new AccountResponse(StatusCode.SUCCESS, "Account exist", accountId,
                                            new Account(rs.getLong("customer_id"), rs.getString("currency_code"),
                                                            rs.getBigDecimal("amount")));
             } else {
                 logger.warn("No account exist for account id: ", accountId);
                 return new AccountResponse(StatusCode.NOT_FOUND, "Account not found");
             }
        } catch (SQLException ex) {
            logger.error("Unable to get account: ", ex);
            return new AccountResponse(StatusCode.SYSTEM_ERROR, "Error while fetching account");
        }
    }

    private PreparedStatement createAccountPS(final Connection conn, final Account account) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.CREATE_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
        int index = 0;
        pStatement.setLong(++index, account.getCustomerId());
        pStatement.setString(++index, account.getCurrencyCode().toUpperCase());
        pStatement.setBigDecimal(++index, account.getAmount());
        return pStatement;
    }

    private PreparedStatement getAccountPS(final Connection conn, final Long accountId) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_ACCOUNT);
        pStatement.setLong(1, accountId);
        return pStatement;
    }

    private PreparedStatement getAccountPS(final Connection conn, final Account account) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_ACCOUNT_BY_CUSTOMER);
        int index = 0;
        pStatement.setLong(++index, account.getCustomerId());
        pStatement.setString(++index, account.getCurrencyCode().toUpperCase());
        return pStatement;
    }
}