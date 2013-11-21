/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "receipts")
@NamedQueries({
    @NamedQuery(name = "Receipts.findAll", query = "SELECT r FROM Receipts r"),
    @NamedQuery(name = "Receipts.findById", query = "SELECT r FROM Receipts r WHERE r.id = :id"),
    @NamedQuery(name = "Receipts.findByRecdDate", query = "SELECT r FROM Receipts r WHERE r.recdDate = :recdDate"),
    @NamedQuery(name = "Receipts.findByCheckNum", query = "SELECT r FROM Receipts r WHERE r.checkNum = :checkNum"),
    @NamedQuery(name = "Receipts.findByAmount", query = "SELECT r FROM Receipts r WHERE r.amount = :amount"),
    @NamedQuery(name = "Receipts.findByPayeeId", query = "SELECT r FROM Receipts r WHERE r.payeeId = :payeeId")})
public class Receipts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "NEXT_RECEIPT",
            allocationSize=1,
            table="seqcontrol", 
            pkColumnName="ID",
            pkColumnValue="NEXT_RECEIPT",
            valueColumnName="nextSeq")
    @GeneratedValue(generator = "NEXT_RECEIPT", strategy=GenerationType.TABLE)
    @Column(name = "id")
    private Integer id;
    @Column(name = "recd_date")
    @Temporal(TemporalType.DATE)
    private Date recdDate;
    @Size(max = 100)
    @Column(name = "check_num")
    private String checkNum;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Float amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payeeId")
    private int payeeId;

    public Receipts() {
    }

    public Receipts(Integer id) {
        this.id = id;
    }

    public Receipts(Integer id, int payeeId) {
        this.id = id;
        this.payeeId = payeeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecdDate() {
        return recdDate;
    }

    public void setRecdDate(Date recdDate) {
        this.recdDate = recdDate;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
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
        if (!(object instanceof Receipts)) {
            return false;
        }
        Receipts other = (Receipts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.beans.Receipts[ id=" + id + " ]";
    }
    
}
