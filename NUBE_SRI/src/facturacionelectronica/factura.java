package facturacionelectronica;

//import com.sun.org.apache.bcel.internal.generic.DCONST;
import conexion.datosConsulta;
import conexion.datosGuarda;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class factura {
    
    String ambiente = "2";
    String tipoEmision = "1";
    String razonSocial = "";
    String ruc = "";
    String claveAcceso = "";
    String codDoc = "01";
    String establecimiento="";
    String puntoEmision="";
    String secuencial="";
//    String Direccion="";
    
    int codigoNumerico;
    String dirMatriz = "";
    int secuenciaNum = 0;
    boolean band = true;
    String BodCodigo;
    String TrInvTipo;
    String IECabNumero;
    String IECabTipo;
    String baseVF;
    String baseGE;
    String path;
//    List<String> fempresa = new ArrayList<>();
    
//    String BodCodigo="",TrInvTipo="",IECabNumero="",IECabTipo="";
    
    public int modulo11(String cadena) {
        int v7 = 7;
        int longitudCadena = cadena.length();
        int cantidadTotal = 0;
        for (int i = 0; i < longitudCadena; i++) {
            if (v7 == 1) {
                v7 = 7;
            }
            int temporal = Integer.parseInt(cadena.substring(i,(i+1)));
            temporal = temporal*v7;
            v7--;
            cantidadTotal = cantidadTotal + temporal;
        }
        cantidadTotal = 11 - (cantidadTotal % 11);
        if(cantidadTotal==11){
            cantidadTotal=0;
        }
        if(cantidadTotal==10){
            cantidadTotal=1;
        }
        return cantidadTotal;
    }
    public String claveAcceso(String fecha, String tipo, String ruc, String ambiente, String secuencia, String tipEmi){
        if(fecha.length()==10){
            fecha=fecha.substring(0, 2)+fecha.substring(3, 5)+fecha.substring(6, 10);
        }
        String txt = fecha+tipo+ruc+ambiente+establecimiento+"100"+secuencia+intToStringFormat(codigoNumerico, 8)+tipEmi;
        return txt+modulo11(txt);
    }
    public String intToStringFormat(int num, int dig){
        String txt=num+"";
        int leng = txt.length();
        if(leng<dig){
            for (int i = 0; i < (dig-leng); i++) {
                txt = "0"+txt;
            }
        }
        return txt;
    }
//    public void docList(List<List<String>> ltemp, List<List<String>> lpagos){
    public void docList(List<List<String>> ltemp){
        if(ltemp.size()>0){
            secuencial=intToStringFormat(secuenciaNum,9);
            claveAcceso=claveAcceso(ltemp.get(0).get(2),codDoc,ruc,ambiente,secuencial,tipoEmision);
            
            String tarifaIVA = "0";
            if(Double.parseDouble(ltemp.get(0).get(13))>0){
                tarifaIVA = "2";
            }
            String cedCli = ltemp.get(0).get(4);
            String cedCliTip = "";
            switch (cedCli.length()) {
                case 10:
                    cedCliTip = "05";
                    break;
                case 13:
                    cedCliTip = "04";
                    break;
                default:
                    cedCliTip = "06";
                    break;
            }
            
            String fechaEmision = ltemp.get(0).get(2);
//            String tipoIdent = ltemp.get(0).get(3);
            String tipoIdent = cedCliTip;
            String razonSocialCli = ltemp.get(0).get(7);
//            String identificacionComprador = ltemp.get(0).get(4);
            String identificacionComprador = cedCli;
            String totalSinImpuestos = ltemp.get(0).get(17);
            String totalDescuento = ltemp.get(0).get(10);
            String importeTotal = ltemp.get(0).get(11);
            String impCodigo = "2";
            String impCodigoPorcentaje = tarifaIVA;
            String impBaseImponible = ltemp.get(0).get(17);
            String impValor = ltemp.get(0).get(14);
    
            xmlFactura xml = new xmlFactura();
            xml.setAmbiente(ambiente);
            xml.setTipoEmision(tipoEmision);
            xml.setRazonSocial(razonSocial);
            xml.setRuc(ruc);
            xml.setClaveAcceso(claveAcceso);
            xml.setCodDoc(codDoc);
            xml.setEstab(establecimiento);
            xml.setPtoEmi(puntoEmision);
            xml.setSecuencial(secuencial);
            xml.setDirMatriz(dirMatriz);
            xml.setFechaEmision(fechaEmision);
            xml.setTipoIdent(tipoIdent);
            xml.setRazonSocialCli(razonSocialCli);
            xml.setIdentificacionComprador(identificacionComprador);
            xml.setTotalSinImpuestos(totalSinImpuestos);
            xml.setTotalDescuento(totalDescuento);
            xml.setImporteTotal(importeTotal);
            xml.setImpCodigo(impCodigo);
            xml.setImpCodigoPorcentaje(impCodigoPorcentaje);
            xml.setImpBaseImponible(impBaseImponible);
            xml.setImpValor(impValor);

            List<xmlFactura.detalle> det = new ArrayList<>();
            
            for (int i = 0; i < ltemp.size(); i++) {
                String dDescripcion = ltemp.get(i).get(1);
                String dCantidad = ltemp.get(i).get(8).replaceAll(",", "");
                String dPrecioUnitario = ltemp.get(i).get(9).replaceAll(",", "");
                String dDescuento = ltemp.get(i).get(12).replaceAll(",", "");
                String dPrecioTotalSinImpuesto = ltemp.get(i).get(16).replaceAll(",", "");
                String dImpCodigo = "2";
                String dImpCodigoPorcentaje = tarifaIVA;
                String dImpBaseImponible = ltemp.get(i).get(16);
                String dImpTarifa = ltemp.get(i).get(13);
                String dImpValor = ltemp.get(i).get(15);

                xmlFactura.detalle detalle = new xmlFactura.detalle();
                detalle.setDescripcion(dDescripcion);
                detalle.setCantidad(dCantidad);
                detalle.setPrecioUnitario(dPrecioUnitario);
                detalle.setDescuento(dDescuento);
                detalle.setPrecioTotalSinImpuesto(dPrecioTotalSinImpuesto);
                detalle.setImpCodigo(dImpCodigo);
                detalle.setImpCodigoPorcentaje(dImpCodigoPorcentaje);
                detalle.setImpBaseImponible(dImpBaseImponible);
                detalle.setImpTarifa(dImpTarifa);
                detalle.setImpValor(dImpValor);
                det.add(detalle);

                xml.setDet(det);
            }
            String xmlAux = xml.generarXML();
//            System.out.println("xmlAux:"+xmlAux);
            crearXml(xmlAux, path+"\\GENERADOS\\"+claveAcceso+".xml");
            
            escribirBase(ltemp.get(0).get(5));
        }
    }
    public void escribirBase(String mail){
        String query = "SELECT MAX(ESTADOEMISION) "
                    + "FROM "+baseVF+"..RELACIONSRI WHERE BODCODIGO="+BodCodigo+" AND TRINVTIPO='"+TrInvTipo+"' AND IECABNUMERO="+IECabNumero+" AND IECABTIPO='"+IECabTipo+"'";
            datosConsulta dcons = new datosConsulta();
            List<String> estadoEmision = dcons.buscador(query, 1).get(0);
            
            if(estadoEmision.get(0)==null){
                datosGuarda dg = new datosGuarda();
                query = "INSERT INTO "+baseVF+"..RELACIONSRI "
                        + "(BodCodigo,TrInvTipo,IECabNumero,IECabTipo,SecuencialSRI,CodigoNumerico,ClaveAutorizacion,PerMail, estadoemision) "
                        + "VALUES("+BodCodigo+",'"+TrInvTipo+"',"+IECabNumero+",'"+IECabTipo+"',"+secuencial+","+codigoNumerico+",'"+claveAcceso+"','"+mail+"', 'N')";
//                System.out.println("q0:"+query);
                dg.guardarBase(query);
                query = "UPDATE "+baseVF+"..SecuenciaSRI SET SecNumero = SecNumero+1 WHERE SecTipo='"+TrInvTipo+"'";
                dg.guardarBase(query);
            }else{
                if(estadoEmision.get(0).equals("DEVUELTA")){
                    query = "SELECT CODIGONUMERICO+1 FROM "+baseVF+"..RELACIONSRI WHERE BODCODIGO="+BodCodigo+" AND TRINVTIPO='"+TrInvTipo+"' AND IECABNUMERO="+IECabNumero+" AND IECABTIPO='"+IECabTipo+"'";
                    String codNum = dcons.consUnDato(query);
//                    System.out.println(codNum);

                    datosGuarda dg = new datosGuarda();
                    query = "UPDATE "+baseVF+"..RELACIONSRI SET CODIGONUMERICO="+codNum+", ESTADOEMISION='', NUMEROAUTORIZACION = '"+claveAcceso+"' WHERE BODCODIGO="+BodCodigo+" AND TRINVTIPO='"+TrInvTipo+"' AND IECABNUMERO="+IECabNumero+" AND IECABTIPO='"+IECabTipo+"'";
//                    System.out.println("UP:"+query);
                    dg.guardarBase(query);
                }
            }
    }
    public byte[] archivoToByte(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                //TODO LOGGER
            }
        }

        return buffer;
    }
    public void crearXml(String xml, String fileName){
        try {
            File archivo = new File(fileName);
            BufferedWriter bw = null;
            if(archivo.exists()) {
                archivo.delete();
            }
            try {
//                bw = new BufferedWriter(new FileWriter(archivo));
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8));
                bw.write(xml);
            } catch (IOException ex) {
                Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
            }
//            }
            if(bw!=null){
                bw.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean crear(String BodCodigo, String TrInvTipo, String IECabNumero, String IECabTipo, String baseVF, String baseGE, int seq, int codigoNumerico, String path, String ambiente,String razonSocial,String ruc,String codDoc,String establecimiento,String puntoEmision,String dirMatriz){
        this.ambiente=ambiente;
        this.razonSocial=razonSocial;
        this.ruc=ruc;
        this.codDoc=codDoc;
        this.establecimiento=establecimiento;
        this.puntoEmision=puntoEmision;
        this.dirMatriz=dirMatriz;
        
        this.secuenciaNum=seq;
        this.codigoNumerico=codigoNumerico;
        this.BodCodigo = BodCodigo;
        this.TrInvTipo=TrInvTipo;
        this.IECabNumero=IECabNumero;
        this.IECabTipo=IECabTipo;
        this.baseVF=baseVF;
        this.baseGE=baseGE;
        this.path=path;
        
//        ambiente = "1";
        if(TrInvTipo.equals("VTA")){
            String query = "SELECT "
                    + "A.IECABNUMERO"
                    + ", F.PRONOMBRE"
                    + ",convert(varchar, A.IECABFECHA, 103),"
                    + "(case when PERDOCUMENTO='R' then '04' when PERDOCUMENTO='C' then '05' when PERDOCUMENTO='P' then '06' else '07' end)"
                    + ",CASE WHEN PERFACTURAR='E' THEN CEDULARUC"
                    + "     WHEN PERFACTURAR='P' THEN (SELECT REPCEDULARUC1 FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     WHEN PERFACTURAR='M' THEN (SELECT REPCEDULARUC2 FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     WHEN PERFACTURAR='O' THEN (SELECT REPCEDULARUC3 FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     ELSE '' END CEDULARUC"
                    + ",CASE WHEN PERFACTURAR='O' THEN (SELECT REPCORREO FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     ELSE PERMAIL END PERMAIL"//5
                    + ",B.PROCODIGO"
                    + ",CASE WHEN PERFACTURAR='E' THEN PERAPELLIDOS+' '+PERNOMBRES"
                    + "     WHEN PERFACTURAR='P' THEN (SELECT REPPADRE FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     WHEN PERFACTURAR='M' THEN (SELECT REPMADRE FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     WHEN PERFACTURAR='O' THEN (SELECT REPREPRESENTANTE FROM "+baseGE+"..REPRESENTANTES G"
                    + "				WHERE A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO"
                    + "				AND C.TIPECODIGO = G.TIPECODIGO AND C.PERCODIGO = G.PERCODIGO)"
                    + "     ELSE '' END NOMBRES"
                    + ",IEDETCANTIDAD"
                    + ",DFDPRECIOUNITARIO, "
                    + "CFDDESNOIMPONIBLE+CFDDESIMPONIBLE DESCUENTOT"
                    + ",IECABTOTAL"
//                    + ",C.PERMAIL "//12
                    + ",E.DFDDESCUENTOT DESCUENTOL "
                    + ",E.DFDIMPIVA " 
                    + ",D.CFDIMPUESTO "
                    + ",E.DFDIMPUESTO " 
                    + ",DFDPRECIOUNITARIO-DFDDESCUENTO " 
                    + ",IECABTOTAL-D.CFDIMPUESTO " +
                "FROM "+baseVF+"..IECABECERA A, "+baseVF+"..IEDETALLE B, "+baseGE+"..PERSONAS C, "+baseVF+"..CABFACTDEV D, "+baseVF+"..DETFACTDEV E, "+baseVF+"..PRODUCTOS F " +
                "WHERE A.TRINVTIPO = 'VTA' " +
                "AND D.BodCodigo="+BodCodigo+" AND D.TrInvTipo='"+TrInvTipo+"' AND D.IECabNumero="+IECabNumero+" AND D.IECabTipo='"+IECabTipo+"' " +
                "AND IECABESANULADA = 'N' " +
                "AND A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO " +
                "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO " +
                "AND A.BODCODIGO = D.BODCODIGO AND A.TRINVTIPO = D.TRINVTIPO " +
                "AND A.IECABNUMERO = D.IECABNUMERO AND A.IECABTIPO = D.IECABTIPO " +
                "AND B.BODCODIGO = E.BODCODIGO AND B.TRINVTIPO = E.TRINVTIPO " +
                "AND B.IECABNUMERO = E.IECABNUMERO AND B.IECABTIPO = E.IECABTIPO " +
                "AND B.IEDETSECUENCIA = E.IEDETSECUENCIA " +
                "AND A.TIPECODIGO = C.TIPECODIGO AND A.PERCODIGO = C.PERCODIGO " +
                "AND B.PROCODIGO = F.PROCODIGO ORDER BY 1";
            datosConsulta dcons = new datosConsulta();
            List<List<String>> fcab = dcons.buscador(query, 18);

            if(fcab.isEmpty()){
                try {
                    Runtime.getRuntime().exec("cmd.exe /c echo " + query + " >> " + path+"\\log.txt");
                    Runtime.getRuntime().runFinalization();
                } catch (IOException ex) {
                    Runtime.getRuntime().runFinalization();
                }
            }
            List<List<String>> ltemp = new ArrayList<>();
            for (int i = 0; i < fcab.size(); i++){
                if((i+1)<fcab.size()){
                    if(fcab.get(i+1).get(0).equals(fcab.get(i).get(0))){
                        ltemp.add(fcab.get(i));
                    }else{
                        ltemp.add(fcab.get(i));
    //                    if(fpagos.size()>0){ptemp.add(fpagos.get(i));}
                        docList(ltemp);
                        ltemp = new ArrayList<>();
    //                    ptemp = new ArrayList<>();
                    }
                }else{
                    ltemp.add(fcab.get(i));
                }
            }
            if(ltemp.size()>0){
                docList(ltemp);
            }
        }else if(TrInvTipo.equals("RET")){
            String queryRet = "Select "
                    + "convert(varchar, TraFecha, 103) fechaEmisionDocSustento"
                    + ",c.perdireccion"
                    + ",pronumerocontrib contEsp"
                    + ",prollevacontabilidad llevaCont"
                    + ",(case when c.PERDOCUMENTO='R' then '04' when c.PERDOCUMENTO='C' then '05' when c.PERDOCUMENTO='P' then '06' else '07' end)"
                    + ",PERNOMBRES+' '+PERAPELLIDOS"
                    + ",CEDULARUC"
                    + ",Case When RteEsIVA = 'N' Then 1 Else 2 End codigo"
                    + ",CodRetencion codigoRetencion"
                    + ",Case When CodRetencion = '322' Then convert(numeric(9,2),Round(RteBaseImponible/10,2)) Else convert(numeric(9,2),RteBaseImponible) End baseImponible"
                    + ",RtePorcentaje porcentajeRetener"
                    + ",RteValor valorRetenido"
                    + ",'01' codDocSustento"
                    + ",Left(RteSerie,3) Sucursal"
                    + ",Right(RteSerie,3) PuntoEmision"
                    + ",RteDocumento NroFactura"
                    + ",PERMAIL "//5
                    + "From co"+baseVF.substring(2)+"..Retenciones a, co"+baseVF.substring(2)+"..RteDetalle b, ge"+baseVF.substring(2)+"..personas c, vf"+baseVF.substring(2)+"..proveedores d "
                    + "Where a.NroRetencion = b.NroRetencion "
                    + "and a.percodigo = c.percodigo and a.tipecodigo=c.tipecodigo "
                    + "and a.percodigo = d.percodigo and a.RteEsReversado='N' "
                    + "And a.NroRetencion = "+IECabNumero;
//            System.out.println("queryRet:"+queryRet);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> datRenta = dcons.buscador(queryRet, 17);
//            ambiente = "1";
            String fechaDoc = datRenta.get(0).get(0);
//            String fecha = "11/12/2018";
            
            secuencial=intToStringFormat(secuenciaNum,9);
            claveAcceso=claveAcceso(fechaDoc,codDoc,ruc,ambiente,secuencial,tipoEmision);
            
            List<String> cabImp = new ArrayList<>();
            cabImp.add(ambiente);//<ambiente>"+cab.get(0)+"</ambiente>" +
            cabImp.add("1");//<tipoEmision>"+cab.get(1)+"</tipoEmision>" +
            cabImp.add(razonSocial);//<razonSocial>"+cab.get(2)+"</razonSocial>" +
            cabImp.add(razonSocial);//<nombreComercial>"+cab.get(3)+"</nombreComercial>" +
            cabImp.add(ruc);//<ruc>"+cab.get(4)+"</ruc>" +
            cabImp.add(claveAcceso);//<claveAcceso>"+cab.get(5)+"</claveAcceso>" +
            cabImp.add(codDoc);//<codDoc>"+cab.get(6)+"</codDoc>" +
            cabImp.add(establecimiento);//<estab>"+cab.get(7)+"</estab>" +
            cabImp.add(puntoEmision);//<ptoEmi>"+cab.get(8)+"</ptoEmi>" +
            cabImp.add(secuencial);//<secuencial>"+cab.get(9)+"</secuencial>" +
            cabImp.add(dirMatriz);//<dirMatriz>"+cab.get(10)+"</dirMatriz>" +
            
            
            String llevaCont = datRenta.get(0).get(3);
            if(llevaCont.equals("S")){
                llevaCont = "SI";
            }else{
                llevaCont = "NO";
            }
//            String fechaDoc = fecha;
            
            cabImp.add(fechaDoc);//<fechaEmision>"+cab.get(11)+"</fechaEmision>" +
            cabImp.add(datRenta.get(0).get(1));//<dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>" +
            cabImp.add(datRenta.get(0).get(2));//<contribuyenteEspecial>"+cab.get(13)+"</contribuyenteEspecial>" +
            cabImp.add(llevaCont);//<obligadoContabilidad>"+cab.get(14)+"</obligadoContabilidad>" +
            cabImp.add(datRenta.get(0).get(4));//<tipoIdentificacionSujetoRetenido>"+cab.get(15)+"</tipoIdentificacionSujetoRetenido>" +
            cabImp.add(datRenta.get(0).get(5));//<razonSocialSujetoRetenido>"+cab.get(16)+"</razonSocialSujetoRetenido>" +
            cabImp.add(datRenta.get(0).get(6));//<identificacionSujetoRetenido>"+cab.get(17)+"</identificacionSujetoRetenido>" +
            cabImp.add(fechaDoc.substring(3));//<periodoFiscal>"+cab.get(18)+"</periodoFiscal>" +
            cabImp.add("");//
            cabImp.add("");//
            cabImp.add("");//
            cabImp.add("");//
            cabImp.add("");//
            cabImp.add("");//
            
            List<List<String>> impuest = new ArrayList<>();
            List<String> impuestAux = new ArrayList<>();
            for (int i = 0; i <datRenta.size(); i++) {
                impuestAux = new ArrayList<>();
                String codDet = datRenta.get(i).get(7);
                if(codDet.length()==4){
                    codDet = codDet.substring(0,1);
                }
                String codRet = datRenta.get(i).get(8);
//                String porcRet = datRenta.get(i).get(10).replaceAll(".00", "");
                String porcRet = datRenta.get(i).get(10);
                if(porcRet.length()>3){
                    porcRet = porcRet.substring(0,porcRet.length()-3);
                }
                
                if(porcRet.equals("30") && codDet.equals("2")){
                    codRet = "1";
                }else if(porcRet.equals("70") && codDet.equals("2")){
                    codRet = "2";
                }else if(porcRet.equals("100") && codDet.equals("2")){
                    codRet = "3";
                }else if(porcRet.equals("10") && codDet.equals("2")){
                    codRet = "9";
                }else if(porcRet.equals("20") && codDet.equals("2")){
                    codRet = "10";
                }else if(porcRet.equals("50") && codDet.equals("2")){
                    codRet = "11";
                }
                impuestAux.add(codDet);//<codigo>"+imp.get(i).get(0)+"</codigo>" +
                impuestAux.add(codRet);//<codigoRetencion>"+imp.get(i).get(1)+"</codigoRetencion>" +
                impuestAux.add(datRenta.get(i).get(9).replaceAll(",", ""));//<baseImponible>"+imp.get(i).get(2)+"</baseImponible>" +
                impuestAux.add(porcRet);//<porcentajeRetener>"+imp.get(i).get(3)+"</porcentajeRetener>" +
                impuestAux.add(datRenta.get(i).get(11).replaceAll(",", ""));//<valorRetenido>"+imp.get(i).get(4)+"</valorRetenido>" +
                impuestAux.add(datRenta.get(i).get(12));//<codDocSustento>"+imp.get(i).get(5)+"</codDocSustento>" +
                impuestAux.add(datRenta.get(i).get(13)+datRenta.get(i).get(14)+intToStringFormat(Integer.parseInt(datRenta.get(i).get(15)),9));//<numDocSustento>"+imp.get(i).get(6)+"</numDocSustento>" +
                impuestAux.add(fechaDoc);//<fechaEmisionDocSustento>"+imp.get(i).get(7)+"</fechaEmisionDocSustento>" +
                impuest.add(impuestAux);
            }
            xmlRetencion xmlRet = new xmlRetencion();
            String xml = xmlRet.retencion(cabImp, impuest);
            crearXml(xml, path+"\\GENERADOS\\"+claveAcceso+".xml");
            
            escribirBase(datRenta.get(0).get(16));
        }else if(TrInvTipo.equals("NDB")){
            String qNDB = "Select Convert(varchar, a.DctoFechaEmision, 103) FechaEmision," +
            "Case When PerDocumento='R' Then '04' " +
            "     When PerDocumento='C' Then '05' " +
            "     When PerDocumento='P' Then '06' " +
            "     Else '07' END Documento," +
            "CASE When PerFacturar='E' Then PerApellidos+' '+PerNombres" +
            "     When PerFacturar='P' Then (Select RepPadre From "+baseGE+"..Representantes g" +
                    //And a.PerCodigo = '00004689'
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='M' Then (Select RepMadre From "+baseGE+"..Representantes g" +
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='O' Then (Select RepRepresentante From "+baseGE+"..Representantes g" +
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else '' End Nombres," +
            "Case When PerFacturar='E' Then CedulaRUC" +
            "     When PerFacturar='P' Then (Select REPCedulaRUC1 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='M' Then (Select REPCedulaRUC2 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='O' Then (Select REPCedulaRUC3 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else '' End CedulaRUC," +
            "" +
            "DctoCliente," +
            "Convert(varchar, d.IECabFecha, 103) FechaEmisionDocModificado"
                    + ",DctoNoImponible+DctoImponible TotalSinImpuesto"
                    + ",DctoNoImponible+DctoImponible+DctoIVA total"
                    + ",Substring(DctoDescripcion,5,120) motivo," +
            "Case When PerFacturar='O' Then (Select RepCorreo From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.PerCodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.PerCodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else PerMail End PerMail"
                    + ",DetDescripcion,1,DetValor,DetValor " +
            ", DFDImpuesto, DctoNoImponible sub0, DctoImponible sub12, DctoIVA iva " +
            "From "+baseVF+"..Documentos a,"+baseVF+"..DetalleDcmtos b,"+baseGE+"..Personas c,"+baseVF+"..IECabecera d, "+baseVF+"..DetFactDev e " +
            "Where a.BodCodigo="+BodCodigo+" And A.TrCliDocumento='NDB' And A.DctoNumero="+IECabNumero+" And A.DctoTipo='"+IECabTipo+"' And a.DctoEsAnulado = 'N' " +
            "And a.TipEcodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo " +
            "And a.BodCodigo = b.BodCodigo And a.TrCliDocumento = b.TrCliDocumento " +
            "And a.DctoNumero = b.DctoNumero And a.DctoTipo = b.DctoTipo " +
            "And d.BodCodigo = 1 And d.TrInvTipo = 'VTA' " +
            "And d.IECabNumero = (select IECabNumero from "+baseVF+"..relacionSRI s where s.secuencialSRI=Convert(Numeric(9,0),Right(DctoCliente,9))) " +
            "And d.IECabTipo = '"+IECabTipo+"' " +
            "And e.BodCodigo = 1 And e.TrInvTipo = 'VTA' " +
            "And e.IECabNumero = (select IECabNumero from "+baseVF+"..relacionSRI s where s.secuencialSRI=Convert(Numeric(9,0),Right(DctoCliente,9))) " +
            "And e.IECabTipo = '"+IECabTipo+"' and codcuenta <> '211324'";
//            System.out.println("qNDB:"+qNDB);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> filas = dcons.buscador(qNDB, 18);
            
            String obligadoConta = "SI";
            if(this.baseGE.equals("geBG")){
                obligadoConta = "NO";
            }
            String fechaDoc = "";
            if(filas.size()>0){
                fechaDoc = filas.get(0).get(0);
            }
            secuencial=intToStringFormat(secuenciaNum,9);
            claveAcceso=claveAcceso(fechaDoc,codDoc,ruc,ambiente,secuencial,tipoEmision);
            
            List<String> cab = new ArrayList<>();
            cab.add(ambiente);//<ambiente>"+cab.get(0)+"</ambiente>" +
            cab.add("1");//<tipoEmision>"+cab.get(1)+"</tipoEmision>" +
            cab.add(razonSocial);//<razonSocial>"+cab.get(2)+"</razonSocial>" +
            cab.add(razonSocial);//<nombreComercial>"+cab.get(3)+"</nombreComercial>" +
            cab.add(ruc);//<ruc>"+cab.get(4)+"</ruc>" +
            cab.add(claveAcceso);//<claveAcceso>"+cab.get(5)+"</claveAcceso>" +
            cab.add(codDoc);//<codDoc>"+cab.get(6)+"</codDoc>" +
            cab.add(establecimiento);//<estab>"+cab.get(7)+"</estab>" +
            cab.add(puntoEmision);//<ptoEmi>"+cab.get(8)+"</ptoEmi>" +
            cab.add(secuencial);//<secuencial>"+cab.get(9)+"</secuencial>" +
            cab.add(dirMatriz);//<dirMatriz>"+cab.get(10)+"</dirMatriz>" +
            
            
            List<String> cabDB = new ArrayList<>();
            if(filas.size()>0){
                cabDB = filas.get(0);
                
                Double sub0 = Double.parseDouble(cabDB.get(15).replaceAll(",", ""));
                Double sub12 = Double.parseDouble(cabDB.get(16).replaceAll(",", ""));
                Double iva = Double.parseDouble(cabDB.get(17).replaceAll(",", ""));
//                Double tot = sub0 + sub12 + iva;
                cab.add(cabDB.get(0));//<fechaEmision>"+cab.get(11)+"</fechaEmision>" +
                cab.add(dirMatriz);//<dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>" +
                cab.add(cabDB.get(1));//<tipoIdentificacionComprador>"+cab.get(13)+"</tipoIdentificacionComprador>" +
                cab.add(cabDB.get(2));//<razonSocialComprador>"+cab.get(14)+"</razonSocialComprador>" +
                cab.add(cabDB.get(3));//<identificacionComprador>"+cab.get(15)+"</identificacionComprador>" +
                cab.add("000");//<contribuyenteEspecial>"+cab.get(16)+"</contribuyenteEspecial>" +
                cab.add(obligadoConta);//<obligadoContabilidad>"+cab.get(17)+"</obligadoContabilidad>" +
                cab.add("");//<rise>rise"+cab.get(18)+"</rise>" +
                cab.add("01");//<codDocModificado>"+cab.get(19)+"</codDocModificado>" +
                cab.add(cabDB.get(4));//<numDocModificado>"+cab.get(20)+"</numDocModificado>" +
                cab.add(cabDB.get(5));//<fechaEmisionDocSustento>"+cab.get(21)+"</fechaEmisionDocSustento>" +
                cab.add(cabDB.get(6).replaceAll(",", ""));//<totalSinImpuestos>"+cab.get(22)+"</totalSinImpuestos>" +
                cab.add(cabDB.get(7).replaceAll(",", ""));//"<valorModificacion>"+cab.get(23)+"</valorModificacion>" +
                cab.add(cabDB.get(8));//<motivo>"+cab.get(25)+"</motivo>" +

                cab.add("emailCliente");//<campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>" +
                cab.add(cabDB.get(9));//<campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>" +

                List<List<String>> impuestos = new ArrayList<>();
                List<String> impuesto = new ArrayList<>();
                if(sub0>0){
                    impuesto = new ArrayList<>();
                    impuesto.add("2");//<codigo>"+impuesto.get(0)+"</codigo>" +
                    impuesto.add("0");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add(sub0+"");//<baseImponible>"+impuesto.get(3)+"</baseImponible>" +
                    impuesto.add("0");//<valor>"+impuesto.get(4)+"</valor>" +
                    impuestos.add(impuesto);
                }
                if(sub12>0){
                    impuesto = new ArrayList<>();
                    impuesto.add("2");//<codigo>"+impuesto.get(0)+"</codigo>" +
                    impuesto.add("2");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add(sub12+"");//<baseImponible>"+impuesto.get(3)+"</baseImponible>" +
                    impuesto.add(iva+"");//<valor>"+impuesto.get(4)+"</valor>" +
                    impuestos.add(impuesto);
                }
                
                
                List<List<String>> compensaciones = new ArrayList<>();
                //<codigo>"+compensacion.get(0)+"</codigo>" +
                //<tarifa>"+compensacion.get(0)+"</tarifa>" +
                //<valor>"+compensacion.get(0)+"</valor>" +
                
                List<List<String[]>> pagos = new ArrayList<>();
                List<String[]> pago = new ArrayList<>();
                pago.add(new String[]{
                "01",//<formaPago>"+pag[0]+"</formaPago>" +
                cabDB.get(7),//<total>"+pag[1]+"</total>" +
                "0",//<plazo>"+pag[2]+"</plazo>" +
                "dias"//<unidadTiempo>"+pag[3]+"</unidadTiempo>" +
                });
                pagos.add(pago);

                List<List<String>> motivos = new ArrayList<>();
                List<String> motivo = new ArrayList<>();
                motivo.add(cabDB.get(8));//<razon>"+motivo.get(0)+"</razon>" +
                motivo.add(cabDB.get(7));//<valor>"+motivo.get(1)+"</valor>" +
                motivos.add(motivo);
                
                xmlNotaDebito xmlNDB = new xmlNotaDebito();
                String xml = xmlNDB.getNotaDebito(cab, impuestos, compensaciones, pagos, motivos);
//                System.out.println("xml:"+xml);
                crearXml(xml, path+"\\GENERADOS\\"+claveAcceso+".xml");
                escribirBase(cabDB.get(9));
            }

        }else if(TrInvTipo.equals("NCR")){
            
            String qNCR = "Select Convert(varchar, a.DctoFechaEmision, 103) FechaEmision," +
            "Case When PerDocumento='R' Then '04' " +
            "     When PerDocumento='C' Then '05' " +
            "     When PerDocumento='P' Then '06' " +
            "     Else '07' END Documento," +
            "CASE When PerFacturar='E' Then PerApellidos+' '+PerNombres" +
            "     When PerFacturar='P' Then (Select RepPadre From "+baseGE+"..Representantes g" +
                    //And a.PerCodigo = '00004689'
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='M' Then (Select RepMadre From "+baseGE+"..Representantes g" +
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='O' Then (Select RepRepresentante From "+baseGE+"..Representantes g" +
            "				Where a.TipECodigo = '"+IECabTipo+"'  " +
            "				And a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else '' End Nombres," +
            "Case When PerFacturar='E' Then CedulaRUC" +
            "     When PerFacturar='P' Then (Select REPCedulaRUC1 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='M' Then (Select REPCedulaRUC2 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     When PerFacturar='O' Then (Select REPCedulaRUC3 From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.TipECodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else '' End CedulaRUC," +
            "" +
            "DctoCliente," +
            "Convert(varchar, d.IECabFecha, 103) FechaEmisionDocModificado,DctoNoImponible+DctoImponible TotalSinImpuesto," +
            "DctoNoImponible+DctoImponible+DctoIVA total,Substring(DctoDescripcion,5,120) CabDescripcion," +
            "Case When PerFacturar='O' Then (Select RepCorreo From "+baseGE+"..Representantes G" +
            "				Where a.TipECodigo = c.PerCodigo And a.PerCodigo = c.PerCodigo" +
            "				And c.PerCodigo = g.TipECodigo And c.PerCodigo = g.PerCodigo)" +
            "     Else PerMail End PerMail,DetDescripcion,1,DetValor,DetValor " +
            ", DFDImpuesto, DctoNoImponible sub0, DctoImponible sub12, DctoIVA iva " +
            "From "+baseVF+"..Documentos a,"+baseVF+"..DetalleDcmtos b,"+baseGE+"..Personas c,"+baseVF+"..IECabecera d, "+baseVF+"..DetFactDev e " +
            "Where a.BodCodigo="+BodCodigo+" And A.TrCliDocumento='NCR' And A.DctoNumero="+IECabNumero+" And A.DctoTipo='"+IECabTipo+"' And a.DctoEsAnulado = 'N' " +
            "And a.TipEcodigo = c.TipECodigo And a.PerCodigo = c.PerCodigo " +
            "And a.BodCodigo = b.BodCodigo And a.TrCliDocumento = b.TrCliDocumento " +
            "And a.DctoNumero = b.DctoNumero And a.DctoTipo = b.DctoTipo " +
            "And d.BodCodigo = 1 And d.TrInvTipo = 'VTA' " +
            "And d.IECabNumero = (select IECabNumero from "+baseVF+"..relacionSRI s where s.secuencialSRI=Convert(Numeric(9,0),Right(DctoCliente,9))) " +
            "And d.IECabTipo = '"+IECabTipo+"' " +
            "And e.BodCodigo = 1 And e.TrInvTipo = 'VTA' " +
            "And e.IECabNumero = (select IECabNumero from "+baseVF+"..relacionSRI s where s.secuencialSRI=Convert(Numeric(9,0),Right(DctoCliente,9))) " +
            "And e.IECabTipo = '"+IECabTipo+"' and codcuenta <> '211324'";
            //"AND D.BodCodigo="+BodCodigo+" AND D.TrInvTipo='"+TrInvTipo+"' AND D.IECabNumero="+IECabNumero+" AND D.IECabTipo='"+IECabTipo+"' " +
//            System.out.println("qNCR:"+qNCR);
            datosConsulta dcons = new datosConsulta();
            List<List<String>> filas = dcons.buscador(qNCR, 18);
            
            String obligadoConta = "SI";
            if(this.baseGE.equals("geBG")){
                obligadoConta = "NO";
            }
            String fechaDoc = "";
            if(filas.size()>0){
                fechaDoc = filas.get(0).get(0);
            }
            secuencial=intToStringFormat(secuenciaNum,9);
            claveAcceso=claveAcceso(fechaDoc,codDoc,ruc,ambiente,secuencial,tipoEmision);
            
            List<String> cab = new ArrayList<>();
            cab.add(ambiente);//<ambiente>"+cab.get(0)+"</ambiente>" +
            cab.add("1");//<tipoEmision>"+cab.get(1)+"</tipoEmision>" +
            cab.add(razonSocial);//<razonSocial>"+cab.get(2)+"</razonSocial>" +
            cab.add(razonSocial);//<nombreComercial>"+cab.get(3)+"</nombreComercial>" +
            cab.add(ruc);//<ruc>"+cab.get(4)+"</ruc>" +
            cab.add(claveAcceso);//<claveAcceso>"+cab.get(5)+"</claveAcceso>" +
            cab.add(codDoc);//<codDoc>"+cab.get(6)+"</codDoc>" +
            cab.add(establecimiento);//<estab>"+cab.get(7)+"</estab>" +
            cab.add(puntoEmision);//<ptoEmi>"+cab.get(8)+"</ptoEmi>" +
            cab.add(secuencial);//<secuencial>"+cab.get(9)+"</secuencial>" +
            cab.add(dirMatriz);//<dirMatriz>"+cab.get(10)+"</dirMatriz>" +
            
            
            List<String> cabDB = new ArrayList<>();
            List<List<String>> detalles = new ArrayList<>();
//            Double nCrTotal = 0.0;
            if(filas.size()>0){
                for (int i = 0; i <filas.size(); i++) {
                    List<String> detalle = new ArrayList<>();
                    for (int j = 0; j <filas.get(i).size(); j++) {
                        if(i==0){
                            cabDB.add(filas.get(0).get(j));
                        }
                    }
                    
//                    nCrTotal = nCrTotal+Double.parseDouble(filas.get(i).get(12));
                    detalle.add(filas.get(i).get(10));//descripcion
                    detalle.add(filas.get(i).get(11).replaceAll(",", ""));//cantidad
                    detalle.add(filas.get(i).get(12).replaceAll(",", ""));//precio
                    detalle.add(filas.get(i).get(13).replaceAll(",", ""));//subtotal
                    detalle.add(filas.get(i).get(14).replaceAll(",", ""));//iva
                    detalles.add(detalle);
                }
                Double sub0 = Double.parseDouble(cabDB.get(15).replaceAll(",", ""));
                Double sub12 = Double.parseDouble(cabDB.get(16).replaceAll(",", ""));
                Double iva = Double.parseDouble(cabDB.get(17).replaceAll(",", ""));
//                Double tot = sub0 + sub12 + iva;
                cab.add(cabDB.get(0));//<fechaEmision>"+cab.get(11)+"</fechaEmision>" +
                cab.add(dirMatriz);//<dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>" +
                cab.add(cabDB.get(1));//<tipoIdentificacionComprador>"+cab.get(13)+"</tipoIdentificacionComprador>" +
                cab.add(cabDB.get(2));//<razonSocialComprador>"+cab.get(14)+"</razonSocialComprador>" +
                cab.add(cabDB.get(3));//<identificacionComprador>"+cab.get(15)+"</identificacionComprador>" +
                cab.add("000");//<contribuyenteEspecial>"+cab.get(16)+"</contribuyenteEspecial>" +
                cab.add(obligadoConta);//<obligadoContabilidad>"+cab.get(17)+"</obligadoContabilidad>" +
                cab.add("");//<rise>rise"+cab.get(18)+"</rise>" +
                cab.add("01");//<codDocModificado>"+cab.get(19)+"</codDocModificado>" +
                cab.add(cabDB.get(4));//<numDocModificado>"+cab.get(20)+"</numDocModificado>" +
                cab.add(cabDB.get(5));//<fechaEmisionDocSustento>"+cab.get(21)+"</fechaEmisionDocSustento>" +
                cab.add(cabDB.get(6).replaceAll(",", ""));//<totalSinImpuestos>"+cab.get(22)+"</totalSinImpuestos>" +
                cab.add(cabDB.get(7).replaceAll(",", ""));//"<valorModificacion>"+cab.get(23)+"</valorModificacion>" +
                cab.add("DOLAR");//<moneda>"+cab.get(24)+"</moneda>" +
                cab.add(cabDB.get(8));//<motivo>"+cab.get(25)+"</motivo>" +

                cab.add("emailCliente");//<campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>" +
                cab.add(cabDB.get(9));//<campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>" +


                List<List<String>> compensaciones = new ArrayList<>();
                //<codigo>"+compensacion.get(0)+"</codigo>" +
                //<tarifa>"+compensacion.get(0)+"</tarifa>" +
                //<valor>"+compensacion.get(0)+"</valor>" +

                List<List<String>> impuestos = new ArrayList<>();
                List<String> impuesto = new ArrayList<>();
                if(sub0>0){
                    impuesto = new ArrayList<>();
                    impuesto.add("2");//<codigo>"+impuesto.get(0)+"</codigo>" +
                    impuesto.add("0");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add("0.0");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add(sub0+"");//<baseImponible>"+impuesto.get(3)+"</baseImponible>" +
                    impuesto.add("0");//<valor>"+impuesto.get(4)+"</valor>" +
                    impuestos.add(impuesto);
                }
                if(sub12>0){
                    impuesto = new ArrayList<>();
                    impuesto.add("2");//<codigo>"+impuesto.get(0)+"</codigo>" +
                    impuesto.add("2");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add("12.0");//<codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>" +
                    impuesto.add(sub12+"");//<baseImponible>"+impuesto.get(3)+"</baseImponible>" +
                    impuesto.add(iva+"");//<valor>"+impuesto.get(4)+"</valor>" +
                    impuestos.add(impuesto);
                }
                xmlNotaCredito xmlNC = new xmlNotaCredito();
                String xml = xmlNC.getNotaCredito(cab, impuestos, compensaciones, detalles);
                System.out.println("xml:"+xml);
                crearXml(xml, path+"\\GENERADOS\\"+claveAcceso+".xml");
                escribirBase(cabDB.get(9));
            }
        }
        return true;
    }
}
