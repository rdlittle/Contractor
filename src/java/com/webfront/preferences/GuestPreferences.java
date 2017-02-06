/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.preferences;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rlittle
 */
@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String theme = "aristo";

    public void init() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> map = etx.getRequestCookieMap();
        if (map.containsKey("globaltheme")) {
            Cookie cookie = (Cookie) map.get("globaltheme");
            theme = (String) cookie.getValue();
            changeTheme();
        }
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void changeTheme() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) etx.getRequest();
        Map<String, Object> map = etx.getRequestCookieMap();
        Map<String, Object> properties = new HashMap<>();
        Cookie cookie = null;
       if(map.containsKey("globaltheme")) {
            cookie = (Cookie) map.get("globaltheme");
        } else {
            cookie = new Cookie("globaltheme",theme);
        }
        cookie.setValue(theme);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE);
        properties.put("maxAge", Integer.MAX_VALUE);
        properties.put("path", "/");
        HttpServletResponse res = (HttpServletResponse)etx.getResponse();
        res.addCookie(cookie);
    }
}
