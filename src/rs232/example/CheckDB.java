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
 *
 * @author Ian Van Schaick
 */
public class CheckDB {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    String url = "jdbc:mysql://localhost:3306/messagedb";
    String user = "root";
    String password = "Eastwind3";
    boolean hasNext = false;
    
    public CheckDB () {
        
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
            hasNext = rs.next();
            if (hasNext) {
                System.out.println(rs.getString(1));
            }

            if (rs != null) {
                rs.close();
            }

            if (st != null) {
                st.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(CheckDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

