/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Client;
import com.webfront.entity.Receipts;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ClientBean sb = (ClientBean) map.get("clientBean");
        if (sb == null) {
            return null;
        }
        if (sb.getClient() == null) {
            return null;
        }
        Client client = sb.getClient();

        String stmt = "SELECT r FROM Receipts r WHERE r.payeeId = ?1 ORDER BY r.id DESC";

        Query query = getEntityManager().createQuery(stmt, Receipts.class);
        if (client.getId() != null) {
            query.setParameter(1, client.getId());
            query.setMaxResults(range[1] - range[0]);
            query.setFirstResult(range[0]);
            return query.getResultList();
        }
        return null;
    }

    public List<Receipts> getList(Date date) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext et = fc.getExternalContext();
        Map<String, Object> map = et.getSessionMap();
        ClientBean sb = (ClientBean) map.get("clientBean");
        if (sb == null) {
            return null;
        }
        if (sb.getClient() == null) {
            return null;
        }
        Client client = sb.getClient();
        
        SimpleDateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateParam = dfmt.format(date);
        System.out.println("invoice date = "+dateParam);

        String stmt = "SELECT r FROM Receipts r WHERE r.payeeId = ?1 AND r.recdDate >= ?2";

        Query query = getEntityManager().createQuery(stmt, Receipts.class);
        if (client.getId() != null) {
            query.setParameter(1, client.getId());
            query.setParameter(2, date);
            query.setFirstResult(0);
            query.setMaxResults(3);
            return query.getResultList();
        }
        return null;
    }

    public ReceiptsFacade() {
        super(Receipts.class);
    }
}
