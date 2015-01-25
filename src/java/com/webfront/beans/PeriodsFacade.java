/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Periods;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author rlittle
 */
@Stateless
public class PeriodsFacade extends AbstractFacade<Periods> {
    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
        public List<Periods> findRange(int[] range) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Periods> criteriaQuery = criteriaBuilder.createQuery(Periods.class);
        Root<Periods> from=criteriaQuery.from(Periods.class);
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("endDate")));
        Query query=getEntityManager().createQuery(criteriaQuery);
        query.setMaxResults(range[1]-range[0]);
        query.setFirstResult(range[0]);
        List<Periods> list=query.getResultList();
        return list;
    }
        
    public PeriodsFacade() {
        super(Periods.class);
    }
    
    public Periods getNextPeriod() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Periods> criteriaQuery = criteriaBuilder.createQuery(Periods.class);
        Root<Periods> from=criteriaQuery.from(Periods.class);
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("endDate")));
        Query query=getEntityManager().createQuery(criteriaQuery);
        query.setMaxResults(1);
        query.setFirstResult(0);
        return (Periods)query.getSingleResult();
    }
    
}
