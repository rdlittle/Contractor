/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Rates;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rlittle
 */
@Stateless


public class RatesFacade extends AbstractFacade<Rates> {
    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RatesFacade() {
        super(Rates.class);
    }
    
}
