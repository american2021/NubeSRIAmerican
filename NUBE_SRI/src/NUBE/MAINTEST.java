/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 *
 * @author David-Administrador
 */
public class MAINTEST {
    //\\192.168.0.160\InstaladorAmerican\Batch\Instituto\GENERADOS
    
    public static void main(String[] args) {
        
        File f = new File("\\\\HOME\\InstaladorAmerican\\Batch\\Instituto\\GENERADOS");
        File[] files = f.listFiles();
        System.out.println(f.isDirectory());
        f = new File("C:\\Users\\Administrador\\Documents\\FacturacionElectronicaAC\\NUBE_SRI\\src\\jasper\\rideFactura2.jrxml");
        System.out.println(f.isFile());
        try { 
            JasperCompileManager.compileReportToFile("C:\\Users\\Administrador\\Documents\\FacturacionElectronicaAC\\NUBE_SRI\\src\\jasper\\factura.jrxml");
        } catch (JRException ex) {
            Logger.getLogger(MAINTEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
