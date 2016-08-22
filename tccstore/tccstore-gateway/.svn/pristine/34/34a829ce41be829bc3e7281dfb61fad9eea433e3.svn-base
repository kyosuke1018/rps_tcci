package com.tcci.storegateway.responsehandler;

import com.tcci.storegateway.AllowedMethodHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OptionsResponseHandler extends ResponseHandlerBase {

    private static final Log log = LogFactory.getLog(OptionsResponseHandler.class);
    private final boolean useOwnAllow;

    public OptionsResponseHandler(OptionsMethod method) {
        super(method);
        useOwnAllow = !method.hasBeenUsed();
    }

    @Override
    public void process(HttpServletResponse response) {
        if (useOwnAllow) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("allow", AllowedMethodHandler.getAllowHeader());
            response.setHeader("Connection", "close");
            response.setHeader("content-length", "0");
        } else {
            setHeaders(response);
            response.setStatus(getStatusCode());
            String allow = method.getResponseHeader("allow").getValue();
            response.setHeader("allow", AllowedMethodHandler.processAllowHeader(allow));
            Header contentLength = method.getResponseHeader("Content-Length");
            if (contentLength == null || contentLength.getValue().equals("0")) {
                response.setHeader("Content-Length", "0");
            } else {
                try {
                    sendStreamToClient(response);
                } catch (IOException e) {
                    log.error("Problem with writing response stream, solving by setting Content-Length=0", e);
                    response.setHeader("Content-Length", "0");
                }
            }
        }
    }

    @Override
    public int getStatusCode() {
        if (useOwnAllow) {
            return 200;
        } else {
            return super.getStatusCode();
        }
    }

}
