package org.rbdc.sra.helperClasses;

import android.util.Log;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Member;

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

    public static String buildAreaUrl(Area area) {
        String url;
        url = "https://intense-inferno-7741.firebaseio.com/organizations/" + org + "/areas/";
        Log.i("Url: ",url);
        return url;
    }

    public static String buildHouseUrl(Household area) {
        String url;
        url = "https://intense-inferno-7741.firebaseio.com/organizations/" + org  + "/resources/";
        Log.i("Url: ",url);
        return url;
    }

    public static String buildNotesUrl() {
        String url = "https://intense-inferno-7741.firebaseio.com/organizations/" + org  + "/notes";
        return url;
    }
}
