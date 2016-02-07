package com.webfront.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class ContractorSession implements Serializable {

    protected Integer clientId;
    private String clientName;

    public Integer getClientId() {
        return this.clientId;
    }

    public void setClientId(Integer cid) {
        this.clientId = cid;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
