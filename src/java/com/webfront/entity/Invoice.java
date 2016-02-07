/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "invoices")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id"),
    @NamedQuery(name = "Invoice.findByInvDate", query = "SELECT i FROM Invoice i WHERE i.invDate = :invDate"),
    @NamedQuery(name = "Invoice.findByInvoice", query = "SELECT i FROM Invoice i WHERE i.invoice = :invoice"),
    @NamedQuery(name = "Invoice.findByAmount", query = "SELECT i FROM Invoice i WHERE i.amount = :amount"),
    @NamedQuery(name = "Invoice.findByClient", query = "SELECT i FROM Invoice i WHERE i.client = :client"),
    @NamedQuery(name = "Invoice.findByPosted", query = "SELECT i FROM Invoice i WHERE i.posted = :posted"),
    @NamedQuery(name = "Invoice.findByPaid", query = "SELECT i FROM Invoice i WHERE i.paid = :paid"),
    @NamedQuery(name = "Invoice.findByCheckNum", query = "SELECT i FROM Invoice i WHERE i.checkNum = :checkNum")})
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "inv_date")
    @Temporal(TemporalType.DATE)
    private Date invDate;

    @Size(max = 100)
    @Column(name = "invoice")
    private String invoice;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Float amount;
    @Column(name = "client")
    private Integer client;
    @Column(name = "posted")
    private Boolean posted;
    @Column(name = "paid")
    private Boolean paid;
    @Size(max = 100)
    @Column(name = "check_num")
    private String checkNum;
    @Column(name="period_num")
    private String periodNum;
    
    public Invoice() {
    }
    
    public Invoice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDate() {
        SimpleDateFormat fmt=new SimpleDateFormat("MM/dd/yy");
        String d=fmt.format(getInvDate());
        return d;
    }
    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "com.webfront.entity.Invoice[ id=" + id + " ]";
//    }

    /**
     * @return the periodNum
     */
    public String getPeriodNum() {
        return periodNum;
    }

    /**
     * @param periodNum the periodNum to set
     */
    public void setPeriodNum(String periodNum) {
        this.periodNum = periodNum;
    }
    
}
