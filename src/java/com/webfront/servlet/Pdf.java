/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author rlittle
 */
@WebServlet(name = "Pdf", urlPatterns = {"/printinvoice.jsp"})
public class Pdf extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource(name = "jdbc/contractor")
    private DataSource dataSource;
    
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletContext context = this.getServletConfig().getServletContext();
        File reportFile = new File(context.getRealPath("/reports/Invoice.jasper"));
        if (!reportFile.exists()) {
            throw new JRRuntimeException("File Invoice.jasper not found. The report design must be compiled first.");
        }

        JasperPrint jasperPrint = null;
        Map parameters = new HashMap();
        byte[] bytes=null;
        try {
            Connection conn = getDataSource().getConnection();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
            bytes = JasperRunManager.runReportToPdf(jasperReport,parameters,conn);
            //jasperPrint =
            //        JasperFillManager.fillReport(jasperReport, parameters, conn);
            
        } catch (SQLException e) {
        } catch (JRException e) {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>JasperReports - Web Application Sample</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");

            out.println("<body bgcolor=\"white\">");

            out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");

            out.println("</body>");
            out.println("</html>");

            return;
        }
        if (jasperPrint != null) {
            //JRPdfExporter exporter = new JRPdfExporter();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","inline, filename=myReport.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
 
//            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oos);
//            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//            
//            try {
//                exporter.exportReport();
//                JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
//            } catch (Exception e) {
//                
//            }
//            
//            oos.flush();
//            oos.close();
//
//            outputStream.flush();
//            outputStream.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
