/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

/**
 *
 * @author rlittle
 */
public class ReportConfigUtil {

    /*
   * PRIVATE METHODS
     */
    private static void setCompileTempDir(ServletContext context, String uri) {
        System.setProperty("jasper.reports.compile.temp", context.getRealPath(uri));
    }

    /*
   * PUBLIC METHODS
     */
    public static boolean compileReport(ServletContext context, String compileDir, String filename) throws JRException {

        try {
            // jasper file has not been constructed yet, so compile the xml file
            setCompileTempDir(context, compileDir);
            String sourceFile = context.getRealPath("/forms/" + filename + ".jrxml");
            String destFile = context.getRealPath("/forms/" + filename + ".jasper");
            JasperCompileManager.compileReportToFile(sourceFile, destFile);
//            JasperReport jasperReport = JasperCompileManager.compileReport(sourceFile);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JasperPrint fillReport(File reportFile, Map<String, Object> parameters, DataSource dataSource) throws JRException {
//        parameters.put("BaseDir", reportFile.getParentFile());
        JasperPrint jasperPrint = null;
        try {
            String filePath = reportFile.getPath();
            Connection conn = dataSource.getConnection();
            jasperPrint = JasperFillManager.fillReport(filePath, parameters, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return jasperPrint;
    }

    public static void fillReportToStream(InputStream inStream, ServletOutputStream servletOutputStream, Map<String, Object> parameters, DataSource dataSource) {
        try {
            try {
                JasperFillManager.fillReportToStream(inStream, servletOutputStream, parameters, dataSource.getConnection());
                servletOutputStream.flush();
                servletOutputStream.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportConfigUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ReportConfigUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JRException ex) {
            Logger.getLogger(ReportConfigUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getJasperFilePath(ServletContext context, String compileDir, String jasperFile) {
        return context.getRealPath(compileDir + jasperFile);
    }

    private static void exportReport(JRAbstractExporter exporter, JasperPrint jasperPrint, PrintWriter out) throws JRException {
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);

        exporter.exportReport();
    }

    public static void exportReportAsHtml(JasperPrint jasperPrint, PrintWriter out) throws JRException {
        JRHtmlExporter exporter = new JRHtmlExporter();
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-9");

        exportReport(exporter, jasperPrint, out);
    }
}
