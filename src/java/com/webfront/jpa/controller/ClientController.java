package com.webfront.jpa.controller;

import com.webfront.beans.ClientFacade;
import com.webfront.entity.Client;
import com.webfront.jpa.controller.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "clientController")
@SessionScoped
public class ClientController implements Serializable {

    private Client selectedClient;
    @EJB
    private com.webfront.beans.ClientFacade ejbFacade;

    public ClientController() {
    }

    public List<Client> getClientList() {
        List<Client> list=getFacade().findAll();
        return list;
    }
    
    public Client getSelectedClient() {
        if (selectedClient == null) {
            selectedClient = new Client();
        }
        return selectedClient;
    }
    
    public void onSelectClient() {
        Client c;
        if (this.selectedClient == null) {
            c = new Client();
        } else {
            c = getFacade().find(this.selectedClient.getId());
            if (c == null) {
                c = new Client();
            }
        }
        this.selectedClient = c;
    }
    
    public void setSelectedClient(Client c) {
        this.selectedClient=c;
    }

    public ClientFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        selectedClient = new Client();
        return "Create?faces-redirect=true";
    }

    public String create() {
        try {
            getFacade().create(selectedClient);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Created"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(selectedClient);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Updated"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String destroy() {
        performDestroy();
        return "View?faces-redirect=true";
    }

    public String destroyAndView() {
        performDestroy();
        return "View?faces-redirect=true";
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedClient);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Deleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
    }

}
