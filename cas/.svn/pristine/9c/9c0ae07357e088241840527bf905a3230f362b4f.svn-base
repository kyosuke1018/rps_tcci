/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcc.sso.authentication;

import com.tcc.sso.model.ejb.CasUserFacade;
import com.tcc.sso.model.entity.CasUser;
import java.util.Hashtable;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 *
 * @author Jason.Yu
 */
public class TCCSsoAuthenticationHandler implements AuthenticationHandler {
    private  Logger logger = LoggerFactory.getLogger( this.getClass());

    //@EJB CasUserFacade casUserFacade;

    @Override
    public boolean authenticate(Credentials credentials) throws AuthenticationException {
        boolean ret = false;

        UsernamePasswordCredentials upCred = (UsernamePasswordCredentials) credentials;
        String netId = upCred.getUsername();
        String password = upCred.getPassword();
        //logger.info("authenticate netId=" + netId +",password=" + password );
        //System.out.println("authenticate netId=" + netId +",password=" + password );
        ret = loginAD( netId,  password);
        if( !ret ){
            ret = loginDB(netId,  password);
        }
        return ret;
    }
    public boolean loginDB(String userName,String password){
        boolean isLogin = false;
        try{
            CasUserFacade casUserFacade = new CasUserFacade();
            CasUser casUser = casUserFacade.getUserByUserId(userName);
            if(casUser != null ){
                if( password.equals(casUser.getPassword()) && casUser.getActivation() )
                    isLogin = true;
            }
        }catch(Exception e){
            //logger.error("DB Login Failed! " + e.getMessage());
            logger.error("DB Login Failed!", e);
            //e.printStackTrace();
        }
        logger.info("loginDB isLogin=" + isLogin );
        //System.out.println("loginDB isLogin=" + isLogin );
        return isLogin;
    }
    public boolean loginAD(String userName,String password){
        boolean isLogin = false;
	try{
            Hashtable env = new Hashtable();

            String sp = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

            String ldapUrl = "ldap://192.168.15.3:389/";
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            env.put(Context.SECURITY_PRINCIPAL, userName + "@taiwancement.com");
            env.put(Context.SECURITY_CREDENTIALS, password);

            DirContext dctx = new InitialDirContext(env);
            //System.out.println("Got it!");
            isLogin = true;
            dctx.close();
	}catch(PartialResultException r){
             logger.info("LDAP PartialResultException");
             //isLogin = true;
        }catch(NamingException nex){
            logger.info("LDAP Connection: FAILED");
            //logger.error(userName, nex)nex.printStackTrace();
	}
        logger.info("loginAD isLogin=" + isLogin );
        //System.out.println("loginAD isLogin=" + isLogin );
	return isLogin;
    }
    public boolean loginAD_old(String userName,String password){
        boolean isLogin = false;
        //LdapContext ctx = null;
	try{
            Hashtable env = new Hashtable();

            String sp = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

            String ldapUrl = "ldap://192.168.15.3:389/";
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            env.put(Context.SECURITY_PRINCIPAL, "tccicommon@taiwancement.com");
            env.put(Context.SECURITY_CREDENTIALS, "Tcci@7000");

            DirContext dctx = new InitialDirContext(env);

            String base = "DC=Taiwancement,DC=com";

            SearchControls sc = new SearchControls();
            String[] attributeFilter = { "sAMAccountName", "mail" };
            sc.setReturningAttributes(attributeFilter);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            Object[] searchArguments = new Object[]{userName};

            String filter = "(&(objectClass=user)(sAMAccountName={0}))";

            NamingEnumeration results = dctx.search(base, filter, searchArguments , sc);
            if (results.hasMore()) {
              //SearchResult sr = (SearchResult) results.next();
              System.out.println("Got it!");
              isLogin = true;
            }
            dctx.close();
	}catch(PartialResultException r){
             System.out.println("LDAP PartialResultException");
             //isLogin = true;
        }catch(NamingException nex){
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
	}
        System.out.println("loginAD isLogin=" + isLogin );
	return isLogin;
    }

    @Override
    public boolean supports(Credentials credentials) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

}
