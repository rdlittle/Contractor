/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.converter;

import com.webfront.entity.Company;
import com.webfront.jpa.controller.CompanyController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
    @FacesConverter(forClass = Company.class)
    public class CompanyConvertor implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String companyId) {
            if(companyId==null) {
                return null;
            }
            CompanyController controller = (CompanyController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "companyController");
            Integer i = Integer.parseInt(companyId);
            Company company = controller.getFacade().find(i);
            if(company==null) {
                return null;
            }
            return company;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object object) {
            if(object==null) {
                return null;
            }
            if(object instanceof Company) {
                Company c = (Company) object;
                if(c.getCompanyID()==null) {
                    return null;
                }
                return (c.getCompanyID().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CompanyController.class.getName());
            }
        }
    }
    
