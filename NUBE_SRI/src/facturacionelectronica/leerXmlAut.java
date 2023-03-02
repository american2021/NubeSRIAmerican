/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacionelectronica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime
 */
public class leerXmlAut {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileReader fr = null;
        BufferedReader br = null;
        String xml = "2705201901070076846800110011000000000500000005011.xml";
        File f = new File("C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\BELLA_MODA\\AUTORIZADOS\\"+xml);
        try{    
            fr = new FileReader (f);
            br = new BufferedReader(fr);
            String linea, txt1="<fechaAutorizacion>", txt2="</fechaAutorizacion>", fecha = "";
            while((linea=br.readLine())!=null){
                if(linea.contains(txt1)){
                    int index1 = linea.indexOf(txt1);
                    int index2 = linea.indexOf(txt2);
                    fecha = linea.substring(index1+txt1.length(), index2);
                    System.out.println("fecha:"+fecha);
                }
            }
        }catch(IndexOutOfBoundsException | java.io.FileNotFoundException iout){
            System.out.println("Error en archivo txt");
        } catch (IOException ex) {
            Logger.getLogger(leerXmlAut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
