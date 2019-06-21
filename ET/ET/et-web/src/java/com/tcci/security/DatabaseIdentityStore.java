/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import com.tcci.et.enums.SysRoleEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.util.Collections.emptySet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import javax.sql.DataSource;
import org.glassfish.soteria.identitystores.IdentityStoreConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class DatabaseIdentityStore implements IdentityStore {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private DataSource dataSource;
    private Properties jndiConfig;
    
    private Set<ValidationType> validationTypes;
    private AESPasswordHashImpl hashAlgorithm; 
    
    @PostConstruct
    public void init() {
        logger.debug("DatabaseIdentityStore init ...");
        dataSource = SecurityUtils.getDataSource(SecurityConfig.getJdbcNmae());
        jndiConfig = SecurityUtils.getJndiProperties(SecurityConfig.getJndiName());
        
        validationTypes = new HashSet<>();
        validationTypes.add(VALIDATE);
        
//        hashAlgorithm.initialize(new HashMap<>());
        hashAlgorithm = new AESPasswordHashImpl();
    }
    
    
    /**
     * 驗證主程式
     * @param credential
     * @return 
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
//        logger.debug("validate ...");
        CredentialValidationResult result = NOT_VALIDATED_RESULT;// INVALID_RESULT
        try{
//            DatabaseCredential databaseCredential = (DatabaseCredential)credential;
//            String loginAccount = databaseCredential.getCaller();
//            String password = databaseCredential.getPassword();
//            boolean encrypted = databaseCredential.isEncrypted();
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential)credential;
            logger.info("validate ..."+usernamePasswordCredential.getCaller());
            logger.info("validate ..."+usernamePasswordCredential.getPassword());
            List<String> passwords = executeQuery(
                    dataSource,
                    this.callerQuery(),
                    usernamePasswordCredential.getCaller()
            );
            
            if (passwords.isEmpty()) {
                return INVALID_RESULT;
            }
            
            if (hashAlgorithm.verify(usernamePasswordCredential.getPassword().getValue(), passwords.get(0))) {
                Set<String> groups = new HashSet<>();
                groups.add(SysRoleEnum.MEMBER.getCode());
                groups.add("Authenticated");
                
//                Set<String> groupQuery = new HashSet<>(executeQuery(dataSource, this.groupsQuery(), usernamePasswordCredential.getCaller()));
//                Boolean admin = true;
//                Boolean admin = !groupQuery.isEmpty();
//                if(admin){
//                    groups.add("ADMINISTRATORS");
//                    groups.add(SysRoleEnum.ADMIN.getCode());
//                }
                
//                logger.info("validate groups..."+groups.size());
                return new CredentialValidationResult(new CallerPrincipal(usernamePasswordCredential.getCaller()), groups);
            }
            
        }catch(Exception e){
            logger.error("validate Exception :\n", e);
        }
//        logger.info("validate result ="+result.getStatus());
        return result;
    }
    
    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) throws NamingException {
        logger.info("validate ..."+usernamePasswordCredential.getCaller());
        logger.info("validate ..."+usernamePasswordCredential.getPassword());
//        logger.info("validate ..."+usernamePasswordCredential.getPassword().getValue());
//        DataSource dataSource = getDataSource();

        List<String> passwords = executeQuery(
            dataSource, 
            this.callerQuery(),
            usernamePasswordCredential.getCaller()
        );
        
        if (passwords.isEmpty()) {
            return INVALID_RESULT;
        }
        
        if (hashAlgorithm.verify(usernamePasswordCredential.getPassword().getValue(), passwords.get(0))) {
            Set<String> groups = emptySet();
            if (validationTypes.contains(ValidationType.PROVIDE_GROUPS)) {
                groups = new HashSet<>(executeQuery(dataSource, this.groupsQuery(), usernamePasswordCredential.getCaller()));
            }
            return new CredentialValidationResult(new CallerPrincipal(usernamePasswordCredential.getCaller()), groups);
        }

        return INVALID_RESULT;
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
     * 自定 IdentityStore 執行順序 - 預設 priority 為100，越小越先執行
     * @return 
     */
    @Override
    public int priority(){ 
         return 80;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        Set<ValidationType> types = new HashSet<>();
        types.add(VALIDATE);
        types.add(PROVIDE_GROUPS);
        
        return types;
    }

    /**
     * 群組查詢 SQL (JNDI 無設定，則抓 server property)
     * @return
     * @throws NamingException 
     */
    public String groupsQuery() throws NamingException{
        String sql = SecurityUtils.getFromSysProps(SecurityConstants.PN_GROUP_QUERY, SecurityConstants.DEF_GROUP_QUERY_SQL);
        sql = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_GROUP_QUERY, sql);
//        logger.debug("groupsQuery:"+sql);
        return sql;
    }
    
    public String callerQuery() throws NamingException{
        String sql = SecurityUtils.getFromSysProps(SecurityConstants.PN_CALLER_QUERY, SecurityConstants.DEF_GROUP_QUERY_SQL);
        sql = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_CALLER_QUERY, sql);
        return sql;
    }
}
