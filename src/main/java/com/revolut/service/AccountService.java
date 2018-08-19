package com.revolut.service;

import com.revolut.dao.impl.AccountDaoImpl;
import com.revolut.model.Account;
import com.revolut.model.AccountResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.revolut.model.enums.StatusCode.mapCode;

/**
 * Created by adnan on 8/18/2018.
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    @POST
    @Path("/create")
    public Response createAccount(final Account account) {
        AccountResponse response = new AccountDaoImpl().createAccount(account);
        return Response.status(mapCode(response.getCode(), true)).entity(response).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccount(@PathParam("accountId") final Long accountId) {
        AccountResponse response = new AccountDaoImpl().getAccount(accountId);
        return Response.status(mapCode(response.getCode(), false)).entity(response).build();
    }
}