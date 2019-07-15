/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sso.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClient {

    private static final Logger LOG = Logger.getLogger(SSOClient.class.getName());

    private SSOClient() {
    }

    private static String getTicket(final String server, final String username,
            final String password, final String service) {
        notNull(server, "server must not be null");
        notNull(username, "username must not be null");
        notNull(password, "password must not be null");
        notNull(service, "service must not be null");
        return getServiceTicket(server, getTicketGrantingTicket(server, username,
                password), service);
    }

    public static String getServiceTicket(final String server,
            final String ticketGrantingTicket, final String service) {
        if (ticketGrantingTicket == null) {
            return null;
        }

        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

        HttpPost post = new HttpPost(server + "/" + ticketGrantingTicket);
        ArrayList<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(new BasicNameValuePair("service", service));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String responseString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case HttpStatus.SC_OK:
                    return responseString;
                default:
                    LOG.warning("Invalid response code (" + statusCode + ") from CAS server!");
                    LOG.info("Response (1k): "
                            + responseString.substring(0, Math.min(1024, responseString.length())));
                    break;
            }
        } catch (final IOException e) {
            LOG.warning(e.getMessage());
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    public static String getTicketGrantingTicket(final String server,
            final String username, final String password) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);

        ArrayList<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(new BasicNameValuePair("username", username));
        pairList.add(new BasicNameValuePair("password", password));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println("getTicketGrantingTicket responseString=\n" + responseString);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 201: {
                    Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                            .matcher(responseString);
                    if (matcher.matches()) {
                        return matcher.group(1);
                    }
                    LOG.warning("Successful ticket granting request, but no ticket found!");
                    LOG.info("Response (1k): "
                            + responseString.substring(0, Math.min(1024, responseString.length())));
                    break;
                }
                default:
                    LOG.warning("Invalid response code (" + statusCode + ") from CAS server!");
                    LOG.info("Response (1k): "
                            + responseString.substring(0, Math.min(1024, responseString.length())));
                    break;
            }
        } catch (final IOException e) {
            LOG.warning(e.getMessage());
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    private static void notNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static HttpResponse callService(String server, String username, String password, String service) {
        String tgt = getTicketGrantingTicket(server, username, password);
        String scTicket = getServiceTicket(server, tgt, service);
        return callService(server, scTicket, service);
    }

    public static HttpResponse callService(String server, String scTicket, String service) {
        if (scTicket != null) {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            String url;
            if (service.contains("?")) {
                url = service + "&ticket=" + scTicket;
            } else {
                url = service + "?ticket=" + scTicket;
            }
            LOG.info(url);
            HttpGet httpGet = new HttpGet(url);
            try {
                return client.execute(httpGet);
            } catch (IOException ex) {
                LOG.warning("scTicket is null");
            }
        } else {
            LOG.warning("scTicket is null");
        }
        return null;
    }

    public static void main(final String[] args) throws IOException {
        String server = "http://192.168.203.50/cas-server/v1/tickets";
        String username = "peter.pan";
        String password = "Abcd1234";
        String service = "http://192.168.203.50/TCJCoServer/resources/callRfcSsoService/transSAPToDWAsync?sapClientCode=tcc&fileType=rfq&fileNoS=2100012084";
        HttpResponse response = callService(server, username, password, service);
        if (response != null) {
            String responseString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            LOG.info("response code:" + statusCode);
            LOG.info("Response (1k): "
                    + responseString.substring(0, Math.min(1024, responseString.length())));
        }
    }
}
