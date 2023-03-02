
package conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class datosGuarda {
    
    String query="";
    String qaux="";
    String td;
    conexion con = null;
    Connection conn;
    int estado=0;
    boolean bmod;
//    public void guardaUnaFila(String tab, int id, List<String> datos){
//        con = new conexion();
//        query="select COLUMN_NAME from ALL_TAB_COLUMNS where upper(table_name) = upper('"+tab+"') and upper(owner) = upper('"+con.nomBase+"') order by column_id";
//        datosConsulta cons=new datosConsulta();
//        List<String> lnom = cons.unacolumna(query);
//        query="insert into "+tab+" values ("+id + cadenaInsert(datos,lnom)+", sysdate)";
//        System.out.println(query);
//        guardarBase(query);
//    }
    public void guardaSimple(String tab, int id, List<String> datos){
        con = new conexion();
        query="select COLUMN_NAME from ALL_TAB_COLUMNS where upper(table_name) = upper('"+tab+"') and upper(owner) = upper('encuesta') order by column_id";
        datosConsulta cons=new datosConsulta();
        List<String> lnom = cons.unacolumna(query);
        query="insert into "+tab+" values ("+id + cadenaInsert(datos,lnom)+", sysdate)";
        System.out.println(query);
        guardarBase(query);
    }
    public void updateSimple(String tab, int id, List<String> datos){
        con = new conexion();
        query="select COLUMN_NAME from ALL_TAB_COLUMNS where upper(table_name) = upper('"+tab+"') and upper(owner) = upper('encuesta') order by column_id";
        datosConsulta cons=new datosConsulta();
        List<String> lnom = cons.unacolumna(query);
        //query="insert into "+tab+" values ("+id + cadenaInsert(datos,lnom)+", sysdate)";
        query="Update "+tab+" set "+cadenaUpdate(datos, lnom, tab)+"FEC_ACTUALIZA = sysdate WHERE "+lnom.get(0)+" = "+id;
        System.out.println(query);
        guardarBase(query);
    }
    
    public String cadenaUpdate(List<String> l, List<String> lnom, String tab){
        qaux="";
        for(int j=1;j<lnom.size()-1;j++){
            td=lnom.get(j).substring(0, 3).toUpperCase();
            String valaux=l.get(j);
//            if(td.equals("COD") && valaux.equals("")){
//                valaux="null";
//            }
            if(td.equals("NUM") || td.equals("VAL")){
                if(valaux.length()>0){
                    qaux=qaux+lnom.get(j)+" = "+valaux+", ";
                }else{
                    qaux=qaux+lnom.get(j)+" = 0, ";
                }
            }else if(td.equals("FEC")){
                qaux=qaux+lnom.get(j)+" = TO_DATE('"+valaux+"', 'dd/mm/yyyy'), ";
            }else{
                qaux=qaux+lnom.get(j)+" = '"+valaux+"', ";
            }
        }
        return qaux;
    }
    public String cadenaInsert(List<String> l, List<String> lnom){
        qaux="";
        String valaux="";
        for(int j=1;j<lnom.size()-1;j++){
            td=lnom.get(j).substring(0, 3).toUpperCase();
//            System.out.println(lnom.get(j)+"="+l.get(j));
            valaux=l.get(j);
//            if(td.equals("NUM") && valaux.equals("")){
//                valaux="null";
//            }
//            if(valaux==null)valaux="";
            if(td.equals("NUM") || td.equals("VAL")){
                if(valaux.length()>0){
                    qaux=qaux+", "+valaux;
                }else{
                    qaux=qaux+", 0";
                }
            }else if(td.equals("FEC")){
                qaux=qaux+", TO_DATE('"+valaux+"', 'dd/mm/yyyy')";
            }else{
                qaux=qaux+", '"+valaux+"'";
            }
        }
        return qaux;
    }
    public boolean noNulos(List<String> ldFinal, List<Integer> ln){
        boolean bn=true;
//        for(int i=1;i<ln.size();i++){
//            System.out.println(i+" nulos:"+ln.get(i));
//        }
//        for(int i=1;i<ldFinal.size();i++){
//            System.out.println(i+" lfnulos:"+ldFinal.get(i));
//        }
        for(int i=1;i<ln.size();i++){
//            System.out.println(1+" "+ldFinal.get(ln.get(i)));
            if(ldFinal.get(ln.get(i))==null || ldFinal.get(ln.get(i)).equals("")){
                bn=false;
            }
        }
        return bn;
    }
    public void guardarBase(String query){
//        System.out.println(query);
        try {
            con = new conexion();
            conn=con.getCon();
            PreparedStatement ps=conn.prepareStatement(query);
            ps.execute();
            conn.close();
            con.CerrarConexion();
        }catch (SQLException ex) {
            System.out.println("***** error al guardar: "+query);
            Logger.getLogger(datosGuarda.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            con.CerrarConexion();
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(datosGuarda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
