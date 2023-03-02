package facturacionelectronica;

import java.util.List;

/**
 *
 * @author Jaime
 */
public class xmlNotaDebito {
    public String getNotaDebito(List<String> cab, List<List<String>> impuestos, List<List<String>> compensaciones, List<List<String[]>> pagos, List<List<String>> motivos){
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<notaDebito id=\"comprobante\" version=\"1.0.0\">\n" +
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
        "    <infoNotaDebito>\n" +
        "        <fechaEmision>"+cab.get(11)+"</fechaEmision>\n" +
        "        <dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>\n" +
        "        <tipoIdentificacionComprador>"+cab.get(13)+"</tipoIdentificacionComprador>\n" +
        "        <razonSocialComprador>"+cab.get(14)+"</razonSocialComprador>\n" +
        "        <identificacionComprador>"+cab.get(15)+"</identificacionComprador>\n" +
        "        <contribuyenteEspecial>"+cab.get(16)+"</contribuyenteEspecial>\n" +
        "        <obligadoContabilidad>"+cab.get(17)+"</obligadoContabilidad>\n" +
        "        <rise>rise"+cab.get(18)+"</rise>\n" +
        "        <codDocModificado>"+cab.get(19)+"</codDocModificado>\n" +
        "        <numDocModificado>"+cab.get(20)+"</numDocModificado>\n" +
        "        <fechaEmisionDocSustento>"+cab.get(21)+"</fechaEmisionDocSustento>\n" +
        "        <totalSinImpuestos>"+cab.get(22)+"</totalSinImpuestos>\n";
        if(impuestos.size()>0){
        xml = xml +
        "        <impuestos>\n";
        for (List<String> impuesto : impuestos) {
            xml = xml +
            "            <impuesto>\n" +
            "                <codigo>"+impuesto.get(0)+"</codigo>\n" +
            "                <codigoPorcentaje>"+impuesto.get(1)+"</codigoPorcentaje>\n" +
            "                <tarifa>"+impuesto.get(2)+"</tarifa>\n" +
            "                <baseImponible>"+impuesto.get(3)+"</baseImponible>\n" +
            "                <valor>"+impuesto.get(4)+"</valor>\n" +
            "            </impuesto>\n";
        }
        xml = xml +
        "        </impuestos>\n";
        }
        if(compensaciones.size()>0){
        xml = xml + "        <compensaciones>\n";
        for (List<String> compensacion : compensaciones) {
            xml = xml +
            "            <compensacion>\n" +
            "                <codigo>"+compensacion.get(0)+"</codigo>\n" +
            "                <tarifa>"+compensacion.get(1)+"</tarifa>\n" +
            "                <valor>"+compensacion.get(2)+"</valor>\n" +
            "            </compensacion>\n";
        }
        xml = xml + "        </compensaciones>\n";
        }
        xml = xml +
        "        <valorTotal>"+cab.get(23)+"</valorTotal>\n";
        if(pagos.size()>0){
        for (List<String[]> pago : pagos) {
            xml = xml +
            "        <pagos>\n";
            for (String[] pag : pago) {
                xml = xml +
                "            <pago>\n" +
                "                <formaPago>"+pag[0]+"</formaPago>\n" +
                "                <total>"+pag[1]+"</total>\n" +
                "                <plazo>"+pag[2]+"</plazo>\n" +
                "                <unidadTiempo>"+pag[3]+"</unidadTiempo>\n" +
                "            </pago>\n";
            }
            xml = xml +
            "        </pagos>\n";
        }
        }
        xml = xml +
        "    </infoNotaDebito>\n" +
        "    <motivos>\n";
        for (List<String> motivo : motivos) {
            xml = xml +
            "        <motivo>\n" +
            "            <razon>"+motivo.get(0)+"</razon>\n" +
            "            <valor>"+motivo.get(1)+"</valor>\n" +
            "        </motivo>\n";
        }
        xml = xml +
        "    </motivos>\n" +
        "    <infoAdicional>\n" +
        "        <campoAdicional nombre=\""+cab.get(24)+"\">"+cab.get(25)+"</campoAdicional>\n" +
        "        <campoAdicional nombre=\""+cab.get(26)+"\">"+cab.get(27)+"</campoAdicional>\n" +
        "    </infoAdicional>\n" +
        "</notaDebito>";
        return xml;
    }
}
