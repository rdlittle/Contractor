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
@Table(name = "seqcontrol")
@NamedQueries({
    @NamedQuery(name = "SeqControl.findAll", query = "SELECT s FROM SeqControl s"),
    @NamedQuery(name = "SeqControl.findById", query = "SELECT s.nextSeq FROM SeqControl s WHERE s.id = :id"),
    @NamedQuery(name = "SeqControl.updateSeq", query = "UPDATE SeqControl s SET s.nextSeq = :nextSeq WHERE s.id = :id"),
    @NamedQuery(name = "SeqControl.findByNextSeq", query = "SELECT s FROM SeqControl s WHERE s.nextSeq = :nextSeq")
    })
public class SeqControl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id")
    private String id;
    @Column(name = "nextSeq")
    private Integer nextSeq;

    public SeqControl() {
    }

    public SeqControl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNextSeq() {
        return nextSeq;
    }
    
    public Integer getNextSeq(String id) {
        return null;
    }

    public void setNextSeq(Integer nextSeq) {
        this.nextSeq = nextSeq;
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
        if (!(object instanceof SeqControl)) {
            return false;
        }
        SeqControl other = (SeqControl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.webfront.entity.SeqControl[ id=" + id + " ]";
    }
    
}
