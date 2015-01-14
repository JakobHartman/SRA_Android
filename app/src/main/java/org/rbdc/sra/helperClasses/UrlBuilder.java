package org.rbdc.sra.helperClasses;

import org.rbdc.sra.objects.Areas;

/**
 * Created by imac on 1/14/15.
 */
public class UrlBuilder {

    private static UrlBuilder instance = null;
    protected UrlBuilder() {
        // Exists only to defeat instantiation.
    }
    public static UrlBuilder getInstance() {
        if(instance == null) {
            instance = new UrlBuilder();
        }
        return instance;
    }

    public static String buildAreaUrl(String org,Areas area){
        String url;
        url = "https://intense-inferno-7741.firebaseio.com/Organizations" + org + "/Countries/" + area.getCountry() + "/Regions/" + area.getRegion() + "/Areas/" + area.getAreaName();
        return url;
    }
}
