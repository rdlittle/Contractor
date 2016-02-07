/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;

import com.webfront.entity.Client;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author rlittle
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
    private String order;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.order = "asc";
    }

    @ManagedProperty(value = "#{clientBean.clientId}")
    private Integer clientId;

    protected abstract EntityManager getEntityManager();

    public void setOrder(String ord) {
        this.order = ord;
    }

    public String getOrder() {
        return this.order;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        //javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
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
        return client.getId();
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

}
