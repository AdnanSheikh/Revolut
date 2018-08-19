package com.revolut;

import com.revolut.dbconn.DBInstance;
import com.revolut.service.AccountService;
import com.revolut.service.CustomerService;
import com.revolut.service.TransactionService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.revolut.config.ApplicationConfig.getIntProperty;

/**
 * Created by adnan on 8/17/2018.
 */
public class BootApplication {
    private final static Logger logger = LoggerFactory.getLogger(BootApplication.class);

    public static void main(String[] args) throws Exception {
        DBInstance dbInstance = ApplicationContext.getInstance().getDBInstance();
        if(Objects.nonNull(dbInstance)){
            logger.info("Database and table initialize successfully");
        }
        startJettyServer();
    }

    private static void startJettyServer() throws Exception {
        logger.info("Starting server...");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server server = new Server(getIntProperty("server.port"));
        server.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                String.join(",", CustomerService.class.getCanonicalName(),
                        AccountService.class.getCanonicalName(),
                        TransactionService.class.getCanonicalName()));

        server.start();
        logger.info("Server started...");
    }
}