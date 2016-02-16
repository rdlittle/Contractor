package com.webfront.jpa.controller;

import com.webfront.beans.ClientFacade;
import com.webfront.entity.Client;
import com.webfront.jpa.controller.util.JsfUtil;
import com.webfront.jpa.controller.util.PaginationHelper;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "clientController")
@SessionScoped
public class ClientController implements Serializable {

    private Client selectedClient;
    private DataModel items = null;
    @EJB
    private com.webfront.beans.ClientFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ClientController() {
    }

    public List<Client> getClientList() {
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,String> map = fc.getExternalContext().getRequestParameterMap();
        String key="timesheetForm:clientSelector";
        String value=map.get(key);
        List<Client> list=getFacade().findAll();
        return list;
    }
    
    public Client getSelectedClient() {
        if (selectedClient == null) {
            selectedClient = new Client();
            selectedItemIndex = -1;
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

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        selectedClient = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        selectedClient = new Client();
        selectedItemIndex = -1;
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
        selectedClient = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
        selectedClient = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View?faces-redirect=true";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List?faces-redirect=true";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedClient);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Deleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            selectedClient = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

}
