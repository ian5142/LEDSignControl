/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates CheckDB object, opens a connection to the messagedb database on localhost
 * Contains methods to select and update the database
 * @author Ian Van Schaick
 */
public class CheckDB {
    private Connection con;
    private Statement st;
    private ResultSet rs;

    private final String url;
    private final String user;
    private final String password;
    
    private String message;
    
    private PreparedStatement updated;
    private PreparedStatement newMessage;
    
    /**
     * 
     */
    public CheckDB () {
        url = "jdbc:mysql://localhost:3306/messagedb";
        user = "javauser";
        password = "admin";
        
        
    }
    
    private boolean openDB () {
        boolean DBopen = false;
        try {
            message = "This is a new message.";
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            updated = con.prepareStatement("UPDATE t SET isupdated=TRUE");
            newMessage = con.prepareStatement("UPDATE t SET message='" + message + "'");
            DBopen = true;
//            To be put in the update methods.
//            newMessage.executeUpdate();
//            newMessage.close();
//            updated.executeUpdate();
//            updated.close();
        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DBopen;
    }
    
    private boolean closeDB () {
        boolean DBclose = false;
        try {
            if (rs != null) {
                rs.close();
            }

            if (st != null) {
                st.close();
            }

            if (con != null) {
                con.close();
            }
            DBclose = true;
        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DBclose;
    }
    
    protected String selectMessage () {
        String message = "";
        try {
            if (openDB()) {
                rs = st.executeQuery("SELECT message FROM t");
                for (int i = 1; rs.next(); i++) {
                    message = rs.getString(i);
                }
                boolean closeDB = closeDB();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);

        }
        return message;
    }
    protected boolean selectisUpdated () {
        boolean isUpdated = false;
        try {
            if (openDB()) {
                rs = st.executeQuery("SELECT isupdated FROM t");
                for (int i = 1; rs.next(); i++) {
                    isUpdated = rs.getBoolean(i);
                }
                boolean closeDB = closeDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isUpdated;
    }
    
}

