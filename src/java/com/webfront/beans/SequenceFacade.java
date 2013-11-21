/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.beans;
import com.webfront.entity.SeqControl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author rlittle
 */
@Stateless
public class SequenceFacade extends AbstractFacade<SeqControl> {
    @PersistenceContext(unitName = "ContractorPU")
    private EntityManager em;
    private String nextSeq;
    private int nextInvoice;
    private int nextReceipt;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public int getNextSequence(String id) {
        TypedQuery<SeqControl> sq=em.createNamedQuery("SeqControl.findById",SeqControl.class);
        sq.setParameter("id", id);
        SeqControl seq=sq.getSingleResult();
        int nextseq=seq.getNextSeq();
        return nextseq;
    }
    
    public SequenceFacade() {
        super(SeqControl.class);
    }

    /**
     * @return the nextSeq
     */
    public String getNextSeq() {
        return nextSeq;
    }

    /**
     * @param nextSeq the nextSeq to set
     */
    public void setNextSeq(String nextSeq) {
        this.nextSeq = nextSeq;
    }

    /**
     * @return the nextInvoice
     */
    public String getNextInvoice() {
        setNextInvoice(Integer.toString(getNextSequence("NEXT_INV")));
        return Integer.toString(nextInvoice);
    }

    /**
     * @param nextInvoice the nextInvoice to set
     */
    public void setNextInvoice(String nextInvoice) {
        this.nextInvoice = Integer.parseInt(nextInvoice);
    }

    /**
     * @return the nextReceipt
     */
    public String getNextReceipt() {
        setNextReceipt(Integer.toString(getNextSequence("NEXT_RECEIPT")));
        return Integer.toString(nextReceipt);
    }

    /**
     * @param nextReceipt the nextReceipt to set
     */
    public void setNextReceipt(String nextReceipt) {
        this.nextReceipt = Integer.parseInt(nextReceipt);
    }
}
