/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.servlet;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Named;

/**
 *
 * @author rlittle
 */
@Named("ph")
public class PhaseListener implements javax.faces.event.PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        System.out.println("afterPhase");
    }

    @Override
    public void beforePhase(PhaseEvent event) {
//        System.out.println("beforePhase");
    }

    @Override
    public PhaseId getPhaseId() {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getViewRoot().findComponent("clientSelector");
        return PhaseId.UPDATE_MODEL_VALUES;
    }
    
    public boolean isPostBack() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.isPostback();
    }
    
}
