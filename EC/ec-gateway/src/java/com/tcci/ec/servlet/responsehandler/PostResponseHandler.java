package com.tcci.ec.servlet.responsehandler;

import org.apache.commons.httpclient.methods.PostMethod;

public class PostResponseHandler extends BasicResponseHandler {

    public PostResponseHandler(PostMethod method) {
        super(method);
    }

}
