package com.webfront.jpa.controller;

import com.webfront.beans.CompanyFacade;
import com.webfront.entity.Company;
import com.webfront.jpa.controller.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "companyController")
@SessionScoped
public class CompanyController implements Serializable {

    @EJB
    private com.webfront.beans.CompanyFacade ejbFacade;
    private final ArrayList<Company> companyList = new ArrayList<>();
    private Company selectedCompany;

    public CompanyController() {
    }

    public Company getSelected() {
        if (selectedCompany == null) {
            selectedCompany = new Company();
        }
        return selectedCompany;
    }

    public CompanyFacade getFacade() {
        return ejbFacade;
    }

    public String prepareView() {
        recreateModel();
        return "View";
    }

    public String prepareCreate() {
        selectedCompany = new Company();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(selectedCompany);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Created"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(selectedCompany);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Updated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String destroy() {
        performDestroy();
        recreateModel();
        return "View";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        return "View";
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedCompany);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Deleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
    }

    private void recreateModel() {
        companyList.clear();
    }

    /**
     * @return the list
     */
    public ArrayList<Company> getCompanyList() {
        companyList.addAll(getFacade().findAll());
        return companyList;
    }

    /**
     * @return the selectedCompany
     */
    public Company getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(Company selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public void onSelectCompany() {
        Company c;
        if (this.selectedCompany == null) {
            c = new Company();
        } else {
            c = getFacade().find(this.selectedCompany.getCompanyID());
            if (c == null) {
                c = new Company();
            }
        }
        this.selectedCompany = c;
    }

}
