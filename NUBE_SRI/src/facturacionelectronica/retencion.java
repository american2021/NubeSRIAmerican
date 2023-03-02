package facturacionelectronica;

import conexion.datosConsulta;
import conexion.datosGuarda;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class retencion {
    
    String ambiente = "2";
    String tipoEmision = "1";
    String razonSocial = "";
    String ruc = "";
    String claveAcceso = "";
    String codDoc = "01";
    String establecimiento="";
    String puntoEmision="";
    String secuencial="";
    String Direccion="";
    
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
        String txt = fecha+tipo+ruc+ambiente+establecimiento+"100"+secuencia+secuencial(codigoNumerico, 8)+tipEmi;
        return txt+modulo11(txt);
    }
    public String secuencial(int num, int dig){
        String txt=num+"";
        int leng = txt.length();
        if(leng<dig){
            for (int i = 0; i < (dig-leng); i++) {
                txt = "0"+txt;
            }
        }
        return txt;
    }
    public void docList(List<String> ltemp, List<List<String>> ldet){
        if(ltemp.size()>0){
            secuencial=secuencial(secuenciaNum,9);
            claveAcceso=claveAcceso(ltemp.get(2),codDoc,ruc,ambiente,secuencial,tipoEmision);
            xmlRetencion2 xml = new xmlRetencion2();
            xml.setAmbiente(ltemp.get(0));
            xml.setTipoEmision(ltemp.get(1));
            xml.setRazonSocial(ltemp.get(2));
            xml.setNombreComercial(ltemp.get(3));
            xml.setRuc(ltemp.get(4));
            xml.setClaveAcceso(claveAcceso);
            xml.setClaveAcceso(ltemp.get(6));
            xml.setCodDoc(ltemp.get(7));
            xml.setEstablecimiento(ltemp.get(8));
            xml.setPuntoEmision(ltemp.get(9));
            xml.setSecuencial(secuencial);
            xml.setMatriz(ltemp.get(11));

            xml.setFecha(ltemp.get(12));
            xml.setDireccion(ltemp.get(13));
            xml.setContribEsp(ltemp.get(14));
            xml.setObligCont(ltemp.get(15));
            xml.setCliTipId(ltemp.get(16));
            xml.setCliRazonSocial(ltemp.get(17));
            xml.setCliId(ltemp.get(18));
            xml.setCliPerFiscal(ltemp.get(19));

            xml.setAdicional1(ltemp.get(20));
            xml.setAdicional2(ltemp.get(21));
            xml.setAdicional3(ltemp.get(22));
            
            String xmlDetalle = "";
            for (int i = 0; i < ldet.size(); i++) {
                xmlRetencion2.detalle detalle = new xmlRetencion2.detalle();
                detalle.setImpCod(ldet.get(i).get(0));
                detalle.setImpCodRet(ldet.get(i).get(1));
                detalle.setImpBaseImp(ldet.get(i).get(2));
                detalle.setImpValorRet(ldet.get(i).get(3));
                detalle.setImpCodDocSus(ldet.get(i).get(4));
                detalle.setImpNumDocSus(ldet.get(i).get(5));
                detalle.setImpFecDocSus(ldet.get(i).get(6));
                xmlDetalle = xmlDetalle + detalle.getDetalleXml();
            }
            xml.setImpuestos(xmlDetalle);
//            System.out.println("path:"+path+"\\GENERADOS\\"+claveAcceso+".xml");
            crearXml(xml.generarXML(), path+"\\GENERADOS\\"+claveAcceso+".xml");
            
            
//            String query = "SELECT MAX(ESTADOEMISION), MAX(SECUENCIALSRI), MAX(CODIGONUMERICO) "
            String query = "SELECT MAX(ESTADOEMISION) "
                    + "FROM "+baseVF+"..RELACIONSRI WHERE BODCODIGO="+BodCodigo+" AND TRINVTIPO='"+TrInvTipo+"' AND IECABNUMERO="+IECabNumero+" AND IECABTIPO='"+IECabTipo+"'";
//            System.out.println("q:"+query);
            datosConsulta dcons = new datosConsulta();
            List<String> estadoEmision = dcons.buscador(query, 1).get(0);
//            System.out.println("est:"+estadoEmision);
            
            if(estadoEmision.get(0)==null){
                
                datosGuarda dg = new datosGuarda();
                query = "INSERT INTO "+baseVF+"..RELACIONSRI VALUES("+BodCodigo+",'"+TrInvTipo+"',"+IECabNumero+",'"+IECabTipo+"',"+secuencial+","+codigoNumerico+",NULL,NULL,'"+claveAcceso+"','')";
//                System.out.println("q:"+query);
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
//        System.out.println("generado: "+claveAcceso);
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
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(xml);
            } catch (IOException ex) {
                Logger.getLogger(retencion.class.getName()).log(Level.SEVERE, null, ex);
            }
//            }
            if(bw!=null){
                bw.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(retencion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void crear(String BodCodigo, String TrInvTipo, String IECabNumero, String IECabTipo, String baseVF, String baseGE, int secuencial, int codigoNumerico, String path, String ambiente,String razonSocial,String ruc,String codDoc,String establecimiento,String puntoEmision,String Direccion){
        this.ambiente=ambiente;
        this.razonSocial=razonSocial;
        this.ruc=ruc;
        this.codDoc=codDoc;
        this.establecimiento=establecimiento;
        this.puntoEmision=puntoEmision;
        this.Direccion=Direccion;
        
        this.secuenciaNum=secuencial;
        this.codigoNumerico=codigoNumerico;
        this.BodCodigo = BodCodigo;
        this.TrInvTipo=TrInvTipo;
        this.IECabNumero=IECabNumero;
        this.IECabTipo=IECabTipo;
        this.baseVF=baseVF;
        this.baseGE=baseGE;
        this.path=path;
//        System.out.println("llega 2");
//        datosConsulta dcons = new datosConsulta();
//        String queryEmpresa = "SELECT * FROM ";
//        System.out.println("query:"+queryEmpresa);
//        fempresa = dcons.buscador(queryEmpresa, 4).get(0);
//        
//        ruc = fempresa.get(0);
//        razonSocial = fempresa.get(1);
//        dirMatriz = fempresa.get(3);
        
        
        String query = "SELECT "
                + "A.IECABNUMERO"
                + ", PERAPELLIDOS+' 'PERNOMBRES"
                + ",convert(varchar, A.IECABFECHA, 103),"
                + "(case when PERDOCUMENTO='R' then '04' when PERDOCUMENTO='C' then '05' when PERDOCUMENTO='P' then '06' else '07' end),"
                + "CEDULARUC"
                + ",PERMAIL"
                + ",B.PROCODIGO"
                + ",PRONOMBRE"
                + ",IEDETCANTIDAD"
                + ",DFDPRECIOUNITARIO, "
                + "CFDDESNOIMPONIBLE"
                + ",IECABTOTAL " +
            "FROM "+baseVF+"..IECABECERA A, "+baseVF+"..IEDETALLE B, "+baseGE+"..PERSONAS C, "+baseVF+"..CABFACTDEV D, "+baseVF+"..DETFACTDEV E, "+baseVF+"..PRODUCTOS F " +
            "WHERE A.TRINVTIPO = 'VTA' " +
            "AND D.BodCodigo="+BodCodigo+" AND D.TrInvTipo='"+TrInvTipo+"' AND D.IECabNumero="+IECabNumero+" AND D.IECabTipo='"+IECabTipo+"' " +
//            "AND D.BodCodigo=1 AND D.TrInvTipo='VTA' AND D.IECabNumero=11901 AND D.IECabTipo='I' " +
            //"AND A.IECABFECHA BETWEEN '28/09/2018 00:00:00' AND '28/09/2018 23:59:56' " +
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
        System.out.println("query:"+query);
        datosConsulta dcons = new datosConsulta();
        List<String> cabecera = dcons.unafila(query, 12);
        List<List<String>> detalle = dcons.buscador(query, 12);
        
        docList(cabecera, detalle);
//        List<List<String>> ltemp = new ArrayList<>();
//        List<List<String>> ptemp = new ArrayList<>();
//        for (int i = 0; i < fcab.size(); i++){
//            if((i+1)<fcab.size()){
//                if(fcab.get(i+1).get(0).equals(fcab.get(i).get(0))){
//                    ltemp.add(fcab.get(i));
//                    try{
//                        ptemp.add(fpagos.get(i));
//                    }catch(java.lang.IndexOutOfBoundsException e){}
//                }else{
//                    ltemp.add(fcab.get(i));
//                    if(fpagos.size()>0){ptemp.add(fpagos.get(i));}
//                    docList(ltemp, ptemp);
//                    ltemp = new ArrayList<>();
//                    ptemp = new ArrayList<>();
//                }
//            }else{
//                ltemp.add(fcab.get(i));
//                try{
//                    ptemp.add(fpagos.get(i));
//                }catch(java.lang.IndexOutOfBoundsException e){}
//            }
//        }
//        if(ltemp.size()>0){
//            docList(ltemp, ptemp);
//        }
    }
}
