package org.rbdc.sra.helperClasses;

import android.app.Activity;

import com.firebase.client.Firebase;

import org.rbdc.sra.objects.LoginObject;

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

}
