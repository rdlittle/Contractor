/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "periods")
@NamedQueries({
    @NamedQuery(name = "Periods.findAll", query = "SELECT p FROM Periods p ORDER BY p.endDate DESC"),
    @NamedQuery(name = "Periods.findById", query = "SELECT p FROM Periods p WHERE p.id = :id"),
    @NamedQuery(name = "Periods.findByStartDate", query = "SELECT p FROM Periods p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "Periods.findByEndDate", query = "SELECT p FROM Periods p WHERE p.endDate = :endDate")})
public class Periods implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "NEXT_BILLING_PERIOD",
            allocationSize=1,
            table="seqcontrol", 
            pkColumnName="ID",
            pkColumnValue="NEXT_BILLING_PERIOD",
            valueColumnName="nextSeq")
    @GeneratedValue(generator = "NEXT_BILLING_PERIOD", strategy=GenerationType.TABLE)
    @Column(name = "id")
    private Integer id;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    public Periods() {
    }

    public Periods(Integer id) {
        this.id = id;
    }

    public Periods(Integer id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    
    public String asString(Date d, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        return format.format(d);
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        if (!(object instanceof Periods)) {
            return false;
        }
        Periods other = (Periods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(this.endDate);
    }
    
}
