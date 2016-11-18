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
     * Creates a CheckDB object
     * Assigns values to url, user, and password.
     */
    public CheckDB () {
        url = "jdbc:mysql://localhost:3306/messagedb";
        user = "javauser";
        password = "admin";
    }
    /**
     * Opens the database
     * @return Returns true if the database opened successfully.
     */
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
    /**
     * Closes the database
     * @return Returns true if the database closed successfully.
     */
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
    /**
     * Selects the message variable from the database
     * @return Returns the message in String format
     */
    protected String selectMessage () {
        message = "";
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
    /**
     * Selects the isUpdated variable from the database
     * @return Returns true if isUpdated is true in the table.
     */
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
    /**
     * Selects the scrollON variable from the database
     * @return Returns true if scrollON is true
     */
    protected boolean selectscrollON () {
        boolean scrollON = false;
        try {
            if (openDB()) {
                rs = st.executeQuery("SELECT scrollon FROM t");
                for (int i = 1; rs.next(); i++) {
                    scrollON = rs.getBoolean(i);
                }
                boolean closeDB = closeDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scrollON;
    }
    /**
     * Sets isUpdated to true.
     */
    protected void setisUpdated () {
        try {
            if (openDB()) {
//                rs.updateBoolean("isupdated", true);
//                String [] s = new String [1];
//                s[0] = "isupdated";
//                updated.setString(1, "TRUE");
                updated.executeUpdate();
//                st.executeUpdate("t", s);
                boolean closeDB = closeDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

