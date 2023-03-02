/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacionelectronica;

import java.util.List;

/**
 *
 * @author Jaime
 */
public class xmlRetencion {
    public String retencion(List<String> cab, List<List<String>> imp){
        String xml = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<comprobanteRetencion id=\"comprobante\" version=\"1.0.0\">\n" +
            "	<infoTributaria>\n" +
            "		<ambiente>"+cab.get(0)+"</ambiente>\n" +
            "		<tipoEmision>"+cab.get(1)+"</tipoEmision>\n" +
            "		<razonSocial>"+cab.get(2)+"</razonSocial>\n" +
            "		<nombreComercial>"+cab.get(3)+"</nombreComercial>\n" +
            "		<ruc>"+cab.get(4)+"</ruc>\n" +
            "		<claveAcceso>"+cab.get(5)+"</claveAcceso>\n" +
            "		<codDoc>"+cab.get(6)+"</codDoc>\n" +
            "		<estab>"+cab.get(7)+"</estab>\n" +
            "		<ptoEmi>"+cab.get(8)+"</ptoEmi>\n" +
            "		<secuencial>"+cab.get(9)+"</secuencial>\n" +
            "		<dirMatriz>"+cab.get(10)+"</dirMatriz>\n" +
            "	</infoTributaria>\n" +
            "	<infoCompRetencion>\n" +
            "		<fechaEmision>"+cab.get(11)+"</fechaEmision>\n" +
            "		<dirEstablecimiento>"+cab.get(12)+"</dirEstablecimiento>\n";
            try{
            if(Integer.parseInt(cab.get(13))>0){
            xml = xml +
            "		<contribuyenteEspecial>"+cab.get(13)+"</contribuyenteEspecial>\n";
            }
            }catch(java.lang.NumberFormatException nn){}
            xml = xml +
            "		<obligadoContabilidad>"+cab.get(14)+"</obligadoContabilidad>\n" +
            "		<tipoIdentificacionSujetoRetenido>"+cab.get(15)+"</tipoIdentificacionSujetoRetenido>\n" +
            "		<razonSocialSujetoRetenido>"+cab.get(16)+"</razonSocialSujetoRetenido>\n" +
            "		<identificacionSujetoRetenido>"+cab.get(17)+"</identificacionSujetoRetenido>\n" +
            "		<periodoFiscal>"+cab.get(18)+"</periodoFiscal>\n" +
            "	</infoCompRetencion>\n" +
            "	<impuestos>\n";
        for (int i = 0; i <imp.size(); i++) {
            xml=xml+
            "		<impuesto>\n" +
            "			<codigo>"+imp.get(i).get(0)+"</codigo>\n" +
            "			<codigoRetencion>"+imp.get(i).get(1)+"</codigoRetencion>\n" +
            "			<baseImponible>"+imp.get(i).get(2)+"</baseImponible>\n" +
            "			<porcentajeRetener>"+imp.get(i).get(3)+"</porcentajeRetener>\n" +
            "			<valorRetenido>"+imp.get(i).get(4)+"</valorRetenido>\n" +
            "			<codDocSustento>"+imp.get(i).get(5)+"</codDocSustento>\n" +
            "			<numDocSustento>"+imp.get(i).get(6)+"</numDocSustento>\n" +
            "			<fechaEmisionDocSustento>"+imp.get(i).get(7)+"</fechaEmisionDocSustento>\n" +
            "		</impuesto>\n";
        }
            xml=xml+
            "	</impuestos>\n" +
//            "	<infoAdicional>\n";
//            if(cab.get(19)!=null)xml=xml+"		<campoAdicional nombre=\""+cab.get(19)+"\">"+cab.get(20)+"</campoAdicional>\n";
//            if(cab.get(21)!=null)xml=xml+"		<campoAdicional nombre=\""+cab.get(21)+"\">"+cab.get(22)+"</campoAdicional>\n";
//            if(cab.get(23)!=null)xml=xml+"		<campoAdicional nombre=\""+cab.get(23)+"\">"+cab.get(24)+"</campoAdicional>\n";
//            xml=xml+
//            "	</infoAdicional>\n" +
            "</comprobanteRetencion>";
        return xml;
    }
}
