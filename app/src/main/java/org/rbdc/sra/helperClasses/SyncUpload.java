package org.rbdc.sra.helperClasses;

import android.app.Activity;

import com.firebase.client.Firebase;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;

import quickconnectfamily.json.JSONUtilities;

/**
 * Created by chad on 1/12/15.
 */
public class SyncUpload {
    String databaseString;

/*
    public SyncUpload(Activity activity){
        this.activity = activity;

    }*/

    public LoginObject startUpload(Activity activity) {
        Firebase.setAndroidContext(activity.getApplication());
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        System.out.println("LoginObject for Upload "+ loginObject);
        return loginObject;
    }

    public void uploadRegion() throws Exception{
        Region region = CRUDFlinger.getRegion();
        for(Area area : region.getAreas()){
            String url = UrlBuilder.buildAreaUrl(area);
            Firebase base = new Firebase(url);
            base.setValue(DownloadData.buildMap(JSONUtilities.stringify(area)));
        }

    }

}
