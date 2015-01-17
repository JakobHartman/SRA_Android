package org.rbdc.sra.helperClasses;

/**
 * Created by imac on 1/14/15.
 */
public class DownloadData {

    public static DownloadData instance = null;
    public static String organization = null;

    public static void setOrganization(String organization) {
        DownloadData.organization = organization;
    }

    protected DownloadData() {
        // Exists only to defeat instantiation.
    }

    public static DownloadData getInstance() {
        if(instance == null) {
            instance = new DownloadData();
        }
        return instance;
    }
}
