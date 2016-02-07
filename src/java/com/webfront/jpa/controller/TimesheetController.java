package com.webfront.jpa.controller;

import com.webfront.beans.ContractorSession;
import com.webfront.beans.TimesheetFacade;
import com.webfront.entity.Periods;
import com.webfront.entity.Timesheet;
import com.webfront.jpa.controller.util.JsfUtil;
import com.webfront.jpa.controller.util.PaginationHelper;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Transient;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean(name = "timesheetController")
@SessionScoped
public class TimesheetController implements Serializable {

    private Timesheet current;
    private DataModel items = null;
    @EJB
    private TimesheetFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Timesheet> selectedItems;
    private Float totalHours;
    private Float invoiceTotal;
    private Float rate;
    private Date invoiceDate;
    private String clientName;
    @Transient
    private Periods selectedPeriod;
    private String junk;

    public TimesheetController() {
        this.selectedItems = new ArrayList();
    }

    public Timesheet getSelected() {
        if (this.current == null) {
            this.current = new Timesheet();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private TimesheetFacade getFacade() {
        return this.ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (this.pagination == null) {
            this.pagination = new PaginationHelper(10) {
                public int getItemsCount() {
                    return TimesheetController.this.getFacade().count();
                }

                public DataModel createPageDataModel() {
                    String dir = getOrder();

                    int[] range = new int[2];
                    range[0] = getPageFirstItem();
                    range[1] = (getPageFirstItem() + getPageSize());
                    ListDataModel list = new ListDataModel(TimesheetController.this.getFacade().findRange(range));
                    return list;
                }
            };
        }
        return this.pagination;
    }

    public void init() {
        recreateModel();
        getPagination().createPageDataModel();
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        this.current = ((Timesheet) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        this.current = new Timesheet();
        this.selectedItemIndex = -1;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ContractorSession sb = (ContractorSession) ec.getSessionMap().get("sessionBean");
        Integer cid = null;
        if (sb != null) {
            cid = sb.getClientId();
        }
        if ((cid != null) && (sb != null)) {
            this.current.setClientID(cid);
            setClientName(getFacade().getClientName(cid));
            if (this.clientName != null) {
                sb.setClientName(this.clientName);
            }
        }
        return "Create?faces-redirect=true";
    }

    public String prepareInvoice() {
        if (!this.selectedItems.isEmpty()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ContractorSession sb = (ContractorSession) ec.getSessionMap().get("sessionBean");
            Integer cid = null;
            if (sb != null) {
                cid = sb.getClientId();
            }
            if (cid != null) {
                setClientName(getFacade().getClientName(cid));
            }
            Date d = getFacade().getPeriod();
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(d);
            cal.add(6, 1);
            setInvoiceDate(cal.getTime());
            setRate(getFacade().getRate());
            setTotalHours(new Float(0.0D));
            setInvoiceTotal(new Float(0.0D));
            for (Timesheet ts : this.selectedItems) {
                this.totalHours = Float.valueOf(this.totalHours.floatValue() + ts.getHours().floatValue());
            }
            setInvoiceTotal(Float.valueOf(this.totalHours.floatValue() * getRate().floatValue()));
            return "/billing/invoice?faces-redirect=true";
        }
        return null;
    }

    public Periods getSelectedPeriod() {
        return this.selectedPeriod;
    }

    public void setSelectedPeriods(Periods p) {
        this.selectedPeriod = p;
    }

    public void changePeriod(AjaxBehaviorEvent evt) {
        try {
            SelectOneMenu menu = (SelectOneMenu) evt.getSource();
            String value = menu.getValue().toString();
            DateFormat df = DateFormat.getDateInstance(3, Locale.getDefault());
            Date d = df.parse(value);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(d);
            cal.add(6, 1);
            this.invoiceDate = cal.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(TimesheetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String create() {
        try {
            getFacade().create(this.current);
            recreateModel();
            return "List?faces-redirect=true&clientId=" + Integer.toString(this.current.getClientID().intValue());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
        return null;
    }

    public String prepareEdit() {
        this.current = ((Timesheet) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Updated"));
            return "List?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
        return null;
    }

    public String destroy() {
        this.current = ((Timesheet) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "View?faces-redirect=true";
        }
        recreateModel();
        return "List?faces-redirect=true";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Deleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (this.selectedItemIndex >= count) {
            this.selectedItemIndex = (count - 1);
            if (this.pagination.getPageFirstItem() >= count) {
                this.pagination.previousPage();
            }
        }
        if (this.selectedItemIndex >= 0) {
            this.current = ((Timesheet) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        if (this.items == null) {
            getPagination().setOrder("desc");
            this.items = getPagination().createPageDataModel();
        }
        return this.items;
    }

    private void recreateModel() {
        this.items = null;
    }

    private void recreatePagination() {
        this.pagination = null;
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
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    public List<Timesheet> getSelectedItems() {
        return this.selectedItems;
    }

    public void setSelectedItems(List<Timesheet> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Float getTotalHours() {
        return this.totalHours;
    }

    public void setTotalHours(Float hours) {
        this.totalHours = hours;
    }

    public Float getInvoiceTotal() {
        return this.invoiceTotal;
    }

    public void setInvoiceTotal(Float total) {
        this.invoiceTotal = total;
    }

    public Date getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getJunk() {
        return this.junk;
    }

    public void setJunk(String junk) {
        this.junk = junk;
    }

    public Float getRate() {
        return this.rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void onRowSelect(SelectEvent event) {
    }

    public void onRowUnselect(UnselectEvent event) {
    }

    @FacesConverter(forClass = Timesheet.class)
    public static class TimesheetControllerConverter
            implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if ((value == null) || (value.length() == 0)) {
                return null;
            }
            TimesheetController controller = (TimesheetController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "timesheetController");
            return controller.ejbFacade.find(getKey(value));
        }

        Integer getKey(String value) {
            Integer key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if ((object instanceof Timesheet)) {
                Timesheet o = (Timesheet) object;
                return getStringKey(o.getId());
            }
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TimesheetController.class.getName());
        }
    }
}
