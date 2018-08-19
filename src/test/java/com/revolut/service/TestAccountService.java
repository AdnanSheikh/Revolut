package com.revolut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.model.Account;
import com.revolut.model.AccountResponse;
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
import static org.junit.Assert.assertTrue;

/**
 * Created by adnan on 8/19/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountService {

    @Test
    public void test1CreateFirstAccount() throws URISyntaxException, IOException {
        HttpResponse result = createAccount(new Account(1L, "CHF", new BigDecimal(20.00)));
        assertEquals(201, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test2CreateSecondAccount() throws URISyntaxException, IOException {
        HttpResponse result = createAccount(new Account(1L, "EUR", new BigDecimal(35.00)));
        assertEquals(201, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test3AccountAlreadyExist() throws URISyntaxException, IOException {
        HttpResponse response = createAccount(new Account(1L, "CHF", new BigDecimal(100.00)));
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test4InvalidCurrency() throws URISyntaxException, IOException {
        HttpResponse result = createAccount(new Account(1L, "XXX", new BigDecimal(350.00)));
        assertEquals(400, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test5FetchAccount() throws URISyntaxException, IOException {
        HttpResponse response = getAccount("1");
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountResponse accountResponse = new ObjectMapper().readValue(jsonString, AccountResponse.class);
        Account account = accountResponse.getAccount();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(account.getCurrencyCode().equals("CHF"));
        assertEquals(0, account.getAmount().compareTo(new BigDecimal(20.00)));
    }

    @Test
    public void test6FetchInvalidAccount() throws URISyntaxException, IOException {
        HttpResponse response = getAccount("3");
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountResponse accountResponse = new ObjectMapper().readValue(jsonString, AccountResponse.class);
        Account account = accountResponse.getAccount();
        assertEquals(404, response.getStatusLine().getStatusCode());
    }

    private HttpResponse createAccount(final Account account) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/account/create").build();
        HttpPost request = new HttpPost(uri);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(account)));
        return client.execute(request);
    }

    private HttpResponse getAccount(final String accountId) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/account/"+accountId).build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("content-type", "application/json");
        return client.execute(request);
    }
}