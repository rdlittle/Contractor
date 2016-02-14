/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByCompanyID", query = "SELECT c FROM Company c WHERE c.companyID = :companyID"),
    @NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "Company.findByAddress1", query = "SELECT c FROM Company c WHERE c.address1 = :address1"),
    @NamedQuery(name = "Company.findByAddress2", query = "SELECT c FROM Company c WHERE c.address2 = :address2"),
    @NamedQuery(name = "Company.findByCity", query = "SELECT c FROM Company c WHERE c.city = :city"),
    @NamedQuery(name = "Company.findByState", query = "SELECT c FROM Company c WHERE c.state = :state"),
    @NamedQuery(name = "Company.findByZip", query = "SELECT c FROM Company c WHERE c.zip = :zip"),
    @NamedQuery(name = "Company.findByPhone1", query = "SELECT c FROM Company c WHERE c.phone1 = :phone1"),
    @NamedQuery(name = "Company.findByPhone2", query = "SELECT c FROM Company c WHERE c.phone2 = :phone2"),
    @NamedQuery(name = "Company.findByAttn", query = "SELECT c FROM Company c WHERE c.attn = :attn"),
    @NamedQuery(name = "Company.findByEmail", query = "SELECT c FROM Company c WHERE c.email = :email")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "companyID")
    private Integer companyID;
    @Size(max = 100)
    @Column(name = "companyName")
    private String companyName;
    @Size(max = 100)
    @Column(name = "address1")
    private String address1;
    @Size(max = 100)
    @Column(name = "address2")
    private String address2;
    @Size(max = 100)
    @Column(name = "city")
    private String city;
    
    @OneToOne
    @JoinColumn(name = "state")
    private States state;
    @Size(max = 100)
    @Column(name = "zip")
    private String zip;
    @Size(max = 100)
    @Column(name = "phone1")
    private String phone1;
    @Size(max = 100)
    @Column(name = "phone2")
    private String phone2;
    @Size(max = 100)
    @Column(name = "attn")
    private String attn;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;

    public Company() {
        state = new States();
    }

    public Company(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public States getState() {
        if(state==null) {
            state = new States();
        }
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyID != null ? companyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.companyID == null && other.companyID != null) || (this.companyID != null && !this.companyID.equals(other.companyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.entity.Company[ companyID=" + companyID + " ]";
    }
    
}
