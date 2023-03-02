package facturacionelectronica;

import java.util.ArrayList;
import java.util.List;

public class xmlFactura {
    String ambiente;
    String tipoEmision;
    String razonSocial;
    String ruc;
    String claveAcceso;
    String codDoc;
    String estab;
    String ptoEmi;
    String secuencial;
    String dirMatriz;
    String fechaEmision;
    String tipoIdent;
    String razonSocialCli;
    String identificacionComprador;
    String totalSinImpuestos;
    String totalDescuento;
    String importeTotal;
    String impCodigo;
    String impCodigoPorcentaje;
    String impBaseImponible;
    String impValor;
    List<detalle> det = new ArrayList<>();
    
    public String generarXML(){
        String detalle="";
        String xml;
        xml = ""+
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<factura id=\"comprobante\" version=\"2.0.0\">\n" +
            "    <infoTributaria>\n" +
            "        <ambiente>"+ambiente+"</ambiente>\n" +
            "        <tipoEmision>"+tipoEmision+"</tipoEmision>\n" +
            "        <razonSocial>"+razonSocial+"</razonSocial>\n" +
            "        <ruc>"+ruc+"</ruc>\n" +
            "        <claveAcceso>"+claveAcceso+"</claveAcceso>\n" +
            "        <codDoc>"+codDoc+"</codDoc>\n" +
            "        <estab>"+estab+"</estab>\n" +
            "        <ptoEmi>"+ptoEmi+"</ptoEmi>\n" +
            "        <secuencial>"+secuencial+"</secuencial>\n" +
            "        <dirMatriz>"+dirMatriz+"</dirMatriz>\n" +
            "    </infoTributaria>\n" +
            "    <infoFactura>\n" +
            "        <fechaEmision>"+fechaEmision+"</fechaEmision>\n" +
            "        <tipoIdentificacionComprador>"+tipoIdent+"</tipoIdentificacionComprador>\n" +
            "        <razonSocialComprador>"+razonSocialCli+"</razonSocialComprador>\n" +
            "        <identificacionComprador>"+identificacionComprador+"</identificacionComprador>\n" +
            "        <totalSinImpuestos>"+totalSinImpuestos+"</totalSinImpuestos>\n" +
            "        <totalDescuento>"+totalDescuento+"</totalDescuento>\n" +
            "        <totalConImpuestos>\n" +
            "            <totalImpuesto>\n" +
            "                <codigo>"+impCodigo+"</codigo>\n" +
            "                <codigoPorcentaje>"+impCodigoPorcentaje+"</codigoPorcentaje>\n" +
            "                <baseImponible>"+impBaseImponible+"</baseImponible>\n" +
            "                <valor>"+impValor+"</valor>\n" +
            "            </totalImpuesto>\n" +
            "        </totalConImpuestos>\n" +
            "        <importeTotal>"+importeTotal+"</importeTotal>\n" +
            "    </infoFactura>\n" +
            "    <detalles>\n";

            for (int i = 0; i < det.size(); i++) {
                detalle=detalle+
                "        <detalle>\n" +
                "            <descripcion>"+det.get(i).descripcion+"</descripcion>\n" +
                "            <cantidad>"+det.get(i).cantidad+"</cantidad>\n" +
                "            <precioUnitario>"+det.get(i).precioUnitario+"</precioUnitario>\n" +
                "            <descuento>"+det.get(i).descuento+"</descuento>\n" +
                "            <precioTotalSinImpuesto>"+det.get(i).precioTotalSinImpuesto+"</precioTotalSinImpuesto>\n" +
                "            <impuestos>\n" +
                "                <impuesto>\n" +
                "                    <codigo>"+det.get(i).impCodigo+"</codigo>\n" +
                "                    <codigoPorcentaje>"+det.get(i).impCodigoPorcentaje+"</codigoPorcentaje>\n" +
                "                    <tarifa>"+det.get(i).impTarifa+"</tarifa>\n" +
                "                    <baseImponible>"+det.get(i).impBaseImponible+"</baseImponible>\n" +
                "                    <valor>"+det.get(i).impValor+"</valor>\n" +
                "                </impuesto>\n" +
                "            </impuestos>\n" +
                "        </detalle>\n";
            }

            xml=xml+detalle+
            "    </detalles>\n" +
            "</factura>";
//            System.out.println(xml);
        return xml;
    }
    public List<detalle> getDet() {
        return det;
    }

    public void setDet(List<detalle> det) {
        this.det = det;
    }
    
    public static class detallePagos{
        String formaPago;
        String total;
        public void detallePagos(){
            
        }

        public String getFormaPago() {
            return formaPago;
        }
        
        public void setFormaPago(String formaPago) {
            this.formaPago = formaPago;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
    public static class detalle{
        String descripcion;
        String cantidad;
        String precioUnitario;
        String descuento;
        String precioTotalSinImpuesto;
        String impCodigo;
        String impCodigoPorcentaje;
        String impBaseImponible;
        String impTarifa;
        String impValor;
        public void detalle(){
            
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getCantidad() {
            return cantidad;
        }

        public void setCantidad(String cantidad) {
            this.cantidad = cantidad;
        }

        public String getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(String precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public String getDescuento() {
            return descuento;
        }

        public void setDescuento(String descuento) {
            this.descuento = descuento;
        }

        public String getPrecioTotalSinImpuesto() {
            return precioTotalSinImpuesto;
        }

        public void setPrecioTotalSinImpuesto(String precioTotalSinImpuesto) {
            this.precioTotalSinImpuesto = precioTotalSinImpuesto;
        }

        public String getImpCodigo() {
            return impCodigo;
        }

        public void setImpCodigo(String impCodigo) {
            this.impCodigo = impCodigo;
        }

        public String getImpCodigoPorcentaje() {
            return impCodigoPorcentaje;
        }

        public void setImpCodigoPorcentaje(String impCodigoPorcentaje) {
            this.impCodigoPorcentaje = impCodigoPorcentaje;
        }

        public String getImpBaseImponible() {
            return impBaseImponible;
        }

        public void setImpBaseImponible(String impBaseImponible) {
            this.impBaseImponible = impBaseImponible;
        }

        public String getImpTarifa() {
            return impTarifa;
        }

        public void setImpTarifa(String impTarifa) {
            this.impTarifa = impTarifa;
        }

        public String getImpValor() {
            return impValor;
        }

        public void setImpValor(String impValor) {
            this.impValor = impValor;
        }

    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCodDoc() {
        return codDoc;
    }

    public void setCodDoc(String codDoc) {
        this.codDoc = codDoc;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getPtoEmi() {
        return ptoEmi;
    }

    public void setPtoEmi(String ptoEmi) {
        this.ptoEmi = ptoEmi;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getDirMatriz() {
        return dirMatriz;
    }

    public void setDirMatriz(String dirMatriz) {
        this.dirMatriz = dirMatriz;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoIdent() {
        return tipoIdent;
    }

    public void setTipoIdent(String tipoIdent) {
        this.tipoIdent = tipoIdent;
    }

    public String getRazonSocialCli() {
        return razonSocialCli;
    }

    public void setRazonSocialCli(String razonSocialCli) {
        this.razonSocialCli = razonSocialCli;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(String totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public String getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(String totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public String getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(String importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getImpCodigo() {
        return impCodigo;
    }

    public void setImpCodigo(String impCodigo) {
        this.impCodigo = impCodigo;
    }

    public String getImpCodigoPorcentaje() {
        return impCodigoPorcentaje;
    }

    public void setImpCodigoPorcentaje(String impCodigoPorcentaje) {
        this.impCodigoPorcentaje = impCodigoPorcentaje;
    }

    public String getImpBaseImponible() {
        return impBaseImponible;
    }

    public void setImpBaseImponible(String impBaseImponible) {
        this.impBaseImponible = impBaseImponible;
    }

    public String getImpValor() {
        return impValor;
    }

    public void setImpValor(String impValor) {
        this.impValor = impValor;
    }
}
