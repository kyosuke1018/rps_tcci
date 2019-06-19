/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.faces.bean.ApplicationScoped;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Startup
@ApplicationScoped
public class ApplicationScopeModel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Resource(lookup="jdbc/ec")
    private DataSource dataSource;
    /*
    int iterationCount = 100;
    int keySize = 128;
    String salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    String iv = "F27D5C9927726BCEFE7510B1BDD3D137";
    String passphrase = "the quick brown fox jumps over the lazy dog";
    */
    // for cilent password encode
    private Map<Integer, Integer> encIteraMap = new HashMap<Integer, Integer>();// iterationCount
    private Map<Integer, Integer> encKeySizeMap = new HashMap<Integer, Integer>();// keySize
    private Map<Integer, String> encSaltMap = new HashMap<Integer, String>();// salt
    private Map<Integer, String> encIvMap = new HashMap<Integer, String>();// iv
    private Map<Integer, String> encPhraseMap = new HashMap<Integer, String>();// passphrase

    @PostConstruct
    public void afterCreate() {
        logger.debug("ApplicationScopeModel afterCreate ...");
        if( countRow()<=0 ){
            addEncryptParams();
        }
    }
    
    private int countRow(){
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) num from ec_encrypt");
        int count = executeCount(dataSource, sql.toString());
        return count;
    }
    
    private void clearEncryptParams(){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ec_encrypt where createtime<sysdate-1/24;");
    }
    
    private void addEncryptParams(){
        int[] iterationCountAry = new int[]{50, 100, 150, 200};
        int[] keySizeAry = new int[]{64, 128, 256};
        String[] passphraseAry = new String[]{
                                "Set server mode",
                                "Fine tune Heap size",
                                "Increase PermGen max size",
                                "Disable Secure Client-Initiated Renegotiation",
                                "Enable Isolated Class loading",
                                "Fine-Tune Garbage Collection",
                                "Disable Development Features",
                                "EJB Pool Settings",
                                "HTTP and Network Configuration",
                                "Acceptor and Request Threads",
                                "JDBC Pool Configuration"
                                };
        // 產生多組
        for(int i=0; i<GlobalConstant.ENCRYPT_NUM; i++){
            long id = i+1;
            int iterationCount = iterationCountAry[i % iterationCountAry.length];
            int keySize = keySizeAry[i % keySizeAry.length];
            String passphrase = passphraseAry[i % passphraseAry.length];
            
            String uuid = UUID.randomUUID().toString();
            String[] tmpAry = uuid.split("-");
            String[] timeAry = new String[]{
                                    Long.toOctalString(System.currentTimeMillis()),
                                    Long.toHexString(System.currentTimeMillis()),
                                    Long.toBinaryString(System.currentTimeMillis()),
                                    Long.toString(System.currentTimeMillis())
                                };

            String iv = tmpAry[i % tmpAry.length] + timeAry[i % timeAry.length];
            String salt = timeAry[i*2 % timeAry.length] + uuid + tmpAry[i*2 % tmpAry.length] + timeAry[i*3 % timeAry.length];

            StringBuilder sql = new StringBuilder();
            sql.append("insert into ec_encrypt (id, iteration_count, key_size, salt, iv, pass_phrase, createtime) \n");
            sql.append("values (").append(id).append(", ").append(iterationCount).append(", ").append(keySize)
               .append(", '").append(salt).append("', '").append(iv).append("', '").append(passphrase).append("', sysdate);");
            
            executeUpdate(dataSource, sql.toString());
        }
    }

    private int executeCount(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery(query);
                return rs.getInt(1);
            }
        } catch (SQLException e) {
           logger.error("executeCount error SQLException = ", e.toString());
        }
        return 0;
    }
    
    private void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
           logger.error("executeUpdate error SQLException = ", e.toString());
        }
    }

    public Map<Integer, Integer> getEncIteraMap() {
        return encIteraMap;
    }

    public void setEncIteraMap(Map<Integer, Integer> encIteraMap) {
        this.encIteraMap = encIteraMap;
    }

    public Map<Integer, Integer> getEncKeySizeMap() {
        return encKeySizeMap;
    }

    public void setEncKeySizeMap(Map<Integer, Integer> encKeySizeMap) {
        this.encKeySizeMap = encKeySizeMap;
    }

    public Map<Integer, String> getEncSaltMap() {
        return encSaltMap;
    }

    public void setEncSaltMap(Map<Integer, String> encSaltMap) {
        this.encSaltMap = encSaltMap;
    }

    public Map<Integer, String> getEncIvMap() {
        return encIvMap;
    }

    public void setEncIvMap(Map<Integer, String> encIvMap) {
        this.encIvMap = encIvMap;
    }

    public Map<Integer, String> getEncPhraseMap() {
        return encPhraseMap;
    }

    public void setEncPhraseMap(Map<Integer, String> encPhraseMap) {
        this.encPhraseMap = encPhraseMap;
    }
   
}
