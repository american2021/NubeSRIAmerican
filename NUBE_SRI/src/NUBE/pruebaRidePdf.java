/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Jaime
 */
public class pruebaRidePdf {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String pathXml="C:\\Users\\Jaime\\Desktop\\AMERICAN COLLAGE\\1512201904019042651300110011000000000030000000310_sign.xml";
        String pathJasper="C:\\Users\\Jaime\\Documents\\NetBeansProjects\\NUBE_SRI\\src\\jasper\\rideNCredito.jasper";
//        String pathXml="C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\PILI\\ride.xml";
        String pathPdf="C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\PILI\\ride.pdf";
        String pathLogo="C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\PILI\\logo.jpg";
        
        // extrae fecha
        FileReader fr = null;
        BufferedReader br = null;
        String xml = "2705201901070076846800110011000000000500000005011.xml";
        File f = new File("C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\BELLA_MODA\\AUTORIZADOS\\"+xml);
        String fecha = "";
        try{    
            fr = new FileReader (f);
            br = new BufferedReader(fr);
            String linea, txt1="<fechaAutorizacion>", txt2="</fechaAutorizacion>";
            while((linea=br.readLine())!=null){
                if(linea.contains(txt1)){
                    int index1 = linea.indexOf(txt1);
                    int index2 = linea.indexOf(txt2);
                    fecha = linea.substring(index1+txt1.length(), index2);
//                    System.out.println("fecha:"+fecha);
                }
            }
        }catch(IndexOutOfBoundsException | java.io.FileNotFoundException iout){
            System.out.println("Error en archivo txt:"+iout);
        } catch (IOException ex) {
            System.out.println("Error en archivo txt:"+ex);
        }
        //fin extrae fecha
//        //extrae datos
//        String query = "SELECT "
//                    + "LEFT(FECHAAUTORIZACION,10)+' '+SUBSTRING(FECHAAUTORIZACION,12,8)"
//                    + ",NUMEROAUTORIZACION"
//                    + ",CASE WHEN PERFACTURAR='O' THEN (SELECT REPDIRECCION FROM "+ge+"..REPRESENTANTES E"
//                    + "				WHERE B.TIPECODIGO = E.TIPECODIGO AND B.PERCODIGO = E.PERCODIGO)"
//                    + "     ELSE PERDIRECCION END PERDIRECCION"
//                    + ",CFDIMPONIBLE"
//                    + ",CFDNOIMPONIBLE"
//                    + ",CONVERT(NUMERIC(5,2),0) NOOBJETOIVA"
//                    + ",CONVERT(NUMERIC(5,2),0) EXENTODEIVA"
//                    + ",CFDIMPONIBLE+CFDNOIMPONIBLE SUBTOTAL"
//                    + ",CFDDESIMPONIBLE+CFDDESNOIMPONIBLE DESCUENTO"
//                    + ",CONVERT(NUMERIC(5,2),0) ICE"
//                    + ",CFDIMPUESTO"
//                    + ",CONVERT(NUMERIC(5,2),0) IRBPRN"
//                    + ",CONVERT(NUMERIC(5,2),0) PROPINA"
//                    + ",IECABTOTAL"
//                    + ",(SELECT PROFCODIGO FROM "+ge+"..PERSONAS WHERE TIPECODIGO = 'A')"
//                    + ",(SELECT PEROBSERVACION FROM "+ge+"..PERSONAS WHERE TIPECODIGO = 'A')"
//                    + ",(SELECT ECCODIGO FROM "+ge+"..PERSONAS WHERE TIPECODIGO = 'A') "
//                    + "FROM "+vf+"..RELACIONSRI A, "+vf+"..IECABECERA B, "+vf+"..CABFACTDEV C, "+ge+"..PERSONAS D "
//                    + "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO "
//                    + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
//                    + "AND B.BODCODIGO = C.BODCODIGO AND B.TRINVTIPO = C.TRINVTIPO "
//                    + "AND B.IECABNUMERO = C.IECABNUMERO AND B.IECABTIPO = C.IECABTIPO "
//                    + "AND B.TIPECODIGO = D.TIPECODIGO AND B.PERCODIGO = D.PERCODIGO AND A.IECABNUMERO = "+num+" AND A.TRINVTIPO = '"+tip+"'";
//            System.out.println(query);
//            datosConsulta dcons = new datosConsulta();
//            List<List<String>> fcab1 = dcons.buscador(query, 17);
//        //fin extrae datos
        
        Map<String, Object> parametros = new HashMap<String, Object>();
        try {
            parametros.put("LOGO", new FileInputStream(pathLogo));
        } catch (FileNotFoundException ex) {
            parametros.put("LOGO", "");
        }
        parametros.put("FECHA_AUT",fecha);//"java.lang.String"/>
        parametros.put("CLI_MAIL","correo@correo.com");//"java.lang.String"/>
        parametros.put("SUB12","0.00");//"java.lang.String"/>
        parametros.put("SUB0","0.00");//"java.lang.String"/>
//        parametros.put("SUB_NO","0.00");//"java.lang.String"/>
//        parametros.put("SUB_EX","0.00");//"java.lang.String"/>
//        parametros.put("SUB_ICE","0.00");//"java.lang.String"/>
        parametros.put("SUB_IVA","0.00");//"java.lang.String"/>
//        parametros.put("SUB_IRB","0.00");//"java.lang.String"/>
//        parametros.put("SUB_PRO","0.00");//"java.lang.String"/>
        
        
//        String pathXml="";
        rideGenerador rideFac = new rideGenerador();
        if(rideFac.ejecutar(parametros, pathJasper, pathXml, pathPdf, pathLogo, "/notaCredito/detalles/detalle")){
//        if(rideFac.ejecutar(parametros, pathJasper, "", pathPdf, pathLogo, "")){
            System.out.println("ok");
        }
    }
}
