/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.rs;

import com.tcci.security.test.ResVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class AbstractREST {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @Inject
    private SecurityContext context;

    public ResVO checkSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        
        ResVO resVO = new ResVO();
        if( session!=null ){
            if( session.getAttribute("loginAccount")!=null ){
                String loginAccount = (String)session.getAttribute("loginAccount");
                resVO.setLoginAccount(loginAccount);
            }
            
            if( session.getAttribute("groups")!=null ){
                List<String> groups = new ArrayList<>((Set<String>)session.getAttribute("groups"));
                resVO.setGroups(groups);
            }
        }
        
        if( context!=null && context.getCallerPrincipal()!=null ){
            String caller = context.getCallerPrincipal().getName();
            resVO.setCaller(caller);
        }
        return resVO;
    }
    
}
