package com.revolut.model.enums;

import javax.ws.rs.core.Response;

/**
 * Created by adnan on 8/19/2018.
 */
public enum StatusCode {
    SUCCESS, FAIL, CONFLICT, NOT_FOUND, SYSTEM_ERROR;

    public static Response.Status mapCode(final StatusCode code, final boolean isCreateCall){
        switch (code){
            case SUCCESS:
                return isCreateCall ? Response.Status.CREATED: Response.Status.OK;
            case FAIL:
                return Response.Status.BAD_REQUEST;
            case NOT_FOUND:
                return Response.Status.NOT_FOUND;
            case CONFLICT:
                return Response.Status.CONFLICT;
            case SYSTEM_ERROR:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.Status.BAD_REQUEST;
    }
}