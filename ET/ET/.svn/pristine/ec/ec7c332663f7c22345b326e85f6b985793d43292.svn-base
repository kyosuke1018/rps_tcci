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
import javax.inject.Named;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Named
public class AuthFacade {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    //@Resource(lookup="jdbc/testDB")// 包JAR檔不適用
    private DataSource dataSource;
    //@Resource(mappedName = "jndi/csrcCB.config")// 包JAR檔不適用
    private Properties jndiConfig;

    @PostConstruct
    public void init() {
        LOG.debug("AuthFacade init ...");
        dataSource = SecurityUtils.getDataSource(SecurityConfig.getJdbcNmae());
        jndiConfig = SecurityUtils.getJndiProperties(SecurityConfig.getJndiName());
    }
    
    //<editor-fold defaultstate="collapsed" desc="for DatabaseIdentityStoreDefinition">
    public String getPasswordSQL() {
        return SecurityConstants.DEF_PWD_QUERY_SQL;
    }
    public String getGroupsSQL() {
        return SecurityConstants.DEF_GROUP_QUERY_SQL;
    }
    public String[] getDyna() {
        return new String[]{
            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", 
            "Pbkdf2PasswordHash.SaltSizeBytes=64"
        };
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for get user group info ">
    /**
     * 取得授權群組
     * @param caller
     * @return 
     */
    public Set<String> getCallerGroups(String caller) {
        LOG.debug("getCallerGroups ...");
        Set<String> result = Collections.emptySet();
        if( dataSource==null ){
            LOG.error("getCallerGroups dataSource={}, jndiConfig={}", dataSource, jndiConfig);
            return result;
        }
        
        try{
            result = findCallerGroups(dataSource, getGroupsQuery(), caller);
            if( result == null ) {
                result = Collections.emptySet();
            }
            LOG.info("getCallerGroups caller = "+caller+", result = "+result.size());
        }catch(NamingException e){
            LOG.error("getCallerGroups NamingException:\n", e);
        }
        return result;
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
            List<String> groups = new ArrayList<String>();
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
    //</editor-fold>
}
