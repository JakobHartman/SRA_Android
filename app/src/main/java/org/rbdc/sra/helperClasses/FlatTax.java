package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

/**
 * Created by imac on 2/5/15.
 */
public class FlatTax {
    private static FlatTax instance = null;
    private static Activity activity = null;

    public static void setActivity(Activity activity) {
        FlatTax.activity = activity;
    }

    protected FlatTax() {
        // Exists only to defeat instantiation.
    }

    public static FlatTax getInstance() {
        if(instance == null) {
            instance = new FlatTax();
        }
        return instance;
    }




}
