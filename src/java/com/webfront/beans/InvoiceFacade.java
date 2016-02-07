/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Client;
import com.webfront.entity.Invoice;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author rlittle
 */
@Stateless
public class InvoiceFacade extends AbstractFacade<Invoice> {

    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Invoice> findRange(int[] range) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ClientBean sb = (ClientBean) map.get("clientBean");
        if(sb == null) {
            return null;
        }
        if (sb.getClientId() != null) {
            setClientId(sb.getClientId());
        }
        String stmt = "SELECT i FROM Invoice i WHERE i.client = ?1 ORDER BY i.id DESC";
        Query query = getEntityManager().createQuery(stmt, Invoice.class);
        if (getClientId() != null) {
            query.setParameter(1, getClientId());
            query.setMaxResults(range[1] - range[0]);
            query.setFirstResult(range[0]);
            return query.getResultList();
        }
        return null;
    }

    public List<Invoice> findAllByDate() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
        Root<Invoice> from = criteriaQuery.from(Invoice.class);
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("id")));
        Query query = getEntityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Invoice> findClientInvoices() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ClientBean sb = (ClientBean) map.get("clientBean");
        if (sb != null && sb.getClientId() != null) {
            setClientId(sb.getClientId());
            String stmt = "SELECT i FROM Invoice i WHERE i.client = ?1 ORDER BY i.id DESC";
            Query query = getEntityManager().createQuery(stmt, Invoice.class);
            if (getClientId() != null) {
                query.setParameter(1, getClientId());
                return query.getResultList();
            }
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getClientName(Integer id) {
        Query query = getEntityManager().createNamedQuery("Client.findById", Client.class);
        query.setParameter("id", id);
        Client c = (Client) query.getSingleResult();
        return c.getName();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getNextInv() {
        Query query = getEntityManager().createNamedQuery("SeqControl.findById", Integer.class);
        query.setParameter("id", "NEXT_INV");
        Integer i = (Integer) query.getSingleResult();
        return i.toString();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getNextPeriod() {
        Query query = getEntityManager().createNamedQuery("SeqControl.findById", Integer.class);
        query.setParameter("id", "NEXT_BILLING_PERIOD");
        Integer i = (Integer) query.getSingleResult();
        return i.toString();
    }

    public void setNextInv(int invNum) {
        Query query = getEntityManager().createNamedQuery("SeqControl.updateSeq", Integer.class);
        query.setParameter("nextSeq", invNum);
        query.setParameter("id", "NEXT_INV");
        query.executeUpdate();
    }

    public InvoiceFacade() {
        super(Invoice.class);
    }

}
