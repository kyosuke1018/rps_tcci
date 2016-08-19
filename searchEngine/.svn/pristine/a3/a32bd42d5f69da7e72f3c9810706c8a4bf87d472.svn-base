package com.tcci.solr.server.filter;

import java.io.IOException;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 限制 IP 存取 Filter
 * @author Peter
 */
public class IpRestrictFilter implements Filter {
    private static Log logger = LogFactory.getLog(IpRestrictFilter.class);

    private static String VALIDATE_IP_PARAM = "ip";
    private static String DELIMITER = ",";

    private List<String> validateIpList;
            
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 至 web.xml 取的合法 IP
        String ips =  filterConfig.getInitParameter(VALIDATE_IP_PARAM);
        if( ips!=null ){
            String[] validateIPs = ips.split(DELIMITER);
            if( validateIPs!=null ){
               validateIpList = new ArrayList<String>();
               for (String ip : validateIPs) {
                   if( !ip.trim().isEmpty() ){
                       validateIpList.add(ip.trim());
                       logger.info("IpRestrictFilter : validate IP = "+ip.trim());
                   }
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
