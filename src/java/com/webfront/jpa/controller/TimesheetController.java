package com.webfront.jpa.controller;

import com.webfront.beans.TimesheetFacade;
import com.webfront.entity.Timesheet;
import com.webfront.jpa.controller.util.JsfUtil;
import com.webfront.jpa.controller.util.PaginationHelper;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Transient;
import org.primefaces.component.selectonemenu.SelectOneMenu;

@ManagedBean(name = "timesheetController")
@SessionScoped
public class TimesheetController implements Serializable {

    private Timesheet current;
    private DataModel items = null;
    @EJB
    private com.webfront.beans.TimesheetFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer clientId;
    private List<Timesheet> selectedItems;

    private Float totalHours;
    private Float invoiceTotal;
    private Float rate;
    private Date invoiceDate;
    private String clientName;

    @Transient
    private Period selectedPeriod;
    private String junk;

    public TimesheetController() {
    }

    public Timesheet getSelected() {
        if (current == null) {
            current = new Timesheet();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TimesheetFacade getFacade() {
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
                    String dir = getOrder();
                    ListDataModel list;
                    int[] range = new int[2];
                    range[0] = getPageFirstItem();
                    range[1] = getPageFirstItem() + getPageSize();
                    list = new ListDataModel(getFacade().findRange(range));
                    return list;
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
        current = (Timesheet) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Timesheet();
        String inv = getFacade().getNextInv();
        current.setInvNum(inv);
        selectedItemIndex = -1;
        return "Create?faces-redirect=true";
    }

    public String prepareInvoice() {
        if (!selectedItems.isEmpty()) {
            if(clientId!=null) {
                setClientName(getFacade().getClientName(clientId));
            }
            Date d = getFacade().getPeriod();
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            setInvoiceDate(cal.getTime());
            setRate(getFacade().getRate());
            setTotalHours(new Float(0.0));
            setInvoiceTotal(new Float(0.0));
            for (Timesheet ts : selectedItems) {
                totalHours += ts.getHours();
            }
            setInvoiceTotal(totalHours * getRate());
            return "/billing/invoice?faces-redirect=true";
        }
        return null;
    }

    public Period getSelectedPeriod() {
        return selectedPeriod;
    }

    public void setSelectedPeriod(Period p) {
        selectedPeriod = p;
    }
    
    public void changePeriod(AjaxBehaviorEvent evt) {
        try {
            SelectOneMenu menu;
            menu=(SelectOneMenu) evt.getSource();
            String value = menu.getValue().toString();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,Locale.getDefault());
            Date d = df.parse(value);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            invoiceDate = cal.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(TimesheetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Created"));
            recreateModel();
            return "List?faces-redirect=true&clientId=" + Integer.toString(current.getClientID());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Timesheet) getItems().getRowData();
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
        current = (Timesheet) getItems().getRowData();
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
            getPagination().setOrder("desc");
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
     * @return the selectedItems
     */
    public List<Timesheet> getSelectedItems() {
        return selectedItems;
    }

    /**
     * @param selectedItems the selectedItems to set
     */
    public void setSelectedItems(List<Timesheet> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Float getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(Float hours) {
        totalHours = hours;
    }

    public Float getInvoiceTotal() {
        return invoiceTotal;
    }
    
    public void setInvoiceTotal(Float total) {
        invoiceTotal = total;
    }

    /**
     * @return the invoiceDate
     */
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the junk
     */
    public String getJunk() {
        return junk;
    }

    /**
     * @param junk the junk to set
     */
    public void setJunk(String junk) {
        this.junk = junk;
    }

    /**
     * @return the rate
     */
    public Float getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Float rate) {
        this.rate = rate;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @FacesConverter(forClass = Timesheet.class)
    public static class TimesheetControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TimesheetController controller = (TimesheetController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "timesheetController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Timesheet) {
                Timesheet o = (Timesheet) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TimesheetController.class.getName());
            }
        }
    }
}
