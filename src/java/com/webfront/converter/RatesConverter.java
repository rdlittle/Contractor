/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.converter;

import com.webfront.entity.Rates;
import com.webfront.jpa.controller.RatesController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
    @FacesConverter(forClass = Rates.class)
    public class RatesConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String id) {
            if(id==null) {
                return null;
            }
            RatesController controller = (RatesController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "ratesController");
            Rates rates = controller.getFacade().find(id);
            if(rates==null) {
                return null;
            }
            return rates;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object object) {
            if(object==null) {
                return null;
            }
            if(object instanceof Rates) {
                Rates s = (Rates) object;
                if(s.getId()==null) {
                    return null;
                }
                return (s.getId().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + RatesController.class.getName());
            }
        }
    }
    
