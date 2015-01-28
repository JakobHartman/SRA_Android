package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/14/14.
 */
public class LoginObject implements Serializable {
    private String username;
    private ArrayList<String> permissions;
    private SiteLogin siteLogin;
    private boolean loggedIn;

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void addToRoles(String addition){
        permissions.add(addition);
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public SiteLogin getSiteLogin() {
        return siteLogin;
    }

    public void setSiteLogin(SiteLogin siteLogin) {
        this.siteLogin = siteLogin;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginObject(){
        siteLogin = new SiteLogin();
        permissions = new ArrayList<String>();
    }
}
