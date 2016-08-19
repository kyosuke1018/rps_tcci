package com.tcci.storegateway.responsehandler;

import org.apache.commons.httpclient.methods.DeleteMethod;

public class DeleteResponseHandler extends BasicResponseHandler {

    public DeleteResponseHandler(DeleteMethod method) {
        super(method);
    }

}
