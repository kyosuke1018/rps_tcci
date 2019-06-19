package com.tcci.security;

import java.util.Properties;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
//import javax.security.enterprise.identitystore.AESPasswordHash;

@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "${'jdbc/et'}",
    callerQuery = "${databaseIdentityConfig.callerQuery}",
    groupsQuery = "${databaseIdentityConfig.groupsQuery}",
    priorityExpression = "#{100}",
    hashAlgorithm = AESPasswordHash.class//20180423 sha256Hex
//    hashAlgorithm = Pbkdf2PasswordHash.class,
//    hashAlgorithmParameters = {
//        "${appConfig.hashAlgorithmParameters}"
//    }
)
@ApplicationScoped
@Named
public class DatabaseIdentityConfig {
    
    @Resource(mappedName = "jndi/et.config")
    protected Properties jndiConfig;

    public String getCallerQuery(){
//        System.out.println("callerQuery ="+jndiConfig.getProperty("callerQuery"));
//        return jndiConfig.getProperty("callerQuery");
//        return "SELECT PASSWORD FROM TC_USER WHERE ACCOUNT=?";
        return "SELECT PASSWORD FROM EC_MEMBER WHERE LOGIN_ACCOUNT=?";
    }

    public String getGroupsQuery(){
//        System.out.println("groupsQuery ="+jndiConfig.getProperty("groupsQuery"));
//        return jndiConfig.getProperty("groupsQuery");
        return "SELECT CODE from TC_GROUP G, TC_USERGROUP UG, TC_USER U WHERE G.ID=UG.GROUP_ID AND UG.USER_ID=U.ID AND U.ACCOUNT=?";
//        return "SELECT CODE from TC_GROUP G, TC_USERGROUP UG, TC_USER U, EC_MEMBER M WHERE G.ID=UG.GROUP_ID AND UG.USER_ID=U.ID AND U.LOGIN_ACCOUNT = M.LOGIN_ACCOUNT AND U.LOGIN_ACCOUNT=?";
    }
    
    public String[] getHashAlgorithmParameters() {
        return new String[]{
            "Pbkdf2PasswordHash.Iterations=4096",
            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", 
            "Pbkdf2PasswordHash.SaltSizeBytes=64"
        };
//        return new String[]{
//            "Pbkdf2PasswordHash.Iterations=2048",
//            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA256", 
//            "Pbkdf2PasswordHash.KeySizeBytes=32",
//            "Pbkdf2PasswordHash.SaltSizeBytes=32"
//        };
    }

}
