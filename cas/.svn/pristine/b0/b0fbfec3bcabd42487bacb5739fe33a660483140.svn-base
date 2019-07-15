/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sso.client.test;

import com.tcci.sso.client.SSOClient;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClientTest {
    private static final Logger LOG = Logger.getLogger(SSOClientTest.class.getName());
    public static void main(final String[] args) throws IOException {
        // SSO server
        String server = "http://192.168.203.81/cas-server/v1/tickets";
        // 使用者帳號密碼
        String username = "devadmin";
        String password = "abcd123$";
        
        // 首次登入, 成功後請保留 tgt
        String tgt = SSOClient.getTicketGrantingTicket(server, username, password);
        if (null == tgt) {
            LOG.info("無法登入!");
            return;
        }
        
        // RESTful service
        String service = "http://192.168.203.81/dashboard/service/dashboard/ssouser";
        
        // 每次呼叫servicea要先跟SSO取得service ticket
        String scTicket = SSOClient.getServiceTicket(server, tgt, service);
        if (null == scTicket) {
            LOG.info("認證已過期，請重新登入取得新的tgt");
            return;
        }
        
        HttpResponse response = SSOClient.callService(server, scTicket, service);
        if (response != null) {
            String responseString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            // 200: 正確
            // 403: 未授權
            // 404: service 不存在
            // 500: service error
            LOG.info("response code:" + statusCode);
            LOG.info("Response (1k): "
                    + responseString.substring(0, Math.min(1024, responseString.length())));
        }
    }
}
