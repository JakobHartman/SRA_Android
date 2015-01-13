package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/14/14.
 */
public class LoginObject implements Serializable {
    private String username;
    private ArrayList<String> permissions;
    private CountryLogin countryLogin;
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

    public CountryLogin getCountryLogin() {
        return countryLogin;
    }

    public void setCountryLogin(CountryLogin countryLogin) {
        this.countryLogin = countryLogin;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginObject(){
        permissions = new ArrayList<String>();
    }
}
