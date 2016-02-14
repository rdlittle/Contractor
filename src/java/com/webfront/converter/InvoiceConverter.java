/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.converter;

import com.webfront.entity.Invoice;
import com.webfront.jpa.controller.InvoiceController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
@FacesConverter(forClass = Invoice.class)
public class InvoiceConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        InvoiceController controller = (InvoiceController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "InvoiceController");
        Invoice i = controller.getFacade().find(getKey(value));
        return i;
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

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Invoice) {
            Invoice o = (Invoice) object;
            if(o.getId()==null) {
                return null;
            }
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + InvoiceController.class.getName());
        }
    }
}
