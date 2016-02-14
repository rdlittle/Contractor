/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.converter;

import com.webfront.entity.States;
import com.webfront.jpa.controller.StatesController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
    @FacesConverter(forClass = States.class)
    public class StatesConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String abbreviation) {
            if(abbreviation==null) {
                return null;
            }
            StatesController controller = (StatesController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "statesController");
            States states = controller.getFacade().find(abbreviation);
            if(states==null) {
                return null;
            }
            return states;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object object) {
            if(object==null) {
                return null;
            }
            if(object instanceof States) {
                States s = (States) object;
                if(s.getAbbreviation()==null) {
                    return null;
                }
                return (s.getAbbreviation().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + StatesController.class.getName());
            }
        }
    }
    
