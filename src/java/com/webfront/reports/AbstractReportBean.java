/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.reports;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
public abstract class AbstractReportBean {

    public enum ExportOption {

        PDF, HTML, EXCEL, RTF
    }

    private ExportOption exportOption;
    private final String COMPILE_DIR = "/reports/";

    public AbstractReportBean() {
        super();
        setExportOption(ExportOption.PDF);
    }

    protected void prepareReport() throws JRException, IOException {
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
            reportName += getReportParameters().get("invNum");
            reportName += "." + getExportOption();
            
            response.addHeader("Content-disposition", "attachment; filename="+reportName);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            
//            response.setContentType("application/pdf");  
//            response.addHeader("Content-Disposition","attachment, filename=myReport.pdf");
//            request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
//            response.sendRedirect(request.getContextPath() + "/servlets/reports/" + getExportOption());
            
//            FacesContext.getCurrentInstance().responseComplete();            
        }

        FacesContext.getCurrentInstance().responseComplete();
    }

    public ExportOption getExportOption() {
        return exportOption;
    }

    public SelectItem[] getExportOptions() {
        SelectItem[] items = new SelectItem[ExportOption.values().length];
        int i = 0;
        for (ExportOption o : ExportOption.values()) {
            items[i++] = new SelectItem(o, o.name());
        }
        return items;
    }

    public final void setExportOption(ExportOption exportOption) {
        this.exportOption = exportOption;
    }

    protected Map<String, Object> getReportParameters() {
        return new HashMap<String, Object>();
    }

    protected String getCompileDir() {
        return COMPILE_DIR;
    }

    protected abstract javax.sql.DataSource getDataSource();

    protected abstract String getCompileFileName();
}
