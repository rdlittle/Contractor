/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.service;

import com.webfront.entity.States;
import com.webfront.jpa.controller.StatesController;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author rlittle
 */
@ManagedBean(name="statesService")
@SessionScoped
public class StatesService {
    private ArrayList<States> statesList;
    @ManagedProperty(value="#{statesController}")
    private StatesController controller;
    
    public StatesService() {
        statesList = new ArrayList<>();
    }
    @PostConstruct
    public void init() {
        statesList = new ArrayList<>();
    }

    /**
     * @return the statesList
     */
    public ArrayList<States> getStatesList() {
        statesList.addAll(getController().getStatesList());
        return statesList;
    }

    /**
     * @param statesList the statesList to set
     */
    public void setStatesList(ArrayList<States> statesList) {
        this.statesList = statesList;
    }

    /**
     * @return the controller
     */
    public StatesController getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(StatesController controller) {
        this.controller = controller;
    }
    
}
