/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.States;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rlittle
 */
@Stateless


public class StatesFacade extends AbstractFacade<States> {
    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatesFacade() {
        super(States.class);
    }
    
}
