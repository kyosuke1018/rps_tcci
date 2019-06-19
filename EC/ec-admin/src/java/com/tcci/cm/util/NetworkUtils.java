/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Peter.pan
 */
public class NetworkUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);
    
    /**
     * 取得Server端HostName
     *
     * @return
     */
    public static String getHostName() {
        java.net.InetAddress server;
        String serverName = "";
        try {
            server = java.net.InetAddress.getLocalHost();
            serverName = server.getCanonicalHostName();
        } catch (UnknownHostException ex) {
            logger.error("getHostName", ex);
        }
        return serverName;
    }
    
    /**
     * 取得Server端IP
     *
     * @return
     */
    public static String getHostIP() {
        java.net.InetAddress server;
        String ip = "";
        try {
            server = java.net.InetAddress.getLocalHost();
            ip = server.getHostAddress();
        } catch (UnknownHostException ex) {
            logger.error("getHostIP", ex);
        }
        return ip;
    }
    
    /**
     * 是否為本機IP
     * @param checkIP
     * @return
     * @throws SocketException 
     */
    public static boolean isLocalIP(String checkIP) throws SocketException{
        List<String> ips = getLocalIPs();
        
        if( ips!=null ){
            for(String ip : ips){
                if( ip.equals(checkIP) ){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 取出本機IP
     * @return
     * @throws SocketException 
     */
    public static List<String> getLocalIPs() throws SocketException {
        List<String> list = new ArrayList<String>();
        
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if( !i.isAnyLocalAddress() && !i.isLoopbackAddress() ){
                    list.add(i.getHostAddress());
                }
                // logger.debug(i.getHostAddress());
            }
        }
        
        return list;
    }

    /**
     * 取得 IP 資訊
     * @return
     */
    public static String getLocalIPInfoString(){
        StringBuilder sb = new StringBuilder();
        try{
            List<String> ips = getLocalIPs();
            for(String ip : ips){
                sb.append("[").append(ip).append("]");
            }
        }catch(SocketException e){
            logger.error("getLocalIPInfoString exception:\n", e);
        }
        
        return sb.toString();
    }
    
    /**
     * 取得 IP 資訊 (IPV4)
     */
    public static String getLocalIPv4InfoString(){
        StringBuilder sb = new StringBuilder();
        try{
            List<String> ips = getLocalIPs();
            for(String ip : ips){
                if( ip.indexOf(":")<0 ){
                    sb.append("[").append(ip).append("]");
                }
            }
        }catch(SocketException e){
            logger.error("getLocalIPv4InfoString exception:\n", e);
        }
        
        return sb.toString();
    }
}
