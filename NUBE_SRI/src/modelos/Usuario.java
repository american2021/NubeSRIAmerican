/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author Administrador
 */
public class Usuario {
    
    private String nombre;
    private String baseVF;
    private String baseGE;
    private String tipo; 
    private int bodega;
    private String usuario;

    public Usuario() {
    }

    public Usuario(String nombre, String baseVF, String baseGE, String tipo, int bodega, String usuario) {
        this.nombre = nombre;
        this.baseVF = baseVF;
        this.baseGE = baseGE;
        this.tipo = tipo;
        this.bodega = bodega;
        this.usuario = usuario;
    }

    
    

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    /**
     * @return the bodega
     */
    public int getBodega() {
        return bodega;
    }

    /**
     * @param bodega the bodega to set
     */
    public void setBodega(int bodega) {
        this.bodega = bodega;
    }

    /**
     * @return the baseVF
     */
    public String getBaseVF() {
        return baseVF;
    }

    /**
     * @param baseVF the baseVF to set
     */
    public void setBaseVF(String baseVF) {
        this.baseVF = baseVF;
    }

    /**
     * @return the baseGE
     */
    public String getBaseGE() {
        return baseGE;
    }

    /**
     * @param baseGE the baseGE to set
     */
    public void setBaseGE(String baseGE) {
        this.baseGE = baseGE;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
}
