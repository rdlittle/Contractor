/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.jpa.controller;

import com.webfront.entity.Rates;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author rlittle
 */
@ManagedBean(name="ratesController")
@SessionScoped
public class RatesController implements Serializable {

    @EJB
    private com.webfront.beans.RatesFacade ejbFacade;
    private final ArrayList<Rates> ratesList;

    public RatesController() {
        ratesList = new ArrayList<>();
    }
    
    public ArrayList<Rates> getRatesList() {
        ratesList.addAll(getFacade().findAll());
        return ratesList;
    }

    /**
     * @return the ejbFacade
     */
    public com.webfront.beans.RatesFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @param ejbFacade the ejbFacade to set
     */
    public void setFacade(com.webfront.beans.RatesFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
}
