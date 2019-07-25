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
    // server properties or jndi properties
    public final static String PN_CAS_PREFIX = "casServerUrlPrefix";
    public final static String PN_CAS_LOGIN = "casServerLoginUrl";
    //public final static String PN_CAS_JAAS_CONF = "jaasConfig";
//    public final static String PN_GROUP_QUERY = "groupsQuery";
    public final static String PN_CALLER_QUERY = "member.callerQuery";
    public final static String PN_GROUP_QUERY = "member.groupsQuery";
    //public final static String PN_DEF_GROUP = "defaultGroup";
    public final static String PN_SERVER_NAME = "serverName";
    // default values
    public final static String DEF_CAS_PREFIX = System.getProperty("com.taiwancement.sso.serverUrlPrefix");
    public final static String DEF_CAS_LOGIN = System.getProperty("com.taiwancement.sso.loginUrl");
    //public final static String DEF_CAS_JAAS_CONF = "casRealm";
    public final static String DEF_CALLER_QUERY_SQL = "SELECT PASSWORD FROM ET_MEMBER WHERE LOGIN_ACCOUNT=?";
    public final static String DEF_GROUP_QUERY_SQL = "SELECT CODE from TC_GROUP G, TC_USERGROUP UG, TC_USER U WHERE G.ID=UG.GROUP_ID AND UG.USER_ID=U.ID AND U.LOGIN_ACCOUNT=?";
    //public final static String DEF_GROUP = "Authenticated";
    // session attribute names
    public final static String CALLER_ATTR = "_SC_CALLER_";
    public final static String GROUPS_ATTR = "_SC_GROUPS_";
}
