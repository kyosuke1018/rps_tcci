/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.tcci.et.facade.rs.client.CallRfcRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class TestMain {
    public final static Logger logger = LoggerFactory.getLogger(CallRfcRestClient.class);
    
    private static final String URL_TCJCOSERVER_ROOT = "http://localhost:8080/TCJCoServer/resources";
    private static final String JWT_QAS = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3ZWIucmVzdGZ1bCIsImlzcyI6Ind3dy50YWl3YW5jZW1lbnQuY29tIiwiaWF0IjoxNTQyMDc1MjcyLCJleHAiOjMzMDY3NDQ4MDcyfQ.mg6QagkC8Nb5Uoh6DENQ_0aOGn_MijsPxcbgxeWYwMmcKewRMyW5URCCHm5jpLqXR7BliXQo9wUBByMAhMGMPQ";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CallRfcRestClient client = new CallRfcRestClient(URL_TCJCOSERVER_ROOT);
        
        client.setJwt(JWT_QAS);
        String res = client.transSAPToDW("po", "3160005905", "tcc_cn");
        logger.debug("res = "+res);
    }
    
}
