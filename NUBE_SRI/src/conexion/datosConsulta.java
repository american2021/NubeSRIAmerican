package conexion;

import com.sun.corba.se.spi.orbutil.fsm.GuardBase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class datosConsulta {
        public String comillas(String cad){
//        System.out.println("cad: "+cad);
        for(int i=cad.length()-1;i>=0;i--){
//            System.out.println(cad.substring(i, i+1)+"    "+cad.substring(0, i)+"()"+cad.substring(i+1));
            if(cad.substring(i, i+1).equals("'")){
                cad=cad.substring(0, i)+"''"+cad.substring(i+1);
            }
        }
//        System.out.println("cad: "+cad);
        return cad;
    }
    public List<String> unafila(String query, int n){
        //int n = consId("SELECT count_column('"+comillas(query)+"') AS a FROM dual");
        conexion con=null;
        Connection conn=null;
        System.out.println("una fila:"+query);
        List<String> l_tab=new ArrayList<String>();
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                for(int i=1;i<n;i++){
                    if(rs.getString(i)==null){
                        l_tab.add("");
                    }else{
                        l_tab.add(rs.getString(i));
                    }
                }
            }
            rs.close();st.close();
        } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l_tab;
    }
    public List<String> unacolumna(String query){
        conexion con=null;
        Connection conn=null;
        List<String> l_tab=new ArrayList<String>();
        try {
            con = new conexion();
            conn=con.getCon();
//            System.out.println("una columna:"+query);
//            try {
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                while(rs.next()){
                    l_tab.add(rs.getString(1));
                }
//            }
        } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l_tab;
    }
    public Double consValorSimple(String query){//Ingreso:Query conection    Salida:"a" integer
//        System.out.println("condId: "+query);
        conexion con=null;
        Connection conn=null;
        Double id=0.0;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query);
                rs.next();
                id = rs.getDouble(1);
//            }
//            System.out.println("consId:"+cad+"  "+id);
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
            return 0.0;
        }catch(NullPointerException np){
            return 0.0;
        }finally {
            cerrar(con, conn);
        }
        
    }
    public int consId(String query){//Ingreso:Query conection    Salida:"a" integer
//        System.out.println("condId: "+query);
        conexion con=null;
        Connection conn=null;
        int id=0;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            id = rs.getInt("a");
            rs.close();st.close();
//            System.out.println("consId:"+cad+"  "+id);
            return id;
        } catch (SQLException ex) {
            System.out.println("error codId: "+query);
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }catch(NullPointerException np){
            return -1;
        }finally {
            cerrar(con, conn);
        }
        
    }
    public String consUnDato(String query){//Ingreso:Query conection    Salida:"a" String
        conexion con=null;
        Connection conn=null;
        String id="";
//        System.out.println("consUnDato:"+query);
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            id = rs.getString(1);
            rs.close();st.close();
        } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return id;
    }
    public List<List<String>> consListList(String query, String tab){
        conexion con=null;
        Connection conn=null;
        List<List<String>> l=new ArrayList<List<String>>();
        List<String> l_aux;
        int cols = consNumCol(tab);
        if(cols>0)
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                l_aux=new ArrayList<String>();
                for(int i=1;i<=cols;i++){
                    if(rs.getString(i)==null){
                        l_aux.add("");
                    }else{
                        l_aux.add(rs.getString(i));
                    }
//                    System.out.println(rs.getString(i));
                }
                l.add(l_aux);
            }
            rs.close();st.close();
        }catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l;
    }
    public String where(String tab, String busca, List<String> l, List<String> ad){
        
        String w="", adiciona="";
        if(ad!=null && ad.size()>0){
            w="Select * from "+tab+", "+ad.get(1)+" where (";
            adiciona=" and "+ad.get(1)+"."+ad.get(3)+"="+ad.get(4)+" and UPPER("+ad.get(1)+"."+ad.get(2)+") like '%"+ad.get(0)+"%'";
            //"+ad.get(1)+"
        }else{
            w="Select * from "+tab+" where (";
        }
        for(int i=0;i<l.size();i++){
            if(i>0){
                w = w+" or";
            }
            w = w+" UPPER("+l.get(i)+") LIKE '%"+busca+"%'";
        }
        w = w+")"+adiciona;
//        System.out.println("w:"+w);
        return w;
    }
    public List<List<String>> consultarMatriz(String query){
        int cols=consId("SELECT count_column('"+comillas(query)+"') AS a FROM dual");
        conexion con=null;
        Connection conn=null;
        List<List<String>> l=new ArrayList<List<String>>();
        List<String> l_aux;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                l_aux=new ArrayList<String>();
                for(int i=1;i<=cols;i++){
                    l_aux.add(rs.getString(i));
                }
                l.add(l_aux);
            }
            rs.close();st.close();
        }catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l;
    }
    
    
    public List<List<String>> buscador(String query, int cols){
        conexion con=null;
        Connection conn=null;
        List<List<String>> l=new ArrayList<List<String>>();
        List<String> l_aux;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
//            st.execute("set character set utf8");
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                l_aux=new ArrayList<String>();
                for(int i=1;i<=cols;i++){
                    l_aux.add(rs.getString(i));
                }
                l.add(l_aux);
            }
            rs.close();st.close();
        }catch (SQLException ex) {
            System.out.println("error de query:"+query);
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l;
    }
    
    
    
    public List<List<String>> busquedaConCol(String query, List<String> colum){
        conexion con=null;
        Connection conn=null;
        List<List<String>> l=new ArrayList<List<String>>();
        List<String> l_aux;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                l_aux=new ArrayList<String>();
                for(int i=0;i<colum.size();i++){
                    l_aux.add(rs.getString(colum.get(i)));
                }
                l.add(l_aux);
            }
            rs.close();st.close();
        }catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return l;
    }
    public int consNumCol(String tabla){
        conexion con=null;
        Connection conn=null;
        int k=0;
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select count(0) as a from USER_TAB_COLUMNS where UPPER(table_name) like '"+tabla.toUpperCase()+"'");
            rs.next();
            k=rs.getInt("a");
            rs.close();st.close();
            } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        return k;
    }
    public List<Integer> noNull1(String tab){
        conexion con=null;
        Connection conn=null;
        List<Integer> ln=new ArrayList<Integer>();
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("select (COLUMN_ID-1) AS A from ALL_TAB_COLUMNS where upper(table_name) = upper('"+tab+"') and upper(owner) = upper('encuesta') and NULLABLE = 'N' order by column_id");
                while(rs.next()){
                    ln.add(rs.getInt(1));
                }
//            }
        } catch (SQLException ex) {
//            System.out.println("conn:"+conn);
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally {
            cerrar(con, conn);
        }
        return ln;
    }
    public String[] combobox(String tab, int codigo, int combo){
        conexion con=null;
        Connection conn=null;
        List<String> f;
        List<List<String>> l_tab=new ArrayList<List<String>>();
        try {
            con = new conexion();
            conn=con.getCon();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from "+tab+" order by 1");
            while(rs.next()){
                f=new ArrayList<String>();
                f.add(rs.getString(codigo));
                f.add(rs.getString(combo));
                l_tab.add(f);
            }
            rs.close();st.close();
        } catch (SQLException ex) {
            Logger.getLogger(datosConsulta.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar(con, conn);
        }
        String[] cb = new String[l_tab.size()];
        for(int i=0;i<l_tab.size();i++){
            cb[i]=l_tab.get(i).get(0)+"-"+l_tab.get(i).get(1);
        }
        return cb;
    }
    public void cerrar(conexion con, Connection conn){
        if(con!=null){
            if(con.CerrarConexion()==1)System.out.println("con no cerrado");
            try {
                if(conn != null){
                    conn.close();
                }else{
                    System.out.println("conn NO cerrado");
                }
            }catch (SQLException ex) {
                System.out.println("Error SQLException al cerrar Connection");
            }
        }
    }
//    public String comillas(String cad){
////        System.out.println("cad: "+cad);
//        for(int i=0;i<cad.length();i++){
////            System.out.println(cad.substring(i, i+1)+"    "+cad.substring(0, i)+"()"+cad.substring(i+1));
//            if(cad.substring(i, i+1).equals("'")){
//                cad=cad.substring(0, i)+"''"+cad.substring(i+1);
//                i=i+2;
//            }
//        }
////        System.out.println("cad: "+cad);
//        return cad;
//    }
}

