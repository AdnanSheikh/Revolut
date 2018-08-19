package com.revolut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.model.Transaction;
import com.revolut.model.TransactionResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static com.revolut.MoneyTransferTestSuite.client;
import static com.revolut.MoneyTransferTestSuite.uriBuilder;
import static org.junit.Assert.assertEquals;

/**
 * Created by adnan on 8/19/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTransactionService {

    @Test
    public void test1TransferFund() throws URISyntaxException, IOException {
        HttpResponse result = createTransaction(new Transaction(1L, 2L, new BigDecimal(8.00)));
        assertEquals(201, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test2TransferFund() throws URISyntaxException, IOException {
        HttpResponse result = createTransaction(new Transaction(1L, 2L, new BigDecimal(8.00)));
        assertEquals(201, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test3InsufficientBalance() throws URISyntaxException, IOException {
        HttpResponse result = createTransaction(new Transaction(1L, 2L, new BigDecimal(8.00)));
        assertEquals(400, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test4FetchTransaction() throws URISyntaxException, IOException {
        HttpResponse response = getTransaction("1");
        String jsonString = EntityUtils.toString(response.getEntity());
        TransactionResponse transResponse = new ObjectMapper().readValue(jsonString, TransactionResponse.class);
        Transaction transaction = transResponse.getTransaction();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(0, transaction.getAmount().compareTo(new BigDecimal(8.00)));
        assertEquals(1L, transaction.getFromAccountId().longValue());
        assertEquals(2L, transaction.getToAccountId().longValue());
    }

    @Test
    public void test5FetchInvalidTransaction() throws URISyntaxException, IOException {
        HttpResponse response = getTransaction("3");
        String jsonString = EntityUtils.toString(response.getEntity());
        TransactionResponse transResponse = new ObjectMapper().readValue(jsonString, TransactionResponse.class);
        Transaction transaction = transResponse.getTransaction();
        assertEquals(404, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test6InvalidAccount() throws URISyntaxException, IOException {
        HttpResponse result = createTransaction(new Transaction(1L, 3L, new BigDecimal(8.00)));
        assertEquals(400, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test7SameAccount() throws URISyntaxException, IOException {
        HttpResponse result = createTransaction(new Transaction(1L, 1L, new BigDecimal(8.00)));
        assertEquals(400, result.getStatusLine().getStatusCode());
    }

    private HttpResponse createTransaction(final Transaction transaction) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/transaction/execute").build();
        HttpPost request = new HttpPost(uri);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(transaction)));
        return client.execute(request);
    }

    private HttpResponse getTransaction(final String transactionId) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/transaction/"+transactionId).build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("content-type", "application/json");
        return client.execute(request);
    }
}