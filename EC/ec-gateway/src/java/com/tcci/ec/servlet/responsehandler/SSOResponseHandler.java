/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.servlet.responsehandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletResponse;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOResponseHandler extends PostResponseHandler {

    private final static Logger logger = LoggerFactory.getLogger(SSOResponseHandler.class);

    public SSOResponseHandler(PostMethod method) {
        super(method);
    }

    @Override
    protected void sendStreamToClient(ServletResponse response) throws IOException {
        if (201 == getStatusCode()) {
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                    .matcher(output);
            if (matcher.matches()) {
                output = matcher.group(1);
            }
            OutputStream responseStream = response.getOutputStream();
            IOUtils.write(output, responseStream, "UTF-8");
            responseStream.flush();
            responseStream.close();
        } else if (400 == getStatusCode()) {
            // String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            // System.out.println(output);
            String errMsg = "tccstore login fail! invalid account or password.";
            logger.warn(errMsg);
            OutputStream responseStream = response.getOutputStream();
            IOUtils.write(errMsg, responseStream, "UTF-8");
            responseStream.flush();
            responseStream.close();
        } else {
            logger.warn("login fail!, status={}", getStatusCode());
            super.sendStreamToClient(response);
        }
    }

}
