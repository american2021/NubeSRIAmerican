/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

import conexion.datosConsulta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class generarPDF {

    public void generar(String clave, String path, String vf, String ge, String num, String tip, String url, String pathWeb, String empresa, String ruc) {
        String pathXml = path + "\\GENERADOS\\" + clave + ".xml";
        String pathXmlAut = path + "\\AUTORIZADOS\\" + clave + ".xml";
        String pathPdf = path + "\\AUTORIZADOS\\RIDE\\" + clave + ".pdf";
        //pathPdf="C:\\"+clave+".pdf";
        String pathLogo = path + "\\LOGO.JPG";
        String ambiente = "PRUEBAS";
        if (clave.length() == 49) {
            if (clave.substring(23, 24).equals("2")) {
                ambiente = "PRODUCCION";
            }
        }
        System.out.println("el Ambiente sera " + ambiente);
        if (tip.equals("VTA")) {
            String pathJasperFac = path + "\\rideFactura.jrxml";
            System.out.println(path+"\\rideFactura.jrxml");
            /*String query = "SELECT "
                    + "LEFT(FECHAAUTORIZACION,10)+','+SUBSTRING(FECHAAUTORIZACION,12,8)"
                    + ",NUMEROAUTORIZACION"
                    + ",CASE WHEN PERFACTURAR='O' THEN (SELECT REPDIRECCION FROM " + ge + "..REPRESENTANTES E"
                    + "WHERE B.TIPECODIGO = E.TIPECODIGO AND B.PERCODIGO = E.PERCODIGO)"
                    + "ELSE PERDIRECCION END PERDIRECCION"
                    + ",CFDIMPONIBLE"
                    + ",CFDNOIMPONIBLE"
                    + ",CONVERT(NUMERIC(5,2),0) NOOBJETOIVA"
                    + ",CONVERT(NUMERIC(5,2),0) EXENTODEIVA"
                    + ",CFDIMPONIBLE+CFDNOIMPONIBLE SUBTOTAL"
                    + ",CFDDESIMPONIBLE+CFDDESNOIMPONIBLE DESCUENTO"
                    + ",CONVERT(NUMERIC(5,2),0) ICE"
                    + ",CFDIMPUESTO"
                    + ",CONVERT(NUMERIC(5,2),0) IRBPRN"
                    + ",CONVERT(NUMERIC(5,2),0) PROPINA"
                    + ",IECABTOTAL"
                    + ",(SELECT PROFCODIGO FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"' )"
                    + ",(SELECT PEROBSERVACION FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"' )"
                    + ",(SELECT ECCODIGO FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"') "
                    + "FROM " + vf + "..RELACIONSRI A, " + vf + "..IECABECERA B, " + vf + "..CABFACTDEV C, " + ge + "..PERSONAS D "
                    + "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO "
                    + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
                    + "AND B.BODCODIGO = C.BODCODIGO AND B.TRINVTIPO = C.TRINVTIPO "
                    + "AND B.IECABNUMERO = C.IECABNUMERO AND B.IECABTIPO = C.IECABTIPO "
                    + "AND B.TIPECODIGO = D.TIPECODIGO AND B.PERCODIGO = D.PERCODIGO AND A.IECABNUMERO = " + num + " AND A.TRINVTIPO = '" + tip + "'";*/
            
            String query = "SELECT LEFT(FECHAAUTORIZACION,10)+','+SUBSTRING(FECHAAUTORIZACION,12,8),NUMEROAUTORIZACION," +
                            "CASE WHEN PERFACTURAR='O' THEN (SELECT REPDIRECCION FROM geVA..REPRESENTANTES E\n" +
                            "WHERE B.TIPECODIGO = E.TIPECODIGO AND B.PERCODIGO = E.PERCODIGO)" +
                            "ELSE PERDIRECCION END " +
                            "PERDIRECCION,CFDIMPONIBLE,CFDNOIMPONIBLE,CONVERT(NUMERIC(5,2),0) NOOBJETOIVA,CONVERT(NUMERIC(5,2),0) EXENTODEIVA," +
                            "CFDIMPONIBLE+CFDNOIMPONIBLE SUBTOTAL,CFDDESIMPONIBLE+CFDDESNOIMPONIBLE DESCUENTO,\n" +
                            "CONVERT(NUMERIC(5,2),0) ICE,CFDIMPUESTO,CONVERT(NUMERIC(5,2),0) IRBPRN,CONVERT(NUMERIC(5,2),0) PROPINA,IECABTOTAL," +
                            "(SELECT PROFCODIGO FROM geVA..PERSONAS WHERE TIPECODIGO = 'A' AND CedulaRuc = '"+ruc+"')," +
                            "(SELECT PEROBSERVACION FROM geVA..PERSONAS WHERE TIPECODIGO = 'A' AND CedulaRuc = '"+ruc+"')," +
                            "(SELECT ECCODIGO FROM geVA..PERSONAS WHERE TIPECODIGO = 'A' AND CedulaRuc = '"+ruc+"')" +
                            "FROM vfVA..RELACIONSRI A, vfVA..IECABECERA B, vfVA..CABFACTDEV C, geVA..PERSONAS D " +
                            "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO " +
                            "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO " +
                            "AND B.BODCODIGO = C.BODCODIGO AND B.TRINVTIPO = C.TRINVTIPO " +
                            "AND B.IECABNUMERO = C.IECABNUMERO AND B.IECABTIPO = C.IECABTIPO " +
                            "AND B.TIPECODIGO = D.TIPECODIGO AND B.PERCODIGO = D.PERCODIGO AND A.IECABNUMERO = 1 AND A.TRINVTIPO = 'VTA'";
            System.out.println("------------------------");
            System.out.println(query);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> fcab1 = dcons.buscador(query, 17);
            List<String> fcab = new ArrayList<>();

            if (fcab1.size() > 0) {
                fcab = fcab1.get(0);
            }

            if (fcab.size() > 15) {
                Map<String, Object> parametros = new HashMap<>();

                parametros.put("NUM_AUT", fcab.get(1));
                System.out.println("NUM_AUT: " + fcab.get(1));
                parametros.put("FECHA_AUT", fcab.get(0));
                System.out.println("FECHA_AUT: " + fcab.get(0));
                parametros.put("TIPO_EMISION", "NORMAL");
                try {
                    parametros.put("LOGO", new FileInputStream(pathLogo));
                } catch (FileNotFoundException ex) {
                    parametros.put("LOGO", "");
                }
                //            System.out.println("amb:"+ambiente);
                parametros.put("CONT_ESPECIAL", fcab.get(14));
                parametros.put("LLEVA_CONTABILIDAD", fcab.get(16));
                parametros.put("DIRECCION_CLIENTE", fcab.get(2));
                parametros.put("GUIA", "");
                parametros.put("AMBIENTE", ambiente);
                parametros.put("NOM_COMERCIAL", fcab.get(15));
                parametros.put("SUB12", fcab.get(3));
                parametros.put("SUB0", fcab.get(4));
                parametros.put("SUB_NO", fcab.get(5));
                parametros.put("SUB_EX", fcab.get(6));
                //parametros.put("SUB", par.get(23));
                //parametros.put("SUB_DES", par.get(24));
                parametros.put("SUB_ICE", "0.00");
                parametros.put("SUB_IVA", fcab.get(10));
                parametros.put("SUB_IRB", "0.00");
                parametros.put("SUB_PRO", "0.00");

                if (empresa.equals("BillGates")) {

                    parametros.put("INFL1", "Régimen");

                } else {

                    parametros.put("INFL1", "Agente de Retención");

                }
                if (empresa.equals("Corel")) {

                    parametros.put("INFL2", "Régimen");

                } else {

                    parametros.put("INFL2", "");

                }
                parametros.put("INFL3", "");
                parametros.put("INFL4", "");

                if (empresa.equals("BillGates")) {

                    parametros.put("INFT1", "Contribuyente Régimen Microempresa");

                } else {

                    parametros.put("INFT1", "No. Resolución: 1");

                }
                if (empresa.equals("Corel")) {

                    parametros.put("INFT2", "Contribuyente Régimen Microempresa");

                } else {

                    parametros.put("INFT2", "");

                }
                parametros.put("INFT3", "");
                parametros.put("INFT4", "");
                parametros.put("PAGO_F", "SIN UTILIZACION DEL SISTEMA FINANCIERO");
                parametros.put("PAGO_V", "");

                rideGenerador rideFac = new rideGenerador();
                if (rideFac.ejecutar(parametros, pathJasperFac, pathXml, pathPdf, pathLogo, "/factura/detalles/detalle")) {

                    Thread.currentThread();
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //                copiar(url+empresa+"\\AUTORIZADOS\\"+clave+".xml", pathWeb+empresa+"\\XML\\"+clave+".xml");
                    //                copiar(url+empresa+"\\AUTORIZADOS\\RIDE\\"+clave+".pdf", pathWeb+empresa+"\\PDF\\"+clave+".pdf");
                    copiar(url + empresa + "\\AUTORIZADOS\\" + clave + ".xml", pathWeb + empresa + "\\XML\\");
                    copiar(url + empresa + "\\AUTORIZADOS\\RIDE\\" + clave + ".pdf", pathWeb + empresa + "\\RIDE\\");
                    //                System.out.println("ok");
                }
            }
        } else if (tip.equals("RET")) {
            String pathJasperRet = path + "\\rideRetencion.jrxml";
            String query = "SELECT LEFT(FECHAAUTORIZACION,10)+' '+SUBSTRING(FECHAAUTORIZACION,12,8),NUMEROAUTORIZACION"
                    + ",CASE WHEN PERFACTURAR='O' THEN (SELECT REPDIRECCION FROM " + ge + "..REPRESENTANTES E"
                    + "				WHERE B.TIPECODIGO = E.TIPECODIGO AND B.PERCODIGO = E.PERCODIGO)"
                    + "     ELSE PERDIRECCION END PERDIRECCION"
                    + ",(SELECT PROFCODIGO FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"' )"
                    + ",(SELECT PEROBSERVACION FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"' )"
                    + ",(SELECT ECCODIGO FROM " + ge + "..PERSONAS WHERE TIPECODIGO = 'A' AND CEDULARUC ='"+ruc+"' ) "
                    + "FROM " + vf + "..RELACIONSRI A, co" + vf.substring(2) + "..RETENCIONES B, " + ge + "..PERSONAS C "
                    + "WHERE A.IECABNUMERO = B.NRORETENCION "
                    + "AND B.TIPECODIGO = C.TIPECODIGO AND B.PERCODIGO = C.PERCODIGO "
                    + "AND A.IECABNUMERO = " + num + " AND A.TRINVTIPO = '" + tip + "'";
            System.out.println(query);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> fcab1 = dcons.buscador(query, 6);
            List<String> fcab = new ArrayList<>();
            if (fcab1.size() > 0) {
                fcab = fcab1.get(0);
            }

            if (fcab.size() > 5) {

                Map<String, Object> parametros = new HashMap<String, Object>();

                parametros.put("NUM_AUT", fcab.get(1));
                System.out.println("NUM_AUT");
                parametros.put("FECHA_AUT", fcab.get(0));
                System.out.println("FECHA_AUT");
                parametros.put("TIPO_EMISION", "NORMAL");
                System.out.println("TIPO_EMISION");
                try {
                    parametros.put("LOGO", new FileInputStream(pathLogo));
                    System.out.println("LOOOOOOOOOOOOOOOOOOOOOOOOGO");
                } catch (FileNotFoundException ex) {
                    parametros.put("LOGO", "");
                    System.out.println("LOOOOOOOGO ERROOOOOOOOOOOOOOOOOOOR");
                }
                //            System.out.println("amb:"+ambiente);
                parametros.put("CONT_ESPECIAL", fcab.get(3));
                System.out.println("CONT_ESPECIAL");
                parametros.put("LLEVA_CONTABILIDAD", fcab.get(5));
                System.out.println("LLEVA_CONTABILIDAD");
                parametros.put("DIRECCION_CLIENTE", fcab.get(2));
                System.out.println("DIRECCION_CLIENTE");
                parametros.put("COMPROBANTE", "FACTURA");
                System.out.println("COMPROBANTE FACTURA");
                parametros.put("AMBIENTE", ambiente);
                System.out.println("AMBIENTE");
                parametros.put("NOM_COMERCIAL", fcab.get(4));
                System.out.println("NOM_COMERCIAL");

                if (empresa.equals("BillGates")) {

                    parametros.put("INFL1", "Régimen");

                } else {

                    parametros.put("INFL1", "Agente de Retención");

                }
                if (empresa.equals("Corel")) {

                    parametros.put("INFL2", "Régimen");

                } else {

                    parametros.put("INFL2", "");

                }
                parametros.put("INFL3", "");
                parametros.put("INFL4", "");

                if (empresa.equals("BillGates")) {

                    parametros.put("INFT1", "Contribuyente Régimen Microempresa");

                } else {

                    parametros.put("INFT1", "No. Resolución: 1");

                }
                if (empresa.equals("Corel")) {

                    parametros.put("INFT2", "Contribuyente Régimen Microempresa");

                } else {

                    parametros.put("INFT2", "");

                }
                parametros.put("INFT3", "");
                parametros.put("INFT4", "");

                rideGenerador rideFac = new rideGenerador();
                if (rideFac.ejecutar(parametros, pathJasperRet, pathXml, pathPdf, pathLogo, "/comprobanteRetencion/impuestos/impuesto")) {

                    Thread.currentThread();
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //                copiar(url+empresa+"\\AUTORIZADOS\\"+clave+".xml", pathWeb+empresa+"\\XML\\"+clave+".xml");
                    //                copiar(url+empresa+"\\AUTORIZADOS\\RIDE\\"+clave+".pdf", pathWeb+empresa+"\\PDF\\"+clave+".pdf");
                    copiar(url + empresa + "\\AUTORIZADOS\\" + clave + ".xml", pathWeb + empresa + "\\XML\\");
                    copiar(url + empresa + "\\AUTORIZADOS\\RIDE\\" + clave + ".pdf", pathWeb + empresa + "\\RIDE\\");
                    //                System.out.println("ok");
                }
            }
        } else if (tip.equals("NCR")) {
            String pathJasper = path + "\\rideNCredito.jrxml";
            // extrae fecha
            FileReader fr = null;
            BufferedReader br = null;
            File f = new File(pathXmlAut);
            String fecha = "";
            try {
                fr = new FileReader(f);
                br = new BufferedReader(fr);
                String linea, txt1 = "<fechaAutorizacion>", txt2 = "</fechaAutorizacion>";
                while ((linea = br.readLine()) != null) {
                    if (linea.contains(txt1)) {
                        int index1 = linea.indexOf(txt1);
                        int index2 = linea.indexOf(txt2);
                        fecha = linea.substring(index1 + txt1.length(), index2);
                        //                    System.out.println("fecha:"+fecha);
                    }
                }
            } catch (IndexOutOfBoundsException | java.io.FileNotFoundException iout) {
                System.out.println("Error en archivo txt:" + iout);
            } catch (IOException ex) {
                System.out.println("Error en archivo txt:" + ex);
            }
            //fin extrae fecha
            //extrae datos
//            String query = "SELECT "
//                        + "CFDIMPONIBLE"
//                        + ",CFDNOIMPONIBLE"
//                        + ",CFDIMPUESTO"
//                        + ",CFDIMPONIBLE+CFDNOIMPONIBLE+CFDIMPUESTO TOTAL "
//                        + "FROM "+vf+"..RELACIONSRI A, "+vf+"..IECABECERA B, "+vf+"..CABFACTDEV C, "+ge+"..PERSONAS D "
//                        + "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO "
//                        + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
//                        + "AND B.BODCODIGO = C.BODCODIGO AND B.TRINVTIPO = C.TRINVTIPO "
//                        + "AND B.IECABNUMERO = C.IECABNUMERO AND B.IECABTIPO = C.IECABTIPO "
//                        + "AND B.TIPECODIGO = D.TIPECODIGO AND B.PERCODIGO = D.PERCODIGO AND A.IECABNUMERO = "+num+" AND A.TRINVTIPO = '"+tip+"'";
            String query = "SELECT DCTOIMPONIBLE,DCTONOIMPONIBLE,DCTOIVA,DCTOIMPONIBLE+DCTONOIMPONIBLE+DCTOIVA TOTAL,C.PERMAIL "
                    + "FROM " + vf + "..RELACIONSRI A, " + vf + "..DOCUMENTOS B, " + ge + "..PERSONAS C "
                    + "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRCLIDOCUMENTO AND A.IECABNUMERO = B.DCTONUMERO "
                    + "AND A.IECABTIPO = B.DCTOTIPO "
                    + "AND B.TIPECODIGO = C.TIPECODIGO AND B.PERCODIGO = C.PERCODIGO "
                    + "AND B.DCTONUMERO = " + num + " AND B.TRCLIDOCUMENTO = 'NCR'";
            System.out.println(query);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> fcab1 = dcons.buscador(query, 5);
            List<String> fcab2 = fcab1.get(0);
            //fin extrae datos

            Map<String, Object> parametros = new HashMap<String, Object>();
            try {
                parametros.put("LOGO", new FileInputStream(pathLogo));
            } catch (FileNotFoundException ex) {
                parametros.put("LOGO", "");
            }
            parametros.put("FECHA_AUT", fecha);//"java.lang.String"/>
            parametros.put("CLI_MAIL", fcab2.get(4));//"java.lang.String"/>
            parametros.put("SUB12", fcab2.get(0));//"java.lang.String"/>
            parametros.put("SUB0", fcab2.get(1));//"java.lang.String"/>
            //        parametros.put("SUB_NO","0.00");//"java.lang.String"/>
            //        parametros.put("SUB_EX","0.00");//"java.lang.String"/>
            //        parametros.put("SUB_ICE","0.00");//"java.lang.String"/>
            parametros.put("SUB_IVA", fcab2.get(2));//"java.lang.String"/>
            parametros.put("SUB_TOT", fcab2.get(3));//"java.lang.String"/>
            //        parametros.put("SUB_IRB","0.00");//"java.lang.String"/>
            //        parametros.put("SUB_PRO","0.00");//"java.lang.String"/>

            //        String pathXml="";
            rideGenerador rideFac = new rideGenerador();
            if (rideFac.ejecutar(parametros, pathJasper, pathXml, pathPdf, pathLogo, "/notaCredito/detalles/detalle")) {
                Thread.currentThread();
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }

//                copiar(url+empresa+"\\AUTORIZADOS\\"+clave+".xml", pathWeb+empresa+"\\XML\\"+clave+".xml");
//                copiar(url+empresa+"\\AUTORIZADOS\\RIDE\\"+clave+".pdf", pathWeb+empresa+"\\PDF\\"+clave+".pdf");
                copiar(url + empresa + "\\AUTORIZADOS\\" + clave + ".xml", pathWeb + empresa + "\\XML\\");
                copiar(url + empresa + "\\AUTORIZADOS\\RIDE\\" + clave + ".pdf", pathWeb + empresa + "\\RIDE\\");
                System.out.println("ok");
            }
        } else if (tip.equals("NDB")) {
            String pathJasper = path + "\\rideNDebito.jrxml";
        }
    }

    public void copiar(String pathOr, String pathDe) {
        String cmd = "cmd.exe /c copy " + pathOr + " " + pathDe + " /Y";
        System.out.println(cmd);
        try {
            Runtime.getRuntime().exec(cmd);
            Runtime.getRuntime().runFinalization();
        } catch (IOException ex) {
            Runtime.getRuntime().runFinalization();
        }

    }
}
