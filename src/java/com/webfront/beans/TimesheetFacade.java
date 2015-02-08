/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Client;
import com.webfront.entity.Timesheet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rlittle
 */
@Stateless
public class TimesheetFacade extends AbstractFacade<Timesheet> {
    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Timesheet> findRange(int[] range) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, String> map;
        map = et.getRequestParameterMap();
        String value = map.get("timesheetForm:clientSelector");
        if (value == null) {
            value = map.get("clientId");
        }
       
        Integer clientId;
        String stmt = "SELECT t FROM Timesheet t WHERE t.clientID = ?1 ORDER BY t.id DESC";

        Query query = getEntityManager().createQuery(stmt, Timesheet.class);
        if (value != null) {
            clientId = Integer.valueOf(value);
            query.setParameter(1, clientId.intValue());
            query.setMaxResults(range[1] - range[0]);
            query.setFirstResult(range[0]);
            return query.getResultList();
        }
        return null;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getClientName(Integer id) {
        Query query=getEntityManager().createNamedQuery("Client.findById",Client.class);
        query.setParameter("id",id);
        Client c = (Client) query.getSingleResult();
        return c.getName();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getNextInv() {
        Query query=getEntityManager().createNamedQuery("SeqControl.findById",Integer.class);
        query.setParameter("id", "NEXT_INV");
        Integer i=(Integer)query.getSingleResult()+1;
        return i.toString();
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Date getPeriod() {
        String sql = "select p.end_date from periods p order by p.id desc limit 0,1";
        Query query = getEntityManager().createNativeQuery(sql);
        Date d = (Date) query.getSingleResult();
        return d;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Float getRate() {
        Map<String, String> map;
        map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String value = map.get("timesheetForm:clientSelector");
        Integer clientId = Integer.parseInt(value);
        String sql = "select r.rate from rates r inner join client c on r.id = c.rate where c.id = "+clientId;
        Query query = getEntityManager().createNativeQuery(sql);
        Float rate = (Float)query.getSingleResult();
        return rate;
    }
    
    public TimesheetFacade() {
        super(Timesheet.class);
    }
    
}
