/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

//import clases.datosConsulta;
//import clases.datosGuarda;
import conexion.datosConsulta;
import conexion.datosGuarda;
import facturacionelectronica.FacturacionElectronica;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import servicios.enviarAutorizacion;
import servicios.enviarRecepcion;
import sri.XAdESBESSignature;

/**
 *
 * @author Jaime
 */
public class NUBE_ELECTRONICA_AMERICAN extends javax.swing.JFrame {
    datosConsulta dcons = new datosConsulta();
    datosGuarda dg = new datosGuarda();
    DefaultTableModel modelo;
    List<List<String>> elements;
    String[] m_gen, m_fir, m_env, m_aut, m_noAut;
    String[] m_gen1 = {}, m_fir1, m_env1, m_aut1, m_noAut1;
//    String url = "C:\\COMPROBANTES_ELECTRONICOS\\";
    String url = "\\\\HOME\\InstaladorAmerican\\Batch\\";
    String pathWeb = "\\\\192.168.1.2\\sricomprobantes\\DOCS\\";
    String empresa = "";
    String claveFirma = "";
    String baseVF = "";
    String baseGE = "";
    String numFac = "";
    String tipDoc = "";
    int bodega = 0;
    String IECabTipo = "";
    int ambiente=2;
    public Component ventana = this;
    
    static Socket socket_conectado;
    static TimerTask timerTask;
    static ServerSocket mi_servicio = null;
    static Timer timer;
//    boolean inicia = true;
    /*
    vfIA":
                carpeta = "Instituto";
                break;
            case "vfCO":
                carpeta = "Corel";
                break;
            case "vfBG":
                carpeta = "BillGates";
                break;
            case "vfCA":
    */
    public void ancho(int c, int d){
//        tabla.getColumnModel().getColumn(c).setMaxWidth(d);
//        tabla.getColumnModel().getColumn(c).setMinWidth(d);
        tabla.getColumnModel().getColumn(c).setPreferredWidth(d);
    }
    public NUBE_ELECTRONICA_AMERICAN(int bodega, String empresa, String claveFirma, String baseVF, String baseGE, String IECabTipo) {
        initComponents();
        this.empresa = empresa;
        this.baseVF = baseVF;
        this.baseGE = baseGE;
        this.claveFirma = claveFirma;
        this.IECabTipo = IECabTipo;
        this.bodega = bodega;
        
        Calendar c2 = new GregorianCalendar();
        jcdDesde.setCalendar(c2);
        jcdHasta.setCalendar(c2);
        instLabel.setText("Usuario: " + empresa);
        
        try {
            BufferedImage myPicture = ImageIO.read(new File(empresa +".JPG"));
            Image tmp = myPicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            
            imageLabel.setIcon(new ImageIcon(dimg));
        } catch (IOException ex) {
            imageLabel.setText("Imagen no disponible");
        }
        
        
        /*ancho(0, 50);
        ancho(1, 40);
        ancho(2, 60);
        ancho(3, 20);
        ancho(4, 60);
        ancho(5, 60);
        ancho(6, 70);
        ancho(7, 70);
        ancho(8, 200);
        ancho(9, 40);
        ancho(10, 40);
        ancho(11, 40);
        ancho(12, 40);
        ancho(14, 60);
        ancho(15, 60);*/
        ancho(13, 100);
        
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                socket();
//            }
//        };
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, 0, 1000000000);

        

        process();
        
        timerTask = new TimerTask() {
        //TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //process();
                //verificarRecepcion();
                generarDocumentos();
                //procesarDatos();
            }

        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 20000);
    }
    
    public void verificarRecepcion(){
        
        for(int i = 0; i < tabla.getRowCount(); i++){
            
            String clave = tabla.getValueAt(i, 8).toString();
            enviarRecepcion r = new enviarRecepcion();
            String respuesta = r.recepcionDocumento(url+empresa+"\\FIRMADOS\\"+clave+"_sign.xml",ambiente);
            if(respuesta.equals("RECIBIDA")){
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET FECHAEMISION=GETDATE() WHERE CLAVEAUTORIZACION='"+clave+"'";
                dg.guardarBase(query);
            }else{
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionRecepcion='"+respuesta+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
                dg.guardarBase(query);
                JOptionPane.showMessageDialog(this, "Error en la recepcion!");
            }
            
        }
        
        
        
        
    }
    
    public void generarDocumentos(){

        
        File f;
        List<String> enviados = new ArrayList<>();
        for(int i = 0; i < tabla.getRowCount(); i++){
            
            String clave = tabla.getValueAt(i, 8).toString();
            numFac = tabla.getValueAt(i, 2).toString();
            numFac=numFac.replace(".","");
            numFac=numFac.replaceAll("\\s","");
            tipDoc = tabla.getValueAt(i, 1).toString();
            f = new File(url+"\\"+this.empresa+"\\AUTORIZADOS\\RIDE\\"+clave+".pdf");
            System.out.println("e------------- "+f);
            System.out.println(url+"\\"+this.empresa+"\\AUTORIZADOS\\RIDE\\"+clave+".pdf");
            if (tabla.getValueAt(i, 10).toString().equalsIgnoreCase("ok") && (!tabla.getValueAt(i, 12).toString().equalsIgnoreCase("ok") || !f.exists())){
                   
                System.out.println("No existe el archivo");
                List<String> respuesta = enviarAutorizacion.autorizacionFactura(clave, ambiente);
                if(respuesta.size()==5){
                    if(respuesta.get(0).equals("AUTORIZADO")){
                        String query = "UPDATE "+baseVF+"..RELACIONSRI SET FECHAAUTORIZACION='"+respuesta.get(2)+"', NUMEROAUTORIZACION = '"+respuesta.get(1)+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
                        dg.guardarBase(query);
                        
                    }else{
                        String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionAutorizacion='"+respuesta+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
                        dg.guardarBase(query);

                    }
                }  
                if(respuesta.get(0).equals("AUTORIZADO")){

                    this.setEnabled(false);
                    crearXml(respuesta.get(4), url+empresa+"\\AUTORIZADOS\\"+clave+".xml");
                
                    Thread.currentThread();
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Inicio generación");
                    generarPDF pdf = new generarPDF();
                    pdf.generar(clave, url+empresa, baseVF, baseGE, numFac, tipDoc, url, pathWeb, empresa);        
                    System.out.println("Finalizo Generacion");
                    Thread.currentThread();
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    enviarCorreo enviarcorreo = new enviarCorreo();
                    enviarcorreo.enviarCorreo(url+empresa+"\\AUTORIZADOS\\", clave, tabla.getValueAt(i, 13).toString());
                    //enviarcorreo.enviarCorreo(url+empresa+"\\AUTORIZADOS\\", clave, "carloscevallosfc@gmail.com");
                    this.setEnabled(true);
                    enviados.add(clave);
                    

                }
                
            }
        }  
        /*
        String mensaje = "";
        for(int i = 0; i < enviados.size(); i++){
            
            mensaje+="\n"+enviados.get(i);
            
        }
        if (enviados.size() > 0) { JOptionPane.showMessageDialog(this, "Las siguientes facturas han sido enviadas:"+mensaje);  }  
        */
        process();
    }
    
    public void cerrarDocs() {
//        try {
//            timer.cancel();
//            if(socket_conectado!=null){
//                socket_conectado.close();
//            }
//            mi_servicio.close();
//            timerTask.cancel();
//        } catch (IOException | java.lang.NullPointerException ex){
////            System.out.println("Error:"+ex);
//        }
    }
    public static void socket() {
        mi_servicio = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida;
        socket_conectado = null;
        try {
            mi_servicio = new ServerSocket(5000);
        } catch (IOException excepcion) {
            System.out.println(excepcion);
        }
        try {
            while (true) {
                socket_conectado = mi_servicio.accept();
                InputStream in = socket_conectado.getInputStream();
                try {
                    entrada = new ObjectInputStream(in);
                    salida = new ObjectOutputStream(socket_conectado.getOutputStream());
                    String[] args = (String[]) entrada.readObject();
                    if(args.length>0){
                        
                    }
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Error: " + ex);
                }
                socket_conectado.close();
            }
        } catch (IOException | ClassNotFoundException | java.lang.NullPointerException excepcion) {
            System.out.println("error:"+excepcion);
        }
    }
    public void base(){
        
        String formato = "dd/MM/yyyy";
        Date dateD = jcdDesde.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        String fechaDesde = sdf.format(dateD);
        
        Date dateH = jcdHasta.getDate();
        String fechaHasta = sdf.format(dateH);
        Integer.parseInt(jtxtTop.getText());
        
        String qListElem = "";
        switch (jcbTipo.getSelectedIndex()) {
            case 0:
                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO),A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,"
                        + "FECHAEMISION,FECHAAUTORIZACION,CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,"
                        + "NUMEROAUTORIZACION "
                        + "FROM "+baseVF+"..RELACIONSRI A, "+baseVF+"..IECABECERA B "
                        + "WHERE A.BODCODIGO = "+ bodega
                        + " AND B.BODCODIGO = "+ bodega +" AND A.TRINVTIPO = B.TRINVTIPO "
                        + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
                        + "AND A.TRINVTIPO ='VTA' "
                        + "AND IECABFECHA BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') "
                        + "ORDER BY CODIGONUMERICO DESC";
                break;
            case 1:
                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO),A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,"
                        + "FECHAEMISION,FECHAAUTORIZACION,CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,"
                        + "NUMEROAUTORIZACION "
                        + "FROM "+baseVF+"..RELACIONSRI A, co"+baseVF.substring(2)+"..RETENCIONES B "
                        + "WHERE A.IECABNUMERO = B.NRORETENCION "
                        + "AND TRAFECHA BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') "
                        + "AND A.TRINVTIPO = 'RET' "
                        + "ORDER BY CODIGONUMERICO DESC";
                break;
            case 2:
//                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO),A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,"
//                        + "FECHAEMISION,FECHAAUTORIZACION,CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,"
//                        + "NUMEROAUTORIZACION "
//                        + "FROM "+baseVF+"..RELACIONSRI A, co"+baseVF.substring(2)+"..RETENCIONES B "
//                        + "WHERE A.IECABNUMERO = B.NRORETENCION "
//                        + "AND TRAFECHA BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') "
//                        + "AND A.TRINVTIPO = 'NDB' "
//                        + "ORDER BY CODIGONUMERICO DESC";
                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO),A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,"
                        + "FECHAEMISION,FECHAAUTORIZACION,CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,"
                        + "NUMEROAUTORIZACION "
                        + "FROM "+baseVF+"..RELACIONSRI A, "+baseVF+"..IECABECERA B "
                        + "WHERE A.BODCODIGO = "+ bodega
                        + " AND B.BODCODIGO = "+ bodega +" AND A.TRINVTIPO = B.TRINVTIPO "
                        + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
                        + "AND A.TRINVTIPO ='NDB' "
                        + "AND IECABFECHA BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') "
                        + "ORDER BY CODIGONUMERICO DESC";
                break;
            case 3:
                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO) " +
                        ",A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,FECHAEMISION,FECHAAUTORIZACION " +
                        ",CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,NUMEROAUTORIZACION " +
                        "FROM "+baseVF+"..RELACIONSRI A, "+baseVF+"..DOCUMENTOS B " +
                        "WHERE A.BODCODIGO = "+ bodega 
                        + " AND B.BODCODIGO = "+ bodega
                        +" AND A.TRINVTIPO = B.TRCLIDOCUMENTO AND A.IECABNUMERO = B.DCTONUMERO " +
                        "AND A.IECABTIPO = B.DCTOTIPO AND B.TRCLIDOCUMENTO = 'NCR' " +
                        "AND DCTOFECHAEMISION BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') " +
                        "AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') ORDER BY CODIGONUMERICO DESC";
//                qListElem = "SELECT top "+jtxtTop.getText()+" A.BODCODIGO,A.TRINVTIPO,CONVERT(NUMERIC(9,0),A.IECABNUMERO),A.IECABTIPO,SECUENCIALSRI,CODIGONUMERICO,"
//                        + "FECHAEMISION,FECHAAUTORIZACION,CLAVEAUTORIZACION,ESTADOEMISION ,PERMAIL,ObservacionRecepcion,ObservacionAutorizacion,"
//                        + "NUMEROAUTORIZACION "
//                        + "FROM "+baseVF+"..RELACIONSRI A, "+baseVF+"..IECABECERA B "
//                        + "WHERE A.BODCODIGO = B.BODCODIGO AND A.TRINVTIPO = B.TRINVTIPO "
//                        + "AND A.IECABNUMERO = B.IECABNUMERO AND A.IECABTIPO = B.IECABTIPO "
//                        + "AND A.TRINVTIPO ='NCR' "
//                        + "AND IECABFECHA BETWEEN CONVERT(DATETIME,'"+fechaDesde+" 00:00:00') AND CONVERT(DATETIME,'"+fechaHasta+" 23:59:59') "
//                        + "ORDER BY CODIGONUMERICO DESC";
                break;
            default:
                break;
        }
        System.out.println("qListElem::"+qListElem);
        elements = dcons.buscador(qListElem, 14);
//        System.out.println("elementos:"+elements);
        cargarTabla();
    }
    public void process(){
//        System.out.println(url+empresa+"GENERADOS");

        File f_gen = new File(url+empresa+"\\GENERADOS");
        //File f_gen = new File("//192.168.0.160/InstaladorAmerican/Batch/Instituto/GENERADOS/");
        m_gen = f_gen.list();
        
        if(m_gen!=null)
        if(m_gen.length != m_gen1.length){
            System.out.println("corre:"+m_gen[0]);
            m_gen1 = m_gen;
            
            File f_fir = new File(url+empresa+"\\FIRMADOS");
            m_fir = f_fir.list();
            File f_env = new File(url+empresa+"\\ENVIADOS");
            m_env = f_env.list();
            File f_aut = new File(url+empresa+"\\AUTORIZADOS");
            m_aut = f_aut.list();
            File f_noAut = new File(url+empresa+"\\NO_AUTORIZADOS");
            m_noAut = f_noAut.list();
            
            base();
            
        }else{
            base();
            System.out.println("no hay cambios");
        }
    }
    public String verifica(String clave, String[] m){
        try{
            for (String m1 : m) {
                if (m1.contains(clave)) {
                    return "ok";
                }
            }
        }catch(java.lang.NullPointerException nn){
            return "";
        }
        return "";
    }
    
    public boolean firmar(String clave, String correo){
        String pathSignature = url+empresa+"\\certificado.p12";
        String passSignature = claveFirma;
        String xmlPath = url+empresa+"\\GENERADOS\\"+clave+".xml";
        String pathFirmados = url+empresa+"\\FIRMADOS\\";
        String nameFileOut = clave+"_sign.xml";
        try{
            System.out.println("firmar * "+xmlPath+" * "+pathSignature+" * "+passSignature+" * "+pathFirmados+" * "+nameFileOut);
            XAdESBESSignature.firmar(xmlPath, pathSignature, passSignature, pathFirmados, nameFileOut);
        }catch(CertificateException | IOException e){
            System.out.println("Error: " + e.getMessage());
        }
        String fff = url+empresa+"\\FIRMADOS\\"+clave+"_sign.xml";
        System.out.println("fff:"+fff);
        File archivo = new File(fff);
        if (archivo.exists()) {
            return enviar(clave, correo);
        }else{
            return false;
        }
        
    }
    
   
    public boolean enviar(String clave, String correo){
//        System.out.println("errorX");
        try{
            enviarRecepcion r = new enviarRecepcion();
//            System.out.println("errorX1");
            String respuesta = r.recepcionDocumento(url+empresa+"\\FIRMADOS\\"+clave+"_sign.xml",ambiente);
//            System.out.println("errorX2");
            
            
            if(respuesta.equals("RECIBIDA")){
//                System.out.println("errorX3");
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET FECHAEMISION=GETDATE() WHERE CLAVEAUTORIZACION='"+clave+"'";
    //                    System.out.println("UP:"+query);
                dg.guardarBase(query);
//                System.out.println("errorX4");

                /*
                Thread.currentThread();
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
//                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }
//                System.out.println("errorX5");
                autorizar(clave, correo);
//                System.out.println("errorX6");
                */
            }else{
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionRecepcion='"+respuesta+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
    //            System.out.println(query);
                dg.guardarBase(query);
                JOptionPane.showMessageDialog(this, clave+": "+respuesta);
                return false;
            }
            process();
            return true;
        }catch(java.lang.IndexOutOfBoundsException errIndex){
            System.out.println("errIndex:"+errIndex);
            return false;
        }
    }
    
    /*
    public boolean autorizar(String clave, String correo){
        List<String> respuesta = enviarAutorizacion.autorizacionFactura(clave, ambiente);
        if(respuesta.size()==5){
            if(respuesta.get(0).equals("AUTORIZADO")){
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET FECHAAUTORIZACION='"+respuesta.get(2)+"', NUMEROAUTORIZACION = '"+respuesta.get(1)+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
                dg.guardarBase(query);
                
                
                crearXml(respuesta.get(4), url+empresa+"\\AUTORIZADOS\\"+clave+".xml");
                
                Thread.currentThread();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }
//                copiar(url+empresa+"\\FIRMADOS\\"+clave+"_sign.xml", url+empresa+"\\AUTORIZADOS\\", clave);
                generarPDF pdf = new generarPDF();
                pdf.generar(clave, url+empresa, baseVF, baseGE, numFac, tipDoc, url, pathWeb, empresa);
                
                Thread.currentThread();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                enviarCorreo enviarcorreo = new enviarCorreo();
                enviarcorreo.enviarCorreo(url+empresa+"\\AUTORIZADOS\\", clave, correo);
                
                process();
            }
        }else{
            String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionAutorizacion='"+respuesta+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
            dg.guardarBase(query);
            process();
            JOptionPane.showMessageDialog(this, clave+":"+respuesta);
            return false;
        }
        return true;
    }*/
    
    public boolean autorizar(String clave, String correo){
        List<String> respuesta = enviarAutorizacion.autorizacionFactura(clave, ambiente);
        if(respuesta.size()==5){
            if(respuesta.get(0).equals("AUTORIZADO")){
                String query = "UPDATE "+baseVF+"..RELACIONSRI SET FECHAAUTORIZACION='"+respuesta.get(2)+"', NUMEROAUTORIZACION = '"+respuesta.get(1)+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
                dg.guardarBase(query);
                generarPDF pdf = new generarPDF();
                
                pdf.copiar(url+empresa+"\\GENERADOS\\"+clave+".xml", url+empresa+"\\AUTORIZADOS\\"+clave+".xml");
                
                Thread.currentThread();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }
                pdf.generar(clave, url+empresa, baseVF, baseGE, numFac, tipDoc, url, pathWeb, empresa);
                
                Thread.currentThread();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                enviarCorreo enviarcorreo = new enviarCorreo();
                enviarcorreo.enviarCorreo(url+empresa+"\\AUTORIZADOS\\", clave, correo);
                
                process();
            }
        }else{
            String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionAutorizacion='"+respuesta+"' WHERE CLAVEAUTORIZACION='"+clave+"'";
            dg.guardarBase(query);
            process();
            JOptionPane.showMessageDialog(this, clave+":"+respuesta);
            return false;
        }
        return true;
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
                Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(bw!=null){
                bw.close();
            }
            Thread.currentThread();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ex) {
                Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public final boolean procesarDatos(){
        for(int i=0;i<tabla.getRowCount();i++){
            try{
                numFac = tabla.getValueAt(i, 2).toString();
                tipDoc = tabla.getValueAt(i, 1).toString();
                String xmlgen="", xmlaut="";
                xmlgen = tabla.getValueAt(i, 9).toString();
                if(tabla.getValueAt(i, 7)!=null){
                    xmlaut = tabla.getValueAt(i, 7).toString();
                }else{
                    xmlaut="";
                }
                if(xmlaut.equals("") && xmlgen.equals("ok")){
                    if(elements.get(i).get(11)==null && elements.get(i).get(7)==null){
                        return firmar(elements.get(i).get(8),elements.get(i).get(10));
                    }else{
                        try{
                        if(elements.get(i).get(11).length()==0 && elements.get(i).get(7).length()==0){
                            return firmar(elements.get(i).get(8),elements.get(i).get(10));
                        }
                        }catch(NullPointerException nulo){}
                    }
                }
            }catch(java.lang.IndexOutOfBoundsException ee){
                System.out.println("error en elemens("+ee+"):"+elements.get(i));
                return false;
            }
        }
        return true;
    }
    public final void cargarTabla(){
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        String docTipo = "";
        switch (jcbTipo.getSelectedIndex()) {
            case 0:
                docTipo = "VTA";
                break;
            case 1:
                docTipo = "RET";
                break;
            case 2:
                docTipo = "NDB";
                break;
            case 3:
                docTipo = "NCR";
                break;
            default:
                break;
        }
        if(elements!=null)
        for(int i=0;i<elements.size();i++){
            try{
                String numFacAux = elements.get(i).get(2);
                numFacAux = numFacAux.substring(0,numFacAux.length()-3);
                numFacAux = numFacAux.replaceAll(",", "");
                
                String numSeqAux = elements.get(i).get(4);
                numSeqAux = numSeqAux.substring(0,numSeqAux.length()-3);
                numSeqAux = numSeqAux.replaceAll(",", "");
                
                String numCodAux = elements.get(i).get(5);
                numCodAux = numCodAux.substring(0,numCodAux.length()-3);
                numCodAux = numCodAux.replaceAll(",", "");
                
                String xmlgen="", xmlfir="", xmlenv="", xmlaut="", xmlnoa="";
                File f;
                
                f = new File(url+"\\"+this.empresa+"\\GENERADOS\\"+elements.get(i).get(8)+".xml");
                if (f.exists()){ xmlgen ="ok"; }
                f = new File(url+"\\"+this.empresa+"\\FIRMADOS\\"+elements.get(i).get(8)+"_sign.xml");
                if (f.exists()){ xmlfir ="ok"; }
                f = new File(url+"\\"+this.empresa+"\\AUTORIZADOS\\"+elements.get(i).get(8)+".xml");
                if (f.exists()){ xmlaut ="ok"; }
                
                /*
                xmlgen = verifica(elements.get(i).get(8), m_gen);
                xmlfir = verifica(elements.get(i).get(8), m_fir);
              
//                xmlenv = verifica(elements.get(i).get(8), m_env);
                xmlaut = verifica(elements.get(i).get(8), m_aut);
                xmlnoa = verifica(elements.get(i).get(8), m_noAut);
                
                */
                
                if(elements.get(i).get(1).equals(docTipo)){
                    Object n[]={
                        elements.get(i).get(0),
                        elements.get(i).get(1),
                        numFacAux,
                        elements.get(i).get(3),
                        numSeqAux,
                        numCodAux,
                        elements.get(i).get(6),
                        elements.get(i).get(7),
                        elements.get(i).get(8),
                        xmlgen,
                        xmlfir,
                        elements.get(i).get(9),
                        xmlaut,
                        elements.get(i).get(10),
                        elements.get(i).get(11),
                        elements.get(i).get(12),
                        elements.get(i).get(13)
                    };
                    modelo.addRow(n);
                }
            }catch(java.lang.IndexOutOfBoundsException ee){
                System.out.println("error en elemens("+ee+"):"+elements.get(i));
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jcbTipo = new javax.swing.JComboBox<>();
        jcdDesde = new com.toedter.calendar.JDateChooser();
        jcdHasta = new com.toedter.calendar.JDateChooser();
        jtxtTop = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        instLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BODEGA", "TIPO", "NUMERO", "INSTITUTO", "SRI", "CODIGO", "F_EMISION", "F_AUTORIZA", "CLAVE", "XML", "FIRMADO", "ANULADO", "AUTORIZADO", "MAIL", "ERR.RECEPCION", "ERR.AUTORIZACION", "NUM.AUTORIZACION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nube Electrónica");

        jButton2.setText("ENVIAR FACTURAS AL SRI");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("ENVIAR AL CORREO");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("ENVIAR AL SRI");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("LIMPIAR MENSAJES");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("GENERAR PDF");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("AUTORIZAR");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("REFRESCAR DATOS");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jcbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FACTURA", "RETENCION", "N. DEBITO", "N. CREDITO" }));
        jcbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbTipoItemStateChanged(evt);
            }
        });

        jtxtTop.setText("100");

        jLabel2.setText("Desde:");

        jLabel3.setText("Hasta:");

        jLabel4.setText("Nro de Registros:");

        imageLabel.setText("jLabel6");
        imageLabel.setMaximumSize(new java.awt.Dimension(100, 100));
        imageLabel.setMinimumSize(new java.awt.Dimension(100, 100));
        imageLabel.setPreferredSize(new java.awt.Dimension(100, 100));

        instLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        instLabel.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcdDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3)
                                        .addGap(2, 2, 2)
                                        .addComponent(jcdHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtxtTop, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(instLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(instLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcdHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcdDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jtxtTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)))
                    .addComponent(imageLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton6.getAccessibleContext().setAccessibleName("CREAR PDF");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        final JDialog loading = new JDialog(this);
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Enviando facturas al SRI, por favor espere!"), BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.pack();
        loading.setLocationRelativeTo(this);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setModal(true);
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            
            protected String doInBackground() throws Exception {
                
                int[] filas = tabla.getSelectedRows();
                boolean firmados = true;
                if(filas.length >0){
                    try{
                        for(int i = 0; i < filas.length; i++){

                            Object temp = tabla.getValueAt(filas[i], 8);
                            if(temp!=null){
                                String claveSel = temp.toString();
                                if(claveSel.length()==49){

                                    if(tabla.getValueAt(filas[i], 16)==null){
                                        firmar(tabla.getValueAt(filas[i], 8).toString(), tabla.getValueAt(filas[i], 13).toString());
                                    }else{

                                        if(tabla.getValueAt(filas[i], 16).toString().length()==0){
                                            firmar(tabla.getValueAt(filas[i], 8).toString(), tabla.getValueAt(filas[i], 13).toString());
                                            firmados = true;
                                        }else{

                                            firmados = false;

                                        }

                                    }
                                }
                            }

                        }
                        if(firmados){

                            return "Facturas enviadas correctamente al SRI, ahora se esperará la autorización y se enviarán los documentos automaticamente!";

                        }else{

                            return "Error al enviar alguna de las Facturas al SRI\nMotivo 1: Ya esta autorizada la factura\nMotivo 2: El SRI no recibio la factura";

                        }
                    }catch(NullPointerException nulo){
                       return "Seleccione una o mas filas";
                    }

                }else{
        //            firmar("0407201901019042651300110011000000054670000546716", "jaime.leonh@hotmail.com");
                    return "Seleccione una fila o varias filas";
                }
            }
            
            @Override
            public void done() {
                /*
                try {
                    if (!this.get().isEmpty()){
                        
                        //JOptionPane.showMessageDialog(ventana, this.get());
                        
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                loading.dispose();
            }


              
        };
        worker.execute();
        loading.setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int filas = tabla.getSelectedRow();
        if(filas >=0){
            Object temp = tabla.getValueAt(filas, 8);
            if(temp!=null){
                String claveSel = temp.toString();
                if(claveSel.length()==49){
                    Object mail = tabla.getValueAt(filas, 13);
                    if(mail!=null){
                        enviarCorreo enviarcorreo = new enviarCorreo();
                        if(enviarcorreo.enviarCorreo(url+empresa+"\\AUTORIZADOS\\", claveSel, mail.toString())){
                            JOptionPane.showMessageDialog(this, "CORREO ENVIADO");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "No hay Correo");
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int filas = tabla.getSelectedRow();
        if(filas >=0){
            Object temp = tabla.getValueAt(filas, 8);
            Object mail = tabla.getValueAt(filas, 8);
            if(temp!=null){
                String claveSel = temp.toString();
                String correo="";
                if(mail!=null){
                    correo = mail.toString();
                }
                if(claveSel.length()==49){
                    enviar(claveSel, correo);
                    JOptionPane.showMessageDialog(this, "Terminado.");
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int filas = tabla.getSelectedRow();
        if(filas >=0){
            Object temp = tabla.getValueAt(filas, 8);
            if(temp!=null){
                String claveSel = temp.toString();
                if(claveSel.length()==49){
                    String query = "UPDATE "+baseVF+"..RELACIONSRI SET ObservacionAutorizacion='', ObservacionRecepcion='' WHERE CLAVEAUTORIZACION='"+claveSel+"'";
                    dg.guardarBase(query);
                    process();
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int[] filas = tabla.getSelectedRows();
        if(filas.length > 0){
            
            for(int i = 0; i < filas.length; i++){
                
                Object temp = tabla.getValueAt(filas[i], 8);
                numFac = tabla.getValueAt(filas[i], 2).toString();
                //numFac=numFac.replaceAll(".","");
                numFac=numFac.replaceAll("\\s","");
                tipDoc = tabla.getValueAt(filas[i], 1).toString();
                if(temp!=null){
                    String clave = temp.toString();
                    generarPDF pdf = new generarPDF();
                    pdf.generar(clave, url+empresa, baseVF, baseGE, numFac, tipDoc, url, pathWeb, empresa);
                    
                }
                
            }
            JOptionPane.showMessageDialog(this, "Proceso de creación de PDF completo");
            
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione una fila o más filas");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int fila = tabla.getSelectedRow();
        int colum = tabla.getSelectedColumn();
        if(colum == 14 || colum == 15 || colum == 16){
            JOptionPane.showMessageDialog(this, tabla.getValueAt(fila, colum));
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       int filas = tabla.getSelectedRow();
        if(filas >=0){
            Object temp = tabla.getValueAt(filas, 8);
            String correo = tabla.getValueAt(filas, 13).toString();
            if(temp!=null){
                String clave = temp.toString();
                autorizar(clave, correo);
                JOptionPane.showMessageDialog(this, "Terminado.");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
        }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        process();
        generarDocumentos();
    }//GEN-LAST:event_jButton8ActionPerformed
    
    /*
    public void botonGenerar(){
    
    
        if(jtxtDesde.getText().length()>0 && jtxtHasta.getText().length()>0){
            int desde = Integer.parseInt(jtxtDesde.getText());
            int hasta = Integer.parseInt(jtxtHasta.getText());
            for (int i = desde; i <=hasta; i++) {
                if(!generar(desde, hasta)){
                    i=hasta+1;
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Ingrese Desde y Hasta");
        }
    
    }*/
    
    public boolean generar(int desde, int hasta){
        String TrInvTipo = "";
        switch (jcbTipo.getSelectedIndex()) {
            case 0:
                TrInvTipo = "VTA";
                break;
            case 1:
                TrInvTipo = "RET";
                break;
            case 2:
                TrInvTipo = "NDB";
                break;
            case 3:
                TrInvTipo = "NCR";
                break;
            default:
                break;
        }
        try{
            FacturacionElectronica fe = new FacturacionElectronica();
            for (int i = desde; i <=hasta; i++) {
//                String[] args = {"1","VTA","12813","I","vfIA","geIA"};
                String[] args = {"1",TrInvTipo,i+"",IECabTipo,baseVF,baseGE, ambiente+""};
                Object[] respuesta = fe.correr(args);
                if(!(boolean)respuesta[0] & respuesta[0].toString().length()>0){
                    JOptionPane.showMessageDialog(this, "Error: "+respuesta[0].toString());
                }
            }
            
            process();
            return procesarDatos();
        }catch(NumberFormatException nn){
            JOptionPane.showMessageDialog(this, "NUMERO NO VALIDO");
            return false;
        }
    }
    private void jcbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbTipoItemStateChanged
        cargarTabla();
    }//GEN-LAST:event_jcbTipoItemStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        cerrarDocs();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrarDocs();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NUBE_ELECTRONICA_AMERICAN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NUBE_ELECTRONICA_AMERICAN(1,"","","","","").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel instLabel;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbTipo;
    private com.toedter.calendar.JDateChooser jcdDesde;
    private com.toedter.calendar.JDateChooser jcdHasta;
    private javax.swing.JTextField jtxtTop;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
