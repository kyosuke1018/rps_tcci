package com.tcci.ec.servlet.responsehandler;

import org.apache.commons.httpclient.methods.DeleteMethod;

public class DeleteResponseHandler extends BasicResponseHandler {

    public DeleteResponseHandler(DeleteMethod method) {
        super(method);
    }

}
