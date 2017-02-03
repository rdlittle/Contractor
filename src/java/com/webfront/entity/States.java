/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author rlittle
 */
@Entity
@Table(name = "states")
@NamedQueries({
    @NamedQuery(name = "States.findAll", query = "SELECT s FROM States s"),
    @NamedQuery(name = "States.findByAbbreviation", query = "SELECT s FROM States s WHERE s.abbreviation = :abbreviation"),
    @NamedQuery(name = "States.findByName", query = "SELECT s FROM States s WHERE s.name = :name"),})
public class States implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "name")
    private String name;

    public States() {
        abbreviation = "";
        name = "";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbr) {
        this.abbreviation = abbr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (abbreviation != null ? abbreviation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof States)) {
            return false;
        }
        States other = (States) object;
        if ((this.abbreviation == null && other.abbreviation != null) || (this.abbreviation != null && !this.abbreviation.equals(other.abbreviation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.entity.States[ id=" + abbreviation + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
