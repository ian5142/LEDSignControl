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
    String user = "javauser";
    String password = "admin";
    boolean hasNext = false;
    
    public CheckDB () {
        
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM t");
            for(int i = 1; rs.next(); i++) {
                System.out.println(rs.getString(i));
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

