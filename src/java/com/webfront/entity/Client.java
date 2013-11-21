/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "client")
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id"),
    @NamedQuery(name = "Client.findByName", query = "SELECT c FROM Client c WHERE c.name = :name"),
    @NamedQuery(name = "Client.findByAddress1", query = "SELECT c FROM Client c WHERE c.address1 = :address1"),
    @NamedQuery(name = "Client.findByAddress2", query = "SELECT c FROM Client c WHERE c.address2 = :address2"),
    @NamedQuery(name = "Client.findByAddress3", query = "SELECT c FROM Client c WHERE c.address3 = :address3"),
    @NamedQuery(name = "Client.findByCity", query = "SELECT c FROM Client c WHERE c.city = :city"),
    @NamedQuery(name = "Client.findByState", query = "SELECT c FROM Client c WHERE c.state = :state"),
    @NamedQuery(name = "Client.findByZip", query = "SELECT c FROM Client c WHERE c.zip = :zip"),
    @NamedQuery(name = "Client.findByAttn", query = "SELECT c FROM Client c WHERE c.attn = :attn"),
    @NamedQuery(name = "Client.findByRate", query = "SELECT c FROM Client c WHERE c.rate = :rate")})
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 25)
    @Column(name = "address1")
    private String address1;
    @Size(max = 25)
    @Column(name = "address2")
    private String address2;
    @Size(max = 20)
    @Column(name = "address3")
    private String address3;
    @Size(max = 25)
    @Column(name = "city")
    private String city;
    @Size(max = 2)
    @Column(name = "state")
    private String state;
    @Size(max = 10)
    @Column(name = "zip")
    private String zip;
    @Size(max = 30)
    @Column(name = "attn")
    private String attn;
    @Column(name = "rate")
    private Integer rate;

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.entity.Client[ id=" + id + " ]";
    }
    
}
