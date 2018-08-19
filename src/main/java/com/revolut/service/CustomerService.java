package com.revolut.service;

import com.revolut.dao.impl.CustomerDaoImpl;
import com.revolut.model.Customer;
import com.revolut.model.CustomerResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.revolut.model.enums.StatusCode.mapCode;

/**
 * Created by adnan on 8/18/2018.
 */
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerService {

    @POST
    @Path("/create")
    public Response createCustomer(final Customer customer) {
       CustomerResponse response = new CustomerDaoImpl().createCustomer(customer);
       return Response.status(mapCode(response.getCode(), true)).entity(response).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomer(@PathParam("customerId") final Long customerId) {
        CustomerResponse response = new CustomerDaoImpl().getCustomer(customerId);
        return Response.status(mapCode(response.getCode(), false)).entity(response).build();
    }
}