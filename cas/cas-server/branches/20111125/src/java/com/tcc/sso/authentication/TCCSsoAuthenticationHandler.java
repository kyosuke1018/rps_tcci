/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcc.sso.authentication;

import com.tcc.sso.model.ejb.CasUserFacade;
import com.tcci.cas.entity.CasUser;
import com.tcci.cas.util.PasswordUtil;
import java.math.BigDecimal;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author Jason.Yu
 */
public class TCCSsoAuthenticationHandler implements AuthenticationHandler , InitializingBean {
    private  Logger logger = LoggerFactory.getLogger( this.getClass());

    @NotNull
    private String ldapURL;
    @NotNull
    private String securityAuthentication;
    @NotNull
    private String suffix;
    @NotNull
    private String dataSourceJndi;

    private String dbUser;
    private String dbPassword;
    //@NotNull
    private String sqlStatement;
    //@EJB CasUserFacade casUserFacade;

    @Override
    public boolean authenticate(Credentials credentials) throws AuthenticationException {
        boolean ret = false;

        UsernamePasswordCredentials upCred = (UsernamePasswordCredentials) credentials;
        upCred.setUsername(upCred.getUsername().toLowerCase());
        String netId = upCred.getUsername();
        String password = upCred.getPassword();


        ret = loginAD( netId,  password);
        if( !ret ){
            ret = loginDB(netId,  password);
        }
        return ret;
    }
    public boolean loginDB(String userName, String password){
        boolean isLogin = false;
        try{
            Object result[] = DatabaseUtil.queryUserPassword(this.getDataSourceJndi(), dbUser, dbPassword, sqlStatement, userName);

            if( result != null){
                String dbPassword = (String)result[1];
                PasswordUtil passwordUtil = new PasswordUtil();
                String encryptPwd = passwordUtil.encrypt(password);

                BigDecimal activation = (BigDecimal)result[2];
                if( encryptPwd.equals(dbPassword) ){
                    if( BigDecimal.ONE.equals( activation ) ){
                        isLogin = true;
                    }
                }
            }
        }catch(Exception e){
            logger.error("DB Login Failed!", e);
            e.printStackTrace();
        }
        logger.info("loginDB isLogin=" + isLogin );
        return isLogin;
    }
    public boolean loginDB_old(String userName,String password){
        boolean isLogin = false;
        try{
            CasUserFacade casUserFacade = new CasUserFacade();
            CasUser casUser = casUserFacade.getUserByUserId(userName);
            if(casUser != null ){

                if( password.equals(casUser.getPassword()) && casUser.getActivation() )
                    isLogin = true;
            }
        }catch(Exception e){
            logger.error("DB Login Failed!", e);
            e.printStackTrace();
        }
        logger.info("loginDB isLogin=" + isLogin );
        return isLogin;
    }
    public boolean loginAD(String userName,String password){
        boolean isLogin = false;
	try{
            Hashtable env = new Hashtable();

            String sp = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

            //String ldapUrl = "ldap://192.168.15.3:389/";
            //env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            //env.put(Context.SECURITY_PRINCIPAL, userName + "@taiwancement.com");

            env.put(Context.PROVIDER_URL, ldapURL);
            env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
            env.put(Context.SECURITY_PRINCIPAL, userName + suffix );
            env.put(Context.SECURITY_CREDENTIALS, password);

            DirContext dctx = new InitialDirContext(env);
            isLogin = true;
            dctx.close();
	}catch(PartialResultException r){
             logger.error("LDAP PartialResultException");
        }catch(NamingException nex){
            nex.printStackTrace();
            logger.error("LDAP Connection: FAILED");
	}
        logger.info("loginAD isLogin=" + isLogin );
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
              logger.info("Got it!");
              isLogin = true;
            }
            dctx.close();
	}catch(PartialResultException r){
             logger.error("LDAP PartialResultException");
             //isLogin = true;
        }catch(NamingException nex){
            logger.error("LDAP Connection: FAILED");
            nex.printStackTrace();
	}
        logger.info("loginAD isLogin=" + isLogin );
	return isLogin;
    }

    @Override
    public boolean supports(Credentials credentials) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    public String getDataSourceJndi() {
        return dataSourceJndi;
    }

    public void setDataSourceJndi(String dataSourceJndi) {
        this.dataSourceJndi = dataSourceJndi;
    }

    public String getLdapURL() {
        return ldapURL;
    }

    public void setLdapURL(String ldapURL) {
        this.ldapURL = ldapURL;
    }

    public String getSecurityAuthentication() {
        return securityAuthentication;
    }

    public void setSecurityAuthentication(String securityAuthentication) {
        this.securityAuthentication = securityAuthentication;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
        //maybe
        //this.setSqlPasword("select userid,password,activation from cas_user where userid=?");
    }

}
