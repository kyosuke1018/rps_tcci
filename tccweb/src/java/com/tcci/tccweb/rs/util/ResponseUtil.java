package com.tcci.tccweb.rs.util;

import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *  Util for RESTful services.
 * @author Jackson.Lee
 */
public class ResponseUtil {

    /**
     * Create Response with JSON format.
     * @param wrapper
     * @return 
     */
    public static Response createJsonResponse(Object wrapper) {
        Gson gson = new Gson();
        String json = gson.toJson(wrapper);
        return Response.ok(json).type(MediaType.APPLICATION_JSON).build();
    }
}
