/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.sksp.controller.util.legacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Jason.Yu
 */
public class DatabaseUtil {
    
    private final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());
    public Object lookup(String jndiName)throws Exception{
        Object object=null;
        try {
            Context initialContext = new InitialContext();
            if (initialContext == null) {
                logger.log(Level.SEVERE, "JNDI problem. Cannot get InitialContext.");
            }
            object = initialContext.lookup(jndiName);
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "Cannot get jndiname: " + jndiName +";error:"+ ex);
            throw ex;
        }
        return object;
    }
    public Connection getConnection(String jndiName, String dbUser, String dbPassword)
            throws Exception {
        Connection result = null;
        try {
            DataSource datasource = (DataSource) lookup(jndiName);
            if (datasource != null) {
                if (dbUser != null && dbPassword != null) {
                    result = datasource.getConnection(dbUser, dbPassword);
                } else {
                    result = datasource.getConnection();
                }
            } else {
                logger.log(Level.SEVERE, "Failed to lookup datasource.");
            }
            return result;
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "Cannot get connection: " + ex);
            throw ex;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Cannot get connection: " + ex);
            throw ex;
        }
    }

    public void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception ex) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }
}
