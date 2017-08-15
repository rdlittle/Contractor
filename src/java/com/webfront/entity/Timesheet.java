/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "timesheet")
@NamedQueries({
    @NamedQuery(name = "Timesheet.findAll", query = "SELECT t FROM Timesheet t"),
    @NamedQuery(name = "Timesheet.findById", query = "SELECT t FROM Timesheet t WHERE t.id = :id"),
    @NamedQuery(name = "Timesheet.findByDate", query = "SELECT t FROM Timesheet t WHERE t.date = :date"),
    @NamedQuery(name = "Timesheet.findByDescription", query = "SELECT t FROM Timesheet t WHERE t.description = :description"),
    @NamedQuery(name = "Timesheet.findByHours", query = "SELECT t FROM Timesheet t WHERE t.hours = :hours"),
    @NamedQuery(name = "Timesheet.findByClientID", query = "SELECT t FROM Timesheet t WHERE t.clientID = :clientID"),
    @NamedQuery(name = "Timesheet.findByInvNum", query = "SELECT t FROM Timesheet t WHERE t.invNum = :invNum"),
    @NamedQuery(name = "Timesheet.findTotalUnpostedHours", query = "SELECT SUM(t.hours) FROM Timesheet t WHERE t.clientID = :clientID AND t.posted = :posted"),
    @NamedQuery(name = "Timesheet.findByPosted", query = "SELECT t FROM Timesheet t WHERE t.posted = :posted")})
public class Timesheet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    //<editor-fold defaultstate="collapsed" desc="comment">
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    //</editor-fold>
    @Column(name = "id")
    private Integer id;
    @Column(name = "entryDate")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "hours")
    private Float hours;
    @Column(name = "clientID")
    private Integer clientID;
    @Column(name = "posted")
    private Boolean posted;
    @Column(name="invNum")
    private String invNum;
    
    public Timesheet() {
        this.id=null;
        this.invNum="";
        this.posted=false;
    }

    public Timesheet(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
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
        if (!(object instanceof Timesheet)) {
            return false;
        }
        Timesheet other = (Timesheet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.entity.Timesheet[ id=" + id + " ]";
    }

    /**
     * @return the invNum
     */
    public String getInvNum() {
        if(invNum.isEmpty() || invNum.equals("")) {
            String invn=getNextInv();
            setInvNum(invn);
        }
        return invNum;
    }

    /**
     * @param invNum the invNum to set
     */
    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }
    
    public String getNextInv() {
        return "";
    }
    
}
