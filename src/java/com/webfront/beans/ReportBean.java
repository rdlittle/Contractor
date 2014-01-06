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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

/**
 *
 * @author rlittle
 */
@ManagedBean(name="ReportBean")
@RequestScoped
public class ReportBean extends AbstractReportBean {

  private final String COMPILE_FILE_NAME = "my_first_report";
  private String invNum;
  private String invDate;
  private String clientId;
  
  private DataSource dataSource;
  
  @Resource(lookup="jdbc/contractor")
  public void setDataSource(DataSource ds) {
      this.dataSource=ds;
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
    Map<String, Object> reportParameters = new HashMap<String, Object>();
    
    reportParameters.put("invNum", invNum);
    reportParameters.put("clientId", new Integer(clientId));
    String value = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("hidden1");
    
    return reportParameters;
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
    public String getinvNum() {
        return invNum;
    }

    /**
     * @param invNum the invNum to set
     */
    public void setinvNum(String invNum) {
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
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
