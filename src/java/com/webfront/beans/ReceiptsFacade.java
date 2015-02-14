/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Receipts;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
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
public class ReceiptsFacade extends AbstractFacade<Receipts> {

    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Receipts> findRange(int[] range) {
        Integer clientId=null;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ContractorSession sb = (ContractorSession) map.get("sessionBean");
        if (sb.clientId != null) {
            clientId = sb.clientId;
        }
        String stmt = "SELECT r FROM Receipts r WHERE r.payeeId = ?1 ORDER BY r.id DESC";

        Query query = getEntityManager().createQuery(stmt, Receipts.class);
        if (clientId != null) {
            query.setParameter(1, clientId);
            query.setMaxResults(range[1] - range[0]);
            query.setFirstResult(range[0]);
            return query.getResultList();
        }
        return null;
    }

    public ReceiptsFacade() {
        super(Receipts.class);
    }
}
