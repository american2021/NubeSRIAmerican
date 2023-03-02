package facturacionelectronica;

import java.util.List;

/**
 *
 * @author Jaime
 */
public class xmlNotaCredito {
    public String getNotaCredito(List<String> cab, List<List<String>> impuestos, List<List<String>> compensaciones, List<List<String>> detalles){
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<notaCredito id=\"comprobante\" version=\"1.0.0\">\n" +
        "    <infoTributaria>\n" +
        "	<ambiente>"+cab.get(0)+"</ambiente>\n" +
        "	<tipoEmision>"+cab.get(1)+"</tipoEmision>\n" +
        "	<razonSocial>"+cab.get(2)+"</razonSocial>\n" +
        "	<nombreComercial>"+cab.get(3)+"</nombreComercial>\n" +
        "	<ruc>"+cab.get(4)+"</ruc>\n" +
        "	<claveAcceso>"+cab.get(5)+"</claveAcceso>\n" +
        "	<codDoc>"+cab.get(6)+"</codDoc>\n" +
        "	<estab>"+cab.get(7)+"</estab>\n" +
        "	<ptoEmi>"+cab.get(8)+"</ptoEmi>\n" +
        "	<secuencial>"+cab.get(9)+"</secuencial>\n" +
        "	<dirMatriz>"+cab.get(10)+"</dirMatriz>\n" +
        "    </infoTributaria>\n" +
        "    <infoNotaCredito>\n" +
        "        <fechaEmision>"+cab.get(11)+"</fechaEmision>\n" +
        "        <dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>\n" +
        "        <tipoIdentificacionComprador>"+cab.get(13)+"</tipoIdentificacionComprador>\n" +
        "        <razonSocialComprador>"+cab.get(14)+"</razonSocialComprador>\n" +
        "        <identificacionComprador>"+cab.get(15)+"</identificacionComprador>\n" +
        "        <contribuyenteEspecial>"+cab.get(16)+"</contribuyenteEspecial>\n" +
        "        <obligadoContabilidad>"+cab.get(17)+"</obligadoContabilidad>\n" +
        "        <codDocModificado>"+cab.get(19)+"</codDocModificado>\n" +
        "        <numDocModificado>"+cab.get(20)+"</numDocModificado>\n" +
        "        <fechaEmisionDocSustento>"+cab.get(21)+"</fechaEmisionDocSustento>\n" +
        "        <totalSinImpuestos>"+cab.get(22)+"</totalSinImpuestos>\n";
        if(compensaciones.size()>0){
        xml = xml +
        "        <compensaciones>\n";
        for (List<String> compensacion : compensaciones) {
            xml = xml +
            "            <compensacion>\n" +
            "                <codigo>"+compensacion.get(0)+"</codigo>\n" +
            "                <tarifa>"+compensacion.get(1)+"</tarifa>\n" +
            "                <valor>"+compensacion.get(2)+"</valor>\n" +
            "            </compensacion>\n";
        }
        xml = xml +
        "        </compensaciones>\n";
        }
        xml = xml +
        "        <valorModificacion>"+cab.get(23)+"</valorModificacion>\n" +
        "        <moneda>"+cab.get(24)+"</moneda>\n";
        if(impuestos.size()>0){
        xml = xml +
        "        <totalConImpuestos>\n";
        for (List<String> impuesto : impuestos) {
            xml = xml +
            "            <totalImpuesto>\n" +
            "                <codigo>"+impuesto.get(0)+"</codigo>\n" +
            "                <codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>\n" +
            "                <tarifa>"+impuesto.get(2)+"</tarifa>\n" +
            "                <baseImponible>"+impuesto.get(3)+"</baseImponible>\n" +
            "                <valor>"+impuesto.get(4)+"</valor>\n" +
            "            </totalImpuesto>\n";
        }
        xml = xml +
        "        </totalConImpuestos>\n";
        }
        xml = xml +
        "        <motivo>"+cab.get(25)+"</motivo>\n" +
        "    </infoNotaCredito>\n" +
        "    <detalles>\n";
        for (List<String> detalle : detalles) {
            xml = xml +
            "        <detalle>\n" +
//            "            <codigoInterno>"+detalle.get(0)[0]+"</codigoInterno>\n" +
//            "            <codigoAdicional>"+detalle.get(0)[1]+"</codigoAdicional>\n" +
            "            <descripcion>"+detalle.get(0)+"</descripcion>\n" +
            "            <cantidad>"+detalle.get(1)+"</cantidad>\n" +
            "            <precioUnitario>"+detalle.get(2)+"</precioUnitario>\n" +
            "            <descuento>0</descuento>\n" +
            "            <precioTotalSinImpuesto>"+detalle.get(3)+"</precioTotalSinImpuesto>\n" +
//            "            <detallesAdicionales>\n" +
//            "                <detAdicional nombre=\""+detalle.get(0)[7]+"\" valor=\""+detalle.get(0)[8]+"\"/>\n" +
//            "                <detAdicional nombre=\""+detalle.get(0)[9]+"\" valor=\""+detalle.get(0)[10]+"\"/>\n" +
//            "            </detallesAdicionales>\n" +
            "            <impuestos>\n";
            Double detImp = Double.parseDouble(detalle.get(4));
            String detPorc = "0", tarifa = "0.0";
            if(detImp>0){
                detPorc = "2";
                tarifa = "12.0";
            }
//            for (String[] detImpue : detalle) {
                xml = xml +
                "            <impuesto>\n" +
                "                <codigo>2</codigo>\n" +
                "                <codigoPorcentaje>"+detPorc+"</codigoPorcentaje>\n" +
                "                <tarifa>"+tarifa+"</tarifa>\n" +
                "                <baseImponible>"+detalle.get(3)+"</baseImponible>\n" +
                "                <valor>"+detImp+"</valor>\n" +
                "            </impuesto>\n";
//            }
            xml = xml +
            "            </impuestos>\n" +
            "        </detalle>\n";
        }
        xml = xml +
        "    </detalles>\n" +
        "    <infoAdicional>\n" +
        "        <campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>\n" +
//        "        <campoAdicional nombre=\""+cab.get(28)+"\">"+cab.get(29)+"</campoAdicional>\n" +
        "    </infoAdicional>\n" +
        "</notaCredito>";
        return xml;
    }
}
