package com.revolut.dao.impl;

import com.revolut.ApplicationContext;
import com.revolut.dao.Queries;
import com.revolut.dao.TransactionDao;
import com.revolut.dbconn.DBInstance;
import com.revolut.model.Account;
import com.revolut.model.Transaction;
import com.revolut.model.TransactionResponse;
import com.revolut.model.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Objects;

import static com.revolut.dao.Queries.*;
import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

/**
 * Created by adnan on 8/18/2018.
 */
public class TransactionDaoImpl implements TransactionDao {

    private static final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

    private final DBInstance dbInstance;

    public TransactionDaoImpl(){
        dbInstance = ApplicationContext.getInstance().getDBInstance();
    }

    @Override
    public TransactionResponse transferFunds(final Transaction transaction) {
        if(transaction.getFromAccountId().equals(transaction.getToAccountId())){
            String warnMsg = "From and to account must be different";
            logger.warn(warnMsg);
            return new TransactionResponse(StatusCode.FAIL, warnMsg);
        }

        try (Connection conn = dbInstance.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pStatement = getAccountsWithLockPS(conn, transaction.getFromAccountId(), transaction.getToAccountId());
                 ResultSet rs = pStatement.executeQuery()){
                 Account fromAccount = null, toAccount = null;
                 while(rs.next()){
                     if(rs.getLong("account_id") == transaction.getFromAccountId()){
                         fromAccount = new Account(rs.getLong("customer_id"),
                                                    rs.getString("currency_code"), rs.getBigDecimal("amount"));
                     } else if(rs.getLong("account_id") == transaction.getToAccountId()){
                         toAccount = new Account(rs.getLong("customer_id"),
                                                    rs.getString("currency_code"), rs.getBigDecimal("amount"));
                     }
                 }

                 if(Objects.isNull(fromAccount) || Objects.isNull(toAccount)) {
                    return getMissingAccount(fromAccount, toAccount, transaction);
                 }

                 // Check sufficient balance
                 if(fromAccount.getAmount().compareTo(transaction.getAmount()) < 0){
                     String warnMsg = String.format("Insufficient balance in account id [%d]", transaction.getFromAccountId());
                     logger.warn(warnMsg);
                     return new TransactionResponse(StatusCode.FAIL, warnMsg);
                 }

                 BigDecimal exchangeRate = new BigDecimal(1);
                 if(!fromAccount.getCurrencyCode().equals(toAccount.getCurrencyCode())){
                     try (PreparedStatement pStat = getExchangeRatePS(conn, fromAccount.getCurrencyCode(), toAccount.getCurrencyCode());
                          ResultSet rSet = pStat.executeQuery()){
                          if(rSet.next()){
                              if(rSet.getString("code_from").equals(fromAccount.getCurrencyCode())){
                                  exchangeRate = rSet.getBigDecimal("rate");
                              } else {
                                  exchangeRate = BigDecimal.ONE.divide(rSet.getBigDecimal("rate"), 2, RoundingMode.HALF_UP);
                              }
                          } else {
                              String errorMsg = String.format("No exchange rate defined from %s to %s",
                                                    fromAccount.getCurrencyCode(), toAccount.getCurrencyCode());
                              logger.error(errorMsg);
                              return new TransactionResponse(StatusCode.SYSTEM_ERROR, errorMsg);
                          }
                     }
                 }
                 Long transactionId = performTransaction(conn, fromAccount, toAccount, transaction, exchangeRate);
                 if(transactionId != -1L){
                     return new TransactionResponse(StatusCode.SUCCESS, "Transaction successfully completed...", transactionId, transaction);
                 }
                 return new TransactionResponse(StatusCode.FAIL, "Unable to process transaction...");
            } catch (SQLException ex){
                conn.rollback();
                logger.error("System error while processing transaction", ex);
                return new TransactionResponse(StatusCode.SYSTEM_ERROR, "Error while processing transaction...");
            }
        } catch (SQLException ex) {
            logger.error("Error occur while processing transaction", ex);
            return new TransactionResponse(StatusCode.SYSTEM_ERROR, "Error while processing transaction...");
        }
    }

    @Override
    public TransactionResponse getTransaction(final Long transactionId) {
        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStatement = getTransactionPS(conn, transactionId);
             ResultSet rs = pStatement.executeQuery()) {
            if(rs.next()){
                return new TransactionResponse(StatusCode.SUCCESS, "Transaction exist", transactionId,
                        new Transaction(rs.getLong("from_account_id"), rs.getLong("to_account_id"),
                                             rs.getBigDecimal("amount"), rs.getBigDecimal("rate")));
            } else {
                logger.warn("No transaction exist for transaction id: ", transactionId);
                return new TransactionResponse(StatusCode.NOT_FOUND, "Transaction not found");
            }
        } catch (SQLException ex) {
            logger.error("Error while getting transaction", ex);
            return new TransactionResponse(StatusCode.SYSTEM_ERROR, "Error while fetching transaction");
        }
    }

    private PreparedStatement getTransactionPS(final Connection conn, final Long transactionId) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_TRANSACTION_BY_ID);
        pStatement.setLong(1, transactionId);
        return pStatement;
    }

    private TransactionResponse getMissingAccount(final Account fromAccount, final Account toAccount, final Transaction transaction){
        String warnMsg;
        if(Objects.isNull(fromAccount)){
            warnMsg = String.format("From account [%d] isn't exist", transaction.getFromAccountId());
            logger.warn(warnMsg);
            return new TransactionResponse(StatusCode.FAIL, warnMsg);
        }
        warnMsg = String.format("To account [%d] isn't exist", transaction.getToAccountId());
        logger.warn(warnMsg);
        return new TransactionResponse(StatusCode.FAIL, warnMsg);
    }

    private PreparedStatement getAccountsWithLockPS(final Connection conn, final long fromAccountId, final long toAccountId) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(GET_ACCOUNTS, TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
        int index = 0;
        pStatement.setLong(++index, fromAccountId);
        pStatement.setLong(++index, toAccountId);
        return pStatement;
    }

    private PreparedStatement getExchangeRatePS(final Connection conn, final String fromCurrency, final String toCurrency) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(EXCHANGE_RATE);
        int index = 0;
        pStatement.setString(++index, fromCurrency.toUpperCase());
        pStatement.setString(++index, toCurrency.toUpperCase());
        pStatement.setString(++index, toCurrency.toUpperCase());
        pStatement.setString(++index, fromCurrency.toUpperCase());
        return pStatement;
    }

    private PreparedStatement createTransactionPS(final Connection conn, final long fromAccountId, final long toAccountId,
                                                        final BigDecimal amount, final BigDecimal exchangeRate) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(TRANSACTION, Statement.RETURN_GENERATED_KEYS);
        int index = 0;
        pStatement.setLong(++index, fromAccountId);
        pStatement.setLong(++index, toAccountId);
        pStatement.setBigDecimal(++index, amount);
        pStatement.setBigDecimal(++index, exchangeRate);
        return pStatement;
    }

    private Long performTransaction(final Connection conn, final Account fromAccount, final Account toAccount,
                                    final Transaction transaction, final BigDecimal exchangeRate) throws SQLException {
        try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_BALANCE)) {
            // Withdraw amount
            BigDecimal amountAfterWithDraw = fromAccount.getAmount().subtract(transaction.getAmount());
            updateStmt.setBigDecimal(1, amountAfterWithDraw);
            updateStmt.setLong(2, transaction.getFromAccountId());
            updateStmt.addBatch();

            // Deposit amount
            BigDecimal amountAfterDeposit = (transaction.getAmount().multiply(exchangeRate)).add(toAccount.getAmount());
            updateStmt.setBigDecimal(1, amountAfterDeposit);
            updateStmt.setLong(2, transaction.getToAccountId());
            updateStmt.addBatch();
            updateStmt.executeBatch();
        }

        try (PreparedStatement pStatement = createTransactionPS(conn, transaction.getFromAccountId(), transaction.getToAccountId(),
                transaction.getAmount(), exchangeRate)) {
            pStatement.executeUpdate();
            try (ResultSet rs = pStatement.getGeneratedKeys()) {
                if (rs != null && rs.next()) {
                    Long transactionId = rs.getLong(1);
                    logger.info("Transaction processed successfully...");
                    conn.commit();
                    return transactionId;
                } else {
                    logger.warn("Unable to create transaction...");
                    return -1L;
                }
            }
        }
    }
}