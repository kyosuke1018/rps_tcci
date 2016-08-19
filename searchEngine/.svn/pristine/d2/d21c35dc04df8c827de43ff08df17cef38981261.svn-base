package com.tcci.solr.server.filter;

import com.tcci.solr.server.util.NetworkUtils;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 限制 IP 存取 Filter
 * @author Peter
 */
public class IpRestrictFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(IpRestrictFilter.class);
    
    private static String VALIDATE_IP_PARAM = "ip";
    private static String DELIMITER = ",";
    
    private List<String> validateIpList;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 至 web.xml 取的合法 IP
        String ips =  filterConfig.getInitParameter(VALIDATE_IP_PARAM);
        if( ips!=null ){
            String[] validateIPs = ips.split(DELIMITER);
            validateIpList = new ArrayList<String>();
            
            if( validateIPs!=null ){
                List<String> localIPs = null; // localhost 特別處理
                try {
                    localIPs = NetworkUtils.getLocalIPs();
                    validateIpList.addAll(localIPs);
                } catch (SocketException ex) {
                    logger.error("init can not get local IP !");
                }
                
                for (String ip : validateIPs) {
                    if( !ip.trim().isEmpty() && !validateIpList.contains(ip) ){
                        validateIpList.add(ip.trim());
                    }
                }
                
                for(String ip : validateIpList){
                    logger.info("IpRestrictFilter : validate IP = "+ip);
                }
            }
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse rsp = (HttpServletResponse) response;
        String remoteAddr = request.getRemoteAddr();
        logger.info("doFilter remoteAddr = "+remoteAddr);
        if( validateIpList==null || validateIpList.isEmpty() ){
            rsp.sendError(HttpServletResponse.SC_FORBIDDEN, "[doFilter] You forget to set validated ip in web.xml.");
            return;
        }
        
        if( !validateIpList.contains(remoteAddr) ) {
            logger.info("remoteAddr="+remoteAddr+" try to access "+((HttpServletRequest)request).getRequestURI());
            rsp.sendError(HttpServletResponse.SC_FORBIDDEN, "[doFilter] You are not authorized to access this resource.");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
    }
    
}
