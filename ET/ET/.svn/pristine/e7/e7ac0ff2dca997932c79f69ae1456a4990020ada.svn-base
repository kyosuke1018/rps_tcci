/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Peter.pan
 */
public class BaseRESTClient {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    final static int REST_CONNECT_TIMEOUT = 10000;// 10 seconds
    final static int REST_READ_TIMEOUT = 120000;// 2 minutes
    
    protected WebTarget webTarget;
    protected Client client;
    
    /**
     * Constructor
     * @param wsroot 
     */
    public BaseRESTClient(String wsroot) {//ex "http://localhost:8080/TCJCoServer/resources"
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, REST_CONNECT_TIMEOUT);
        config.property(ClientProperties.READ_TIMEOUT, REST_READ_TIMEOUT);
        
        init(wsroot, config);
    }
    public BaseRESTClient(String wsroot, ClientConfig config) {//ex "http://localhost:8080/TCJCoServer/resources";
        init(wsroot, config);
    }
    public BaseRESTClient(){}

    protected void buildWidthConfig(String wsroot, ClientConfig config){
        init(wsroot, config);
    }
    
    private void init(String wsroot, ClientConfig config) {
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("howtodoinjava", "password");
        //config.register(feature) ;
        //config.register(JacksonFeature.class);
        // client = ClientBuilder.newBuilder().sslContext(getSSLContext()).withConfig(configuration).build();
        client = ClientBuilder.newBuilder().withConfig(config).build();
        //client = ClientBuilder.newClient(config);
        webTarget = client.target(wsroot);
    }
    
    public void close() {
        client.close();
    }

    protected HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        };
    }
    
    protected SSLContext getSSLContext() {
        javax.net.ssl.TrustManager x509 = new javax.net.ssl.X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext ctx = null;
        try {
            /*SslConfigurator sslConfig = SslConfigurator.newInstance()
                .trustStoreFile("./truststore_client")
                .trustStorePassword("secret-password-for-truststore")
                .keyStoreFile("./keystore_client")
                .keyPassword("secret-password-for-keystore");
            ctx = sslConfig.createSSLContext();*/
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new javax.net.ssl.TrustManager[]{x509}, null);
        } catch (java.security.GeneralSecurityException ex) {
        }
        return ctx;
    }    

    public WebTarget getWebTarget() {
        return webTarget;
    }

    public void setWebTarget(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
