/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.jpa.controller;

import com.webfront.entity.States;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author rlittle
 */
@ManagedBean(name="statesController")
@SessionScoped
public class StatesController implements Serializable {

    @EJB
    private com.webfront.beans.StatesFacade ejbFacade;
    private final ArrayList<States> statesList;

    public StatesController() {
        statesList = new ArrayList<>();
    }
    
    public ArrayList<States> getStatesList() {
        statesList.addAll(getFacade().findAll());
        return statesList;
    }

    /**
     * @return the ejbFacade
     */
    public com.webfront.beans.StatesFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @param ejbFacade the ejbFacade to set
     */
    public void setFacade(com.webfront.beans.StatesFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
}
