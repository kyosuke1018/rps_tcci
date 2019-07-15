/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcc.sso.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    
    private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

    private static Connection getConnection(String jndiName, String dbUser, String dbPassword)
            throws Exception {
        Connection result = null;
        try {
            Context initialContext = new InitialContext();
            if (initialContext == null) {
                logger.log(Level.SEVERE, "JNDI problem. Cannot get InitialContext.");
            }
            DataSource datasource = (DataSource) initialContext.lookup(jndiName);

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

    private static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
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

    public static Object[] queryUserPassword(String jndiName, String dbUser, String dbPassword, String sqlPassword ,String userid) throws Exception {
        Object result[] = null;
        // result[] = {"userid", password, activate};
        Connection connection = getConnection(jndiName,  dbUser,  dbPassword);
        PreparedStatement statement = null;
        ResultSet rs = null;
        String as1[] = null;
        try {
            statement = connection.prepareStatement(sqlPassword);
            statement.setString(1, userid);
            rs = statement.executeQuery();
            
            if( rs != null && rs.next() ){
                result = new Object[3];
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                result[2] = rs.getBigDecimal(3);
            }
        } finally {
            close(connection, statement, rs);
        }
        return result;
    }
}
