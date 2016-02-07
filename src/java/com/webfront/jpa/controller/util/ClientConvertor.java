/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.jpa.controller.util;

import com.webfront.entity.Client;
import com.webfront.jpa.controller.ClientController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
    @FacesConverter(forClass = Client.class)
    public class ClientConvertor implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String clientId) {
            if(clientId==null) {
                return null;
            }
            ClientController controller = (ClientController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "clientController");
            Integer i = Integer.parseInt(clientId);
            Client client = controller.getFacade().find(i);
            if(client==null) {
                return null;
            }
            return client;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object object) {
            if(object==null) {
                return null;
            }
            if(object instanceof Client) {
                Client c = (Client) object;
                if(c.getId()==null) {
                    return null;
                }
                return (c.getId().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ClientController.class.getName());
            }
        }
    }
    
