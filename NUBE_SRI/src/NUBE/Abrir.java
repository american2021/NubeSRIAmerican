
package NUBE;

import conexion.datosConsulta;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelos.Usuario;

public class Abrir extends javax.swing.JFrame {

    public String usu;
    public String con;
    private List<Usuario> usuarios;
    
        
    public Abrir() {
        initComponents();
        cargarListaUsuarios();
        this.setLocationRelativeTo(null);
        /*aceptar.addActionListener(new ActionListener e(){
            public 
        });*/
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clave = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        aceptar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        listaUsuario = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        clave.setForeground(new java.awt.Color(0, 51, 153));
        clave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                claveKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                claveKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("CONTRASEÑA");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("USUARIO");

        aceptar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        aceptar.setForeground(new java.awt.Color(0, 51, 153));
        aceptar.setText("INGRESAR");
        aceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aceptarMouseClicked(evt);
            }
        });
        aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sistema de Facturación Electrónica");

        listaUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        listaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clave, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(listaUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(83, 83, 83))
            .addGroup(layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(listaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cargarListaUsuarios(){
        
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Instituto", "vfIA", "geIA", "I", 1, "0190426513001"));
        usuarios.add(new Usuario("BillGates", "vfBG", "geBG", "B", 1, "0102365491001"));
        usuarios.add(new Usuario("Coradac", "vfCA", "geCA", "I", 1, "0190408051001"));
        usuarios.add(new Usuario("Corel", "vfCO", "geCO", "C", 1, "0190427269001"));
        
        usuarios.add(new Usuario("Johanna", "vfVA", "geVA", "A", 1, "0105335301001"));
        usuarios.add(new Usuario("Christian", "vfVA", "geVA", "A", 2, "0103714879001"));
        usuarios.add(new Usuario("Felipe", "vfVA", "geVA", "A", 3, "0105079701001"));
        usuarios.add(new Usuario("Rosa", "vfVA", "geVA", "A", 4, "0100322791001"));
        
        listaUsuario.removeAllItems();
        for(int i = 0; i < usuarios.size(); i++){
            
            listaUsuario.addItem(usuarios.get(i).getNombre());
            
        }
        
        
    }
    
    public void acepta(){
        int index = listaUsuario.getSelectedIndex();
        Usuario elegido = usuarios.get(index);
        con = clave.getText();
        
        String query = "SELECT CLAVEFIRMA FROM appl..USUARIOS WHERE USUARIO = '"+elegido.getUsuario()+"' AND CLAVE = '"+con+"'";
        datosConsulta dcons = new datosConsulta();
        String claveFirma = dcons.consUnDato(query);
        if(claveFirma!=null){
            if(claveFirma.length()>0){
                NUBE_ELECTRONICA_AMERICAN nube = new NUBE_ELECTRONICA_AMERICAN(elegido.getBodega(), elegido.getNombre(), claveFirma, elegido.getBaseVF(), elegido.getBaseGE(), elegido.getTipo());
                this.dispose();
                nube.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this, "Error en las credenciales.");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Error en las credenciales.");
        }
    }
    
    private void aceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aceptarMouseClicked
        
    }//GEN-LAST:event_aceptarMouseClicked

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarActionPerformed
//        //aux
//        NUBE_ELECTRONICA_AMERICAN nube = new NUBE_ELECTRONICA_AMERICAN("Instituto", "Sandra1992", "vfIA", "geIA", "I");
//        this.dispose();
//        nube.setVisible(true);
//        //fin aux
        
        acepta();
   }//GEN-LAST:event_aceptarActionPerformed

    private void claveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_claveKeyTyped
        //can.requestFocus();
    }//GEN-LAST:event_claveKeyTyped

    private void claveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_claveKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            acepta();
        }
    }//GEN-LAST:event_claveKeyPressed

    private void listaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listaUsuarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Abrir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Abrir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Abrir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Abrir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Abrir().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptar;
    private javax.swing.JPasswordField clave;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox<String> listaUsuario;
    // End of variables declaration//GEN-END:variables
}
