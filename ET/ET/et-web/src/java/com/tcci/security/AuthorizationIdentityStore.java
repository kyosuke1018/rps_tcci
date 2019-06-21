/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import javax.sql.DataSource;
import org.glassfish.soteria.identitystores.IdentityStoreConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
//public class AuthorizationIdentityStore implements IdentityStore {
public class AuthorizationIdentityStore {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    //@Resource(lookup="jdbc/testDB")// 包JAR檔不適用
    private DataSource dataSource;
    //@Resource(mappedName = "jndi/JavaEESecSSO.config")// 包JAR檔不適用
    private Properties jndiConfig;
    

    @PostConstruct
    public void init() {
        LOG.debug("init ...");
        dataSource = SecurityUtils.getDataSource(SecurityConfig.getJdbcNmae());
        jndiConfig = SecurityUtils.getJndiProperties(SecurityConfig.getJndiName());
    }
    
    
    private List<String> executeQuery(DataSource dataSource, String query, String parameter) {
        List<String> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, parameter);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(resultSet.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new IdentityStoreConfigurationException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * 取得授權群組
     * @param validationResult
     * @return 
     */
//    @Override
//    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
//        LOG.debug("getCallerGroups ...");
//        Set<String> result = new HashSet<>(); 
//        result.add("Authenticated");
//        if( dataSource==null ){
//            LOG.error("getCallerGroups dataSource={}, jndiConfig={}", dataSource, jndiConfig);
//            return result;
//        }
//        
//        try{
//            String caller = validationResult.getCallerPrincipal().getName();
//            result.addAll(findCallerGroups(dataSource, getGroupsQuery(), caller));
//            LOG.info("getCallerGroups caller = "+caller+", result = "+result.size());
//        }catch(NamingException e){
//            LOG.error("getCallerGroups NamingException:\n", e);
//        }
//        return result;
//    }

//    @Override
    public Set<IdentityStore.ValidationType> validationTypes() {
        return Collections.singleton(PROVIDE_GROUPS);
    }
    
    /**
     * 群組查詢 SQL (JNDI 無設定，則抓 server property)
     * @return
     * @throws NamingException 
     */
    public String getGroupsQuery() throws NamingException{
        String sql = SecurityUtils.getFromSysProps(SecurityConstants.PN_GROUP_QUERY, SecurityConstants.DEF_GROUP_QUERY_SQL);
        sql = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_GROUP_QUERY, sql);
        return sql;
    }
    
    /**
     * DB 抓取授權群組
     * @param dataSource
     * @param query
     * @param caller
     * @return 
     */
    private Set<String> findCallerGroups(DataSource dataSource, String query, String caller) {
        LOG.info("findCallerGroups query = "+query+", caller = "+caller);
        try (Connection connection = dataSource.getConnection()) {
            List<String> groups = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, caller);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    LOG.info("findCallerGroups rs = "+rs.getString(1));
                    groups.add(rs.getString(1));
                }
            }
            return new HashSet<>(groups);
        } catch (SQLException e) {
           LOG.error("findCallerGroups exception:\n", e);
        }
        return Collections.emptySet();
    }
    
}
