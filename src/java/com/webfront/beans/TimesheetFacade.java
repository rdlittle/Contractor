/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Timesheet;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
        Map<String, String> map;
        map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String value = map.get("timesheetForm:clientSelector");
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
    public String getNextInv() {
        Query query=getEntityManager().createNamedQuery("SeqControl.findById",Integer.class);
        query.setParameter("id", "NEXT_INV");
        Integer i=(Integer)query.getSingleResult();
        return i.toString();
    }
    
    public TimesheetFacade() {
        super(Timesheet.class);
    }
    
}
