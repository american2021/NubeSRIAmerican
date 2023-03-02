/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

//import servicios.Autorizacion;

import conexion.datosConsulta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servicios.enviarAutorizacion;
import servicios.enviarRecepcion;


/**
 *
 * @author Jaime
 */
public class pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String query = "select SecNumero, SecNumero from vfia..SecuenciaSRI WHERE SecTipo='VTA'";
//        System.out.println(query);
        datosConsulta dcons = new datosConsulta();
        List<String> secuencias = dcons.buscador(query, 2).get(0);
        System.out.println("secuencias:"+secuencias);
        
        String secAux = secuencias.get(0);
        String numAux = secuencias.get(1);
        
        secAux = secAux.substring(0,secAux.length()-3);
        secAux = secAux.replaceAll(",", "");
        
        numAux = numAux.substring(0,numAux.length()-3);
        numAux = numAux.replaceAll(",", "");
        
        int secuencial = Integer.parseInt(secAux);
        int codigoNumerico = Integer.parseInt(numAux);
        
        System.out.println("secuencial:"+secuencial+"  codigoNumerico:"+codigoNumerico);
//        String path = "\\\\192.168.1.250\\InstaladorAmerican\\Batch\\Instituto";
//        String pathWeb = "\\\\192.168.1.2\\sricomprobantes\\DOCS\\Instituto\\XML";
//        String clave = "0301201907019042651300120011000000000010000000118";
////        generarPDF g = new generarPDF();
////        g.copiar(path+"\\"+clave+".xml", pathWeb+"\\");
//        
////        enviarRecepcion r = new enviarRecepcion();
////        String respuesta = r.recepcionDocumento(path+"\\FIRMADOS\\0301201907019042651300110011000000000030000000315_sign.xml",1);
//        
////        System.out.println(enviarAutorizacion.autorizacionFactura("1912201801019042651300110011000000000240000002411",1));
//
////        enviarRecepcion r = new enviarRecepcion();
////        System.out.println(r.recepcionDocumento("C:\\Users\\Jaime\\Desktop\\AMERICAN COLLAGE\\Batch\\Instituto\\FIRMADOS\\"
////                + "1912201801019042651300110011000000000010000000111"
////                + "_sign.xml",1));
//        generarPDF pdf = new generarPDF();
//        pdf.generar(clave, path, "vfIA", "geIA", "2687", "RET", "\\\\192.168.1.250\\InstaladorAmerican\\Batch\\", "", "Instituto");
//
////        Thread.currentThread();
////        try {
////            Thread.sleep(15000L);
////        } catch (InterruptedException ex) {
////            Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
////        }
////
////        enviarCorreo enviarcorreo = new enviarCorreo();
////        enviarcorreo.enviarCorreo(path+"\\AUTORIZADOS\\", clave, "amigosjaimeleon@hotmail.com");
    }
    
}
