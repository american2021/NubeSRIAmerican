/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 *
 * @author Jaime
 */
public class rideGenerador {

    public boolean ejecutar(Map<String, Object> parametros, String pathJasper, String pathXml, String pathPdf, String pathLogo, String esquema) {
        try {

            JRDataSource jr = null;
            if (pathXml.length() > 0) {
                jr = new JRXmlDataSource(pathXml, esquema);
            }

            JasperReport jasperReport;
            InputStream input = new FileInputStream(new File(pathJasper));
            JasperDesign jasperDesing = JRXmlLoader.load(input);
            jasperReport = JasperCompileManager.compileReport(jasperDesing);
            //File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rptJSF.jasper"));
            JasperPrint jasperPrint = null;
            /*File file = new File(pathJasper);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
            JasperPrint jasperPrint = null;*/
            System.out.println(pathXml);
            if (pathXml.length() > 0) {
                System.out.println("Mayor a 0");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, jr);
            } else {

                System.out.println("No hay nada");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            }
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pathPdf));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return true;
        } catch (JRException e) {
            System.out.println("error:" + e.getMessage());
        } catch (FileNotFoundException ex) {
            System.out.println("error 149");
        }
        return false;
    }
}
