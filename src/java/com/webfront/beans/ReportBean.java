/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.reports.AbstractReportBean;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

/**
 *
 * @author rlittle
 */
@ManagedBean(name = "ReportBean")
@SessionScoped
public class ReportBean extends AbstractReportBean {

    private final String COMPILE_FILE_NAME = "my_first_report";
    private String invNum;
    private String invDate;
    private Integer clientId;

    private static DataSource dataSource;

    public ReportBean() {

    }

    @Resource(name = "jdbc/contractor")
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
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
        reportParameters.put("clientId", new Integer(clientId));
        String value = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("hidden1");

        return reportParameters;
    }

    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ContractorSession sb = (ContractorSession) map.get("sessionBean");
        if (sb.clientId != null) {
            clientId = sb.clientId;
        }
    }

    public String execute() {
        try {
            super.prepareReport();
        } catch (Exception e) {
            // make your own exception handling
        }

        return null;
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

}
