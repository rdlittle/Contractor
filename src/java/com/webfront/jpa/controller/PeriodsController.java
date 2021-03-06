package com.webfront.jpa.controller;

import com.webfront.beans.PeriodsFacade;
import com.webfront.entity.Periods;
import com.webfront.jpa.controller.util.JsfUtil;
import com.webfront.jpa.controller.util.PaginationHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "periodsController")
@SessionScoped
public class PeriodsController implements Serializable {

    private Periods current;
    private Periods newPeriod;
    private DataModel items = null;
    @EJB
    private com.webfront.beans.PeriodsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PeriodsController() {
    }

    public Periods getSelected() {
        if (current == null) {
            current = new Periods();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PeriodsFacade getFacade() {
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
        current = (Periods) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Periods();
        selectedItemIndex = -1;
        return "Create?faces-redirect=true";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Created"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public void newPeriod() {
        RequestContext r = RequestContext.getCurrentInstance();
        Map<String,Object> params=new HashMap<>();
        params.put("model", true);
        params.put("contentWidth",400);
        params.put("contentHeight", 300);
        r.openDialog("newPeriod",params,null);
        setNewPeriod(new Periods());
    }

    public void selectNewPeriodFromDialog(Periods p) {
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    public void closeDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
        newPeriod = null;
    }

    public void onPeriodCreated(SelectEvent evt) {
        if (newPeriod != null) {
            current = newPeriod;
            getFacade().create(current);
            current = new Periods();
            recreateModel();
        }
    }

    public void createNew(ActionEvent evt) {
        getFacade().create(current);
        current = new Periods();
        recreateModel();
    }

    public String prepareEdit() {
        current = (Periods) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Messages").getString("Updated"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Messages").getString("PersistenceError"));
            return null;
        }
    }

    public String destroy() {
        current = (Periods) getItems().getRowData();
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

    public ArrayList<Periods> getItemsAsArrayList() {
        ArrayList<Periods> newList = new ArrayList<>();
        newList.addAll((List<Periods>) getItems().getWrappedData());
        newList.sort(new Comparator<Periods>() {
            @Override
            public int compare(Periods p1, Periods p2) {
                Date d1 = p1.getEndDate();
                Date d2 = p2.getEndDate();
                return d2.compareTo(d1);
            }
        });
        return newList;
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

    public void changePeriod(final AjaxBehaviorEvent evt) {
        System.out.println(evt.getSource().toString());
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        SelectItem[] itemList = JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * @return the newPeriod
     */
    public Periods getNewPeriod() {
        return newPeriod;
    }

    /**
     * @param newPeriod the newPeriod to set
     */
    public void setNewPeriod(Periods newPeriod) {
        this.newPeriod = newPeriod;
    }

    @FacesConverter(forClass = Periods.class)
    public static class PeriodsControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PeriodsController controller = (PeriodsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "periodsController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Periods) {
                Periods o = (Periods) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PeriodsController.class.getName());
            }
        }
    }
}
