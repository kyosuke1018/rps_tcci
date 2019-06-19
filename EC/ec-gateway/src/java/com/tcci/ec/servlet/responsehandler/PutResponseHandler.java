package com.tcci.ec.servlet.responsehandler;

import org.apache.commons.httpclient.methods.PutMethod;

public class PutResponseHandler extends BasicResponseHandler {

    public PutResponseHandler(PutMethod method) {
        super(method);
    }

}
