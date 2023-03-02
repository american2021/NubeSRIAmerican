/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

/**
 *
 * @author Jaime
 */
public class detalleNCredito {
    private int DET_CANT;
    private String DET_DESCR;
    private Double DET_PREU;
    private Double DET_DESCU;
    private Double DET_TOT;

    public detalleNCredito(int DET_CANT, String DET_DESCR, Double DET_PREU, Double DET_DESCU, Double DET_TOT) {
        this.DET_CANT = DET_CANT;
        this.DET_DESCR = DET_DESCR;
        this.DET_PREU = DET_PREU;
        this.DET_DESCU = DET_DESCU;
        this.DET_TOT = DET_TOT;
    }

    public int getDET_CANT() {
        return DET_CANT;
    }

    public void setDET_CANT(int DET_CANT) {
        this.DET_CANT = DET_CANT;
    }

    public String getDET_DESCR() {
        return DET_DESCR;
    }

    public void setDET_DESCR(String DET_DESCR) {
        this.DET_DESCR = DET_DESCR;
    }

    public Double getDET_PREU() {
        return DET_PREU;
    }

    public void setDET_PREU(Double DET_PREU) {
        this.DET_PREU = DET_PREU;
    }

    public Double getDET_DESCU() {
        return DET_DESCU;
    }

    public void setDET_DESCU(Double DET_DESCU) {
        this.DET_DESCU = DET_DESCU;
    }

    public Double getDET_TOT() {
        return DET_TOT;
    }

    public void setDET_TOT(Double DET_TOT) {
        this.DET_TOT = DET_TOT;
    }

    
    
}
