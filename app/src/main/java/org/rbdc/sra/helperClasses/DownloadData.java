package org.rbdc.sra.helperClasses;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

/**
 * Created by imac on 1/14/15.
 */
public class DownloadData {

    private static DownloadData instance = null;
    private static String organization = null;
    private static Application application = null;
    private static int passes = 0;

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

    public static void setApplication(Application application) {
        DownloadData.application = application;
    }

    public static void download(LoginObject info) {
        Region region = new Region();
        for(RegionLogin reg : info.getCountryLogin().getRegions()){
            for(AreaLogin area : reg.getAreas()){
                Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/Organizations/" + organization + "/Countries/" + info.getCountryLogin().getName() + "/Regions/" + reg.getName() + "/Areas/" + area.getName());
                Log.i("base url: ","https://intense-inferno-7741.firebaseio.com/Organizations/" + organization + "/Countries/" + info.getCountryLogin().getName() + "/Regions/" + reg.getName() + "/Areas/" + area.getName() );
                base.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gson gson = new GsonBuilder().create();
                        Area area = gson.fromJson(dataSnapshot.getValue().toString(), Area.class);
                        CRUDFlinger.addArea(area);
                        CRUDFlinger.saveRegion();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    }
}
