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
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author rlittle
 */
@WebServlet(name = "Pdf", urlPatterns = {"/printinvoice.jsp"})
public class Pdf extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource(name = "jdbc/contractor")
    private DataSource dataSource;

//    public void setDataSource(DataSource ds) {
//        this.dataSource = ds;
//    }
    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = this.getServletConfig().getServletContext();
        File reportFile = new File(getServletConfig().getServletContext().getRealPath("/reports/Invoice.jasper"));
        if (!reportFile.exists()) {
            throw new JRRuntimeException("File Invoice.jasper not found. The report design must be compiled first.");
        }

        Map parameters = new HashMap();
        String reportName = (String) request.getAttribute("REPORT_NAME");
        parameters = (HashMap) request.getAttribute("REPORT_PARAMS");
        JasperPrint jasperPrint = (JasperPrint) request.getAttribute("JASPER_PRINT");
        
        byte[] bytes = null;
        try {
            Connection conn = getDataSource().getConnection();
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            
            //jasperPrint =
            //        JasperFillManager.fillReport(jasperReport, parameters, conn);
            //            //response.setContentType("application/pdf");  
//        if (jasperPrint != null) {

            response.setContentType("application/octect-stream");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline, filename=myReport.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();

//        }
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
            Logger.getLogger("out").fine(e.getMessage());

            out.println("</pre>");

            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
            return;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
