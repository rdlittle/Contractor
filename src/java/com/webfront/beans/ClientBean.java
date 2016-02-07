package com.webfront.beans;

import com.webfront.entity.Client;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author rlittle
 */
@ManagedBean(name = "clientBean")
@SessionScoped
public class ClientBean implements Serializable {

    private Client client;
    
    public ClientBean() {
        this.client=new Client();
    }

    public Integer getClientId() {
        return this.client.getId();
    }

    public void setClientId(Integer cid) {
        this.client.setId(cid);
    }

    public String getClientName() {
        return this.client.getName();
    }

    public void setClientName(String clientName) {
        this.client.setName(clientName);
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

}
