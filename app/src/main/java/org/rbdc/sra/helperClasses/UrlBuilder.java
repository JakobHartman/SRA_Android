package org.rbdc.sra.helperClasses;

import org.rbdc.sra.objects.Area;

import java.util.Random;

/**
 * Created by imac on 1/14/15.
 */
public class UrlBuilder {

    private static UrlBuilder instance = null;
    private static String org = null;
    protected UrlBuilder() {
        // Exists only to defeat instantiation.
    }

    public static String getOrg() {
        return org;
    }

    public static void setOrg(String org) {
        UrlBuilder.org = org;
    }

    public static UrlBuilder getInstance() {
        if(instance == null) {
            instance = new UrlBuilder();
        }
        return instance;
    }

    public static String buildAreaUrl(Area area){
        String url;
        url = "https://intense-inferno-7741.firebaseio.com/Organizations" + org + "/Countries/" + area.getCountry() + "/Regions/" + area.getRegion() + "/Areas/" + area.getName();
        return url;
    }

    public static String buildHouseholdUrl(){
        String url;
            url = "";
        return url;
    }

    public static String buildUUID(){
        String uuid;
        uuid = "";
        final String set = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int length = set.length();
        Random r = new Random();
        uuid += "-";
        for(int i = 0; i < 19;i++){
            uuid += set.charAt(r.nextInt(length));
        }
        return uuid;
    }

}