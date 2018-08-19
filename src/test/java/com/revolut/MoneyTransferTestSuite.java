package com.revolut;

/**
 * Created by adnan on 8/19/2018.
 */

import com.revolut.service.TestAccountService;
import com.revolut.service.TestCustomerService;
import com.revolut.service.TestTransactionService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCustomerService.class,
        TestAccountService.class,
        TestTransactionService.class
})

public class MoneyTransferTestSuite {
    public static HttpClient client;
    public static URIBuilder uriBuilder;

    @BeforeClass
    public static void setUp() throws Exception {
        BootApplication.main(null);
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(50);
        connManager.setMaxTotal(50);
        client = HttpClients.custom().setConnectionManager(connManager)
                                     .setConnectionManagerShared(true).build();
        uriBuilder = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080);
    }

    @AfterClass
    public static void tearDown(){
        HttpClientUtils.closeQuietly(client);
    }
}