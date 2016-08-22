package com.tcci.storegateway.responsehandler;

import org.apache.commons.httpclient.methods.GetMethod;

public class GetResponseHandler extends BasicResponseHandler {

    public GetResponseHandler(GetMethod method) {
        super(method);
    }

}
