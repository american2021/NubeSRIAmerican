/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime
 */
public class conexion {
    Connection Con;
    public conexion(){
        try{
            
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Con = DriverManager.getConnection("jdbc:odbc:sqlFiliales","sa","daemon");
            
        }catch (ClassNotFoundException | SQLException ex) {
            
            System.out.println("Noconexion");
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
            
        }
//        try {
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            Con = DriverManager.getConnection("jdbc:odbc:sqlFiliales","sa","daemon");
//            
//        }   catch (ClassNotFoundException | SQLException ex) {
//            System.out.println("Noconexion");
//            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
////            Con = DriverManager.getConnection("jdbc:odbc:sqlFiliales;useUnicode=true;characterEncoding=UCS-2","sa","daemon");
//            Con = DriverManager.getConnection("jdbc:odbc:sqlFiliales","sa","daemon");
//            
//        }   catch (SQLException ex) {
//            System.out.println("Noconexion");
//            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public int CerrarConexion(){
        try{
            if(getCon()!=null){
                getCon().close();
            }
            return 0;
        }catch(Exception exc){
            System.out.println("Error al cerrar la conexion: "+" : "+exc);
            return -1;
        }
    }
    /**
     * @return the Conexion
     */
    public Connection getCon() {
        return Con;
    }

    public Connection getConexion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
