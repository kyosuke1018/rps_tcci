package com.tcci.storegateway;

import java.util.HashSet;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.HttpMethod;

public class AllowedMethodHandler {

    private static String allowString;

    private static HashSet allowedMethods;

    public static String processAllowHeader(String allowSent) {
        StringBuilder allowToSend = new StringBuilder("");
        StringTokenizer tokenizer = new StringTokenizer(allowSent, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim().toUpperCase();
            if (allowedMethods.contains(token)) {
                allowToSend.append(token).append(",");
            }
        }
        return allowToSend.toString();
    }

    public static String getAllowHeader() {
        return allowString;
    }

    public static boolean methodAllowed(String method) {
        return allowedMethods.contains(method.toUpperCase());
    }

    public static boolean methodAllowed(HttpMethod method) {
        return methodAllowed(method.getName());
    }

    public synchronized static void setAllowedMethods(String allowed) {
        allowedMethods = new HashSet();
        allowString = allowed;
        StringTokenizer tokenizer = new StringTokenizer(allowed, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim().toUpperCase();
            allowedMethods.add(token);
        }
    }

}
