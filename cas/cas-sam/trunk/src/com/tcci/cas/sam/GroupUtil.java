/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cas.sam;

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
public class GroupUtil {

    private static final Logger logger = Logger.getLogger(GroupUtil.class.getName());

    public static Connection getConnection(String jndiName, String dbUser, String dbPassword)
            throws Exception {
        Connection result = null;
        try {
            Context initialContext = new InitialContext();
//            if (initialContext == null) { // findbugs: RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE
//                logger.log(Level.SEVERE, "JNDI problem. Cannot get InitialContext.");
//            }
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

    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
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

    public static String[] getGroups(String jndiName, String dbUser, String dbPassword,String groupQuery, String userid,String defaultGroup) throws Exception {
        Connection connection = getConnection(jndiName,  dbUser,  dbPassword);
        return findGroup(connection, groupQuery,  userid, defaultGroup);
    }
    public static String[] findGroup(Connection connection, String groupQuery, String userid,String defaultGroup) throws Exception {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String as1[] = null;
        try {
            statement = connection.prepareStatement(groupQuery);
            statement.setString(1, userid);
            rs = statement.executeQuery();
            List groups = new ArrayList();
            groups.add(defaultGroup);
            if( rs != null)
            for (; rs.next(); ){
                String groupCode = rs.getString(1);
                groups.add(groupCode);
                logger.info("findGroup groupCode=" + groupCode);
            }
            String groupArray[] = new String[groups.size()];
            as1 = (String[]) groups.toArray(groupArray);
        } finally {
            close(connection, statement, rs);
        }
        return as1;
    }
}
