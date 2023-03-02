package facturacionelectronica;

import conexion.datosConsulta;
import java.util.List;
import javax.swing.JOptionPane;

public class FacturacionElectronica {
    
    public static void main(String[] args) {
        
    }
    public Object[] correr(String[] args) {
        Object[] resp = new Object[2];
        resp[0]=false;
        resp[1]="";
        if(args.length==0){
//            System.out.println("es null");
//            String[] args1 = {"1","VTA","12813","I","vfIA","geIA"};
//            String[] args1 = {"1","VTA","15562","I","vfIA","geIA"};
//            args=args1;
        }
        String BodCodigo="";
        String TrInvTipo="";
        String IECabNumero="";//12726-29
        String IECabTipo="";
        String baseVF="";//RETENCIONES CO
        String baseGE="";
        String carpeta = "";
        switch (args[4]) {
            case "vfIA":
                carpeta = "Instituto";
                break;
            case "vfCO":
                carpeta = "Corel";
                break;
            case "vfBG":
                carpeta = "BillGates";
                break;
            case "vfCA":
                carpeta = "Coradac";
                break;
            default:
                carpeta = "Instituto";
                break;
        }
        
//        String path = "C:\\COMPROBANTES_ELECTRONICOS\\DOCS\\"+carpeta;
        String path = "\\\\HOME\\InstaladorAmerican\\Batch\\"+carpeta;
        int secuencial = 9;
        int codigoNumerico = 9;
        try{
            BodCodigo=args[0];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        try{
            TrInvTipo=args[1];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        try{
            IECabNumero=args[2];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        try{
            IECabTipo=args[3];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        try{
            baseVF=args[4];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        try{
            baseGE=args[5];
        }catch(java.lang.ArrayIndexOutOfBoundsException ex){}
        
        
        String queryGenerados = "SELECT "
                    + "A.NumeroAutorizacion a "
                    + "FROM "+baseVF+"..RELACIONSRI A "
                    + "WHERE A.IECABNUMERO = "+IECabNumero+" AND A.TRINVTIPO = '"+TrInvTipo+"' ";
        datosConsulta dcons = new datosConsulta();
//        System.out.println("queryGenerados:"+queryGenerados);
        List<String> consul = dcons.unafila(queryGenerados,1);
        String secAux="",numAux="";
        if(consul.size()>0){
            if(consul.get(0).length()>36){
                resp[0]=false;
                resp[1]="Secuencial en USO ya Autorizado.";
                return resp;
            }else{
                secAux = IECabNumero;
                numAux = IECabNumero;
            }
        }else{
            String query = "select SecNumero, SecNumero from "+baseVF+"..SecuenciaSRI WHERE SecTipo='"+TrInvTipo+"'";
    //        datosConsulta dcons = new datosConsulta();
            List<String> secuencias = dcons.buscador(query, 2).get(0);
            secAux = secuencias.get(0);
            numAux = secuencias.get(1);

            secAux = secAux.substring(0,secAux.length()-3);
            secAux = secAux.replaceAll(",", "");

            numAux = numAux.substring(0,numAux.length()-3);
            numAux = numAux.replaceAll(",", "");
        }
        
        secuencial = Integer.parseInt(secAux);
        codigoNumerico = Integer.parseInt(numAux);
        
        System.out.println("secuencial:"+secuencial+"  codigoNumerico:"+codigoNumerico);
        
        String queryEmpresa = "Select CedulaRUC RUC,PerNombres razonSocial,PerDireccion Direccion,PerInterseccion establecimiento "
                + "From "+baseGE+"..Personas "
                + "Where TipECodigo = 'A'";
//        System.out.println(query);
        List<String> dEmpresa = dcons.buscador(queryEmpresa, 4).get(0);
        String ambiente = args[6];
        String razonSocial = dEmpresa.get(1);
        String ruc = dEmpresa.get(0);
        String codDoc = "";
        String establecimiento = dEmpresa.get(3);
        String puntoEmision = "100";
        String Direccion = dEmpresa.get(2);
//        System.out.println("establec:"+establecimiento);
        switch (TrInvTipo) {
            case "VTA":
                codDoc = "01";
                break;
            case "RET":
                codDoc = "07";
                break;
            case "NDB":
                codDoc = "05";
                break;
            case "NCR":
                codDoc = "04";
                break;
            case "GUI":
                codDoc = "06";
                break;
            default:
                break;
        }
        factura factura = new factura();
        resp[0]=factura.crear(BodCodigo, TrInvTipo, IECabNumero, IECabTipo, baseVF, baseGE, secuencial, codigoNumerico, path,ambiente,razonSocial,ruc,codDoc,establecimiento,puntoEmision,Direccion);;
        return resp;
    }
}
