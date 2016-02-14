package com.webfront.beans;

import com.webfront.entity.Client;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private final PropertyChangeSupport pcs;
    private final String clientProperty =  "CLIENT_PROPERTY";

    public ClientBean() {
        this.pcs = new PropertyChangeSupport(this);
        this.client = new Client();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(propertyName, listener);
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
        Client oldClient = this.client;
        this.client = client;
        if(oldClient != this.client) {
            pcs.firePropertyChange(clientProperty, oldClient, client);
        }
    }

}
