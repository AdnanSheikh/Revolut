package com.revolut.service;

import com.revolut.dao.impl.TransactionDaoImpl;
import com.revolut.model.Transaction;
import com.revolut.model.TransactionResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.revolut.model.enums.StatusCode.mapCode;

/**
 * Created by adnan on 8/18/2018.
 */
@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

    @POST
    @Path("/execute")
    public Response transferFund(final Transaction transaction) {
        TransactionResponse response = new TransactionDaoImpl().transferFunds(transaction);
        return Response.status(mapCode(response.getCode(), true)).entity(response).build();
    }

    @GET
    @Path("/{transactionId}")
    public Response getCustomer(@PathParam("transactionId") final Long transactionId) {
        TransactionResponse response = new TransactionDaoImpl().getTransaction(transactionId);
        return Response.status(mapCode(response.getCode(), false)).entity(response).build();
    }
}