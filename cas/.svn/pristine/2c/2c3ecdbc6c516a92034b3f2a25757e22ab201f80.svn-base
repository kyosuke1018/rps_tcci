/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

/**
 *
 * @author Peter.pan
 */
public class SecurityConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final int REMEMBERME_VALIDITY_SECONDS = 24 * 60 * 60; //24 hours
    
    // server properties or jndi properties
    public final static String PN_CAS_PREFIX = "casServerUrlPrefix";
    public final static String PN_CAS_LOGIN = "casServerLoginUrl";
    //public final static String PN_CAS_JAAS_CONF = "jaasConfig";
    public final static String PN_GROUP_QUERY = "groupsQuery";
    //public final static String PN_DEF_GROUP = "defaultGroup";
    public final static String PN_SERVER_NAME = "serverName";
    // default values
    public final static String DEF_CAS_PREFIX = "http://tcci-ap-qas.taiwancement.com/cas-server";
    public final static String DEF_CAS_LOGIN = "http://tcci-ap-qas.taiwancement.com/cas-server/login";
    //public final static String DEF_CAS_JAAS_CONF = "casRealm";
    public final static String DEF_GROUP_QUERY_SQL = "SELECT CODE from TC_GROUP G, TC_USERGROUP UG, TC_USER U WHERE G.ID=UG.GROUP_ID AND UG.USER_ID=U.ID AND U.ACCOUNT=?";
    // use when own password in db
    public final static String DEF_PWD_QUERY_SQL = "SELECT PASSWORD FROM TC_USER WHERE ACCOUNT = ?";
    //public final static String DEF_GROUP = "Authenticated";
    // session attribute names
    public final static String CALLER_ATTR = "_SC_CALLER_";
    public final static String GROUPS_ATTR = "_SC_GROUPS_";
    public final static String CALLER_ORI_ATTR = "_SC_CALLER_ORI_";

}
