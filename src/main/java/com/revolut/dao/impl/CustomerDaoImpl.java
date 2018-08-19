package com.revolut.dao.impl;

import com.revolut.ApplicationContext;
import com.revolut.dao.CustomerDao;
import com.revolut.dao.Queries;
import com.revolut.dbconn.DBInstance;
import com.revolut.model.Customer;
import com.revolut.model.CustomerResponse;
import com.revolut.model.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by adnan on 8/18/2018.
 */
public class CustomerDaoImpl implements CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

    private final DBInstance dbInstance;

    public CustomerDaoImpl(){
        dbInstance = ApplicationContext.getInstance().getDBInstance();
    }

    @Override
    public CustomerResponse createCustomer(final Customer customer) {
        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStatement = getCustomerPS(conn, customer);
             ResultSet rs = pStatement.executeQuery()) {
             if (rs.next()) {
                return new CustomerResponse(StatusCode.CONFLICT, "Customer already exist");
             }

             try (PreparedStatement pStat = createCustomerPS(conn, customer)) {
                 pStat.executeUpdate();
                 try (ResultSet rSet = pStat.getGeneratedKeys()){
                    if(rSet != null && rSet.next()){
                        Long customerId = rSet.getLong(1);
                        logger.info("Customer successfully created: ", customerId);
                        return new CustomerResponse(StatusCode.SUCCESS, "Customer created...", customerId, customer);
                    } else {
                        logger.warn("Unable to create customer...");
                        return new CustomerResponse(StatusCode.FAIL, "Unable to create customer");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Error while creating customer", ex);
            return new CustomerResponse(StatusCode.SYSTEM_ERROR, "Error while customer creation");
        }
    }

    @Override
    public CustomerResponse getCustomer(final Long customerId) {
        try (Connection conn = dbInstance.getConnection();
             PreparedStatement pStatement = getCustomerPS(conn, customerId);
             ResultSet rs = pStatement.executeQuery()) {
             if(rs.next()){
                return new CustomerResponse(StatusCode.SUCCESS, "Customer exist", customerId,
                                new Customer(rs.getString("first_name"), rs.getString("last_name"),
                                            rs.getString("personal_identity_no")));
            } else {
                logger.warn("No customer exist for customer id: ", customerId);
                return new CustomerResponse(StatusCode.NOT_FOUND, "Customer not found");
            }
        } catch (SQLException ex) {
            logger.error("Error while getting customer", ex);
            return new CustomerResponse(StatusCode.SYSTEM_ERROR, "Error while fetching customer");
        }
    }

    private PreparedStatement createCustomerPS(final Connection conn, final Customer customer) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.CREATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
        int index = 0;
        pStatement.setString(++index, customer.getFirstName());
        pStatement.setString(++index, customer.getLastName());
        pStatement.setString(++index, customer.getPersonalIdentityNo());
        return pStatement;
    }

    private PreparedStatement getCustomerPS(final Connection conn, final Long customerId) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_CUSTOMER_BY_ID);
        pStatement.setLong(1, customerId);
        return pStatement;
    }

    private PreparedStatement getCustomerPS(final Connection conn, final Customer customer) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(Queries.GET_CUSTOMER_BY_PERSONAL_ID);
        pStatement.setString(1, customer.getPersonalIdentityNo());
        return pStatement;
    }
}