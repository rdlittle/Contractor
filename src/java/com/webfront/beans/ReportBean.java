/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Invoice;
import com.webfront.reports.AbstractReportBean;
import com.webfront.reports.ReportConfigUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
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
@ManagedBean(name = "ReportBean")
@SessionScoped
public class ReportBean extends AbstractReportBean implements Serializable {

    private final String COMPILE_FILE_NAME = "Invoice";
    private String invNum;
    private String invDate;
    private Integer clientId;
    private Invoice selectedInvoice;

    private static DataSource dataSource;

//    @ManagedProperty(value = "#{sessionBean}")
//    private ContractorSession session = null;

    public ReportBean() {
        selectedInvoice = null;
    }

    @Resource(name = "jdbc/contractor")
    public void setDataSource(DataSource ds) {
        ReportBean.dataSource = ds;
    }

    @Override
    public DataSource getDataSource() {
        return ReportBean.dataSource;
    }

    @Override
    protected String getCompileFileName() {
        return COMPILE_FILE_NAME;
    }

    @Override
    protected Map<String, Object> getReportParameters() {
        Map<String, Object> reportParameters = new HashMap<>();
        reportParameters.put("invNum", invNum);
        reportParameters.put("invDate", invDate);
        reportParameters.put("clientId", clientId);
        return reportParameters;
    }

    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ContractorSession sb = (ContractorSession) map.get("sessionBean");
        if (sb != null && sb.clientId != null) {
            clientId = sb.clientId;
        }
    }

    public String execute() {
        try {
            super.prepareReport();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;
    }

    public void doReport() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext context = (ServletContext) externalContext.getContext();

            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            File reportFile = new File(context.getRealPath("/reports/Invoice.jasper"));
            if (!reportFile.exists()) {
                throw new JRRuntimeException("File Invoice.jasper not found. The report design must be compiled first.");
            }

            DataSource ds = getDataSource();
            JasperPrint jasperPrint = ReportConfigUtil.fillReport(reportFile, getReportParameters(), ds);

            if (getExportOption().equals(ExportOption.HTML)) {
                ReportConfigUtil.exportReportAsHtml(jasperPrint, response.getWriter());
            } else {
                String reportName = "invoice_";
//                reportName += getReportParameters().get("invNum");
//                reportName += "." + getExportOption();


                // Send pdf to browser
//          response.setContentType("application/pdf");
//          response.addHeader("Content-disposition", "attachment; filename=" + reportName);
//          ServletOutputStream servletOutputStream = response.getOutputStream();
//          JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

// Redirect to the servlet
//          getReportParameters().put("reportName", reportName);
//          request.setAttribute("REPORT_NAME", reportName);
//          request.setAttribute("REPORT_PARAMS", getReportParameters());
//          request.setAttribute("JASPER_PRINT", jasperPrint);
//          response.sendRedirect(request.getContextPath() + "/servlets/reports/" + getExportOption());
//          FacesContext.getCurrentInstance().responseComplete();
                reportName = reportName + getReportParameters().get("invNum");
                reportName = reportName + "." + getExportOption();
                response.setContentType("application/pdf");
                response.addHeader("Content-disposition", "attachment; filename=" + reportName);
                ServletOutputStream servletOutputStream = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            }
            FacesContext.getCurrentInstance().responseComplete();
            /**
             * @return the invNum
             */
        } catch (JRException ex) {
            Logger.getLogger(ReportBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReportBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the invNum
     */
    public String getInvNum() {
        return invNum;
    }

    /**
     * @param invNum the invNum to set
     */
    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    /**
     * @return the invDate
     */
    public String getInvDate() {
        return invDate;
    }

    /**
     * @param invDate the invDate to set
     */
    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the selectedInvoice
     */
    public Invoice getSelectedInvoice() {
        return selectedInvoice;
    }

    /**
     * @param inv
     */
    public void setSelectedInvoice(Invoice inv) {
        if (inv == null) {
            return;
        }
        this.selectedInvoice = inv;
        this.invDate = selectedInvoice.getShortDate();
        this.invNum = selectedInvoice.getInvoice();
    }

    public void onChangeInvoice() {
        this.invDate = selectedInvoice.getShortDate();
    }
//
//    /**
//     * @return the session
//     */
//    public ContractorSession getSession() {
//        return session;
//    }
//
//    /**
//     * @param session the session to set
//     */
//    public void setSession(ContractorSession session) {
//        this.session = session;
//    }

}
