package com.webfront.jpa.controller;

import com.webfront.beans.ContractorSession;
import com.webfront.beans.InvoiceFacade;
import com.webfront.entity.Invoice;
import com.webfront.jpa.controller.util.JsfUtil;
import com.webfront.jpa.controller.util.PaginationHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "InvoiceController")
@SessionScoped
public class InvoiceController implements Serializable {

    private Invoice current;
    private DataModel items = null;
    @EJB
    private com.webfront.beans.InvoiceFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Invoice> invoiceList;

    public InvoiceController() {
        invoiceList = new ArrayList<>();
    }

    public String Html() {
        return "/";
    }

    public void setCurrent(Invoice i) {
        current = i;
    }

    public Invoice getSelected() {
        if (current == null) {
            current = new Invoice();
            selectedItemIndex = -1;
        }
        return current;
    }

    public InvoiceFacade getFacade() {
        return ejbFacade;
    }

    public List<Invoice> getInvoiceList() {
        List<Invoice> list;
        list = getFacade().findClientInvoices();
        if (list == null) {
            return new ArrayList<>();
        }
        invoiceList.clear();
        invoiceList.addAll(list);
        return invoiceList;
    }

    public void onChangeclient() {
        List<Invoice> list;
        list = getFacade().findClientInvoices();
        if (list == null) {
            return;
        }
        invoiceList.clear();
        invoiceList.addAll(list);
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

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = getPagination().createPageDataModel();
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ContractorSession sb = (ContractorSession) ec.getSessionMap().get("sessionBean");
            if (sb != null) {
                Integer cid = sb.getClientId();
                if (cid != null) {
                    sb.setClientName(getFacade().getClientName(cid));
                }
            }
        }
    }

    public String prepareView() {
        current = (Invoice) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Invoice();
        String inv = getFacade().getNextInv();
        current.setInvoice(inv);
        String pid = getFacade().getNextPeriod();
        current.setPeriodNum(pid);
        selectedItemIndex = -1;
        return "Create?faces-redirect=true";
    }

    public String getCurrentInvoiceNumber() {
        //entityManager.createNamedQuery(Item.QUERY_ALL).getResultList();
        return "";
    }

    public String getNextInvoiceNumber() {
        return getFacade().getNextInv();
    }

    public String create() {
        try {
            getFacade().create(current);
            getFacade().setNextInv(Integer.parseInt(current.getInvoice()));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Created"));
            Double serial = Math.random();
            return "/invoices/List?faces-redirect=true" + "&serial=" + serial.toString();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Invoice) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Updated"));
            return "List?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String destroy() {
        current = (Invoice) getItems().getRowData();
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
            getFacade().remove(current);
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
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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

//    @FacesConverter(forClass = Invoice.class)
//    public static class InvoiceConverter implements Converter {
//
//        @Override
//        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
//            if (value == null || value.length() == 0) {
//                return null;
//            }
//            InvoiceController controller = (InvoiceController) facesContext.getApplication().getELResolver().
//                    getValue(facesContext.getELContext(), null, "InvoiceController");
//            Invoice i = controller.ejbFacade.find(getKey(value));
//            return i;
//        }
//
//        java.lang.Integer getKey(String value) {
//            java.lang.Integer key;
//            key = Integer.valueOf(value);
//            return key;
//        }
//
//        String getStringKey(java.lang.Integer value) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(value);
//            return sb.toString();
//        }
//
//        @Override
//        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
//            if (object == null) {
//                return null;
//            }
//            if (object instanceof Invoice) {
//                Invoice o = (Invoice) object;
//                return getStringKey(o.getId());
//            } else {
//                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + InvoiceController.class.getName());
//            }
//        }
//    }
}
