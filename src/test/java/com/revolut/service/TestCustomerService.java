package com.revolut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.model.Customer;
import com.revolut.model.CustomerResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
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
public class TestCustomerService {

    @Test
    public void test1CreateFirstCustomer() throws URISyntaxException, IOException {
        HttpResponse result = createCustomer(new Customer("Adnan", "Ahmad", "ZE123141"));
        assertEquals(201, result.getStatusLine().getStatusCode());
    }

    @Test
    public void test2CreateSecondCustomer() throws URISyntaxException, IOException {
        HttpResponse response = createCustomer(new Customer("Mubarrah", "Nadeem", "DH32EKQS"));
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test3CustomerAlreadyExist() throws URISyntaxException, IOException {
        HttpResponse response = createCustomer(new Customer("MUBARRAH", "Nadeem", "DH32EKQS"));
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test4FetchCustomer() throws URISyntaxException, IOException {
        HttpResponse response = getCustomer("1");
        String jsonString = EntityUtils.toString(response.getEntity());
        CustomerResponse custResponse = new ObjectMapper().readValue(jsonString, CustomerResponse.class);
        Customer customer = custResponse.getCustomer();
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(customer.getFirstName().equals("Adnan"));
        assertTrue(customer.getLastName().equals("Ahmad"));
    }

    @Test
    public void test4FetchInvalidCustomer() throws URISyntaxException, IOException {
        HttpResponse response = getCustomer("3");
        String jsonString = EntityUtils.toString(response.getEntity());
        CustomerResponse custResponse = new ObjectMapper().readValue(jsonString, CustomerResponse.class);
        Customer customer = custResponse.getCustomer();
        assertEquals(404, response.getStatusLine().getStatusCode());
    }

    private HttpResponse  createCustomer(final Customer customer) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/customer/create").build();
        HttpPost request = new HttpPost(uri);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(customer)));
        return client.execute(request);
    }

    private HttpResponse getCustomer(final String customerId) throws URISyntaxException, IOException {
        URI uri = uriBuilder.setPath("/customer/"+customerId).build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("content-type", "application/json");
        return client.execute(request);
    }
}