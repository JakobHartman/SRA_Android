package org.rbdc.sra.helperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shaded.fasterxml.jackson.core.JsonFactory;
import com.shaded.fasterxml.jackson.core.type.TypeReference;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

/**
 * Created by imac on 1/14/15.
 */
public class DownloadData {

    private static DownloadData instance = null;
    private static String organization = null;
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

    public static void download(final LoginObject info, final Context activity, final Class destination) {
        Region region = new Region();
        final int number = info.getSiteLogin().getAreaCount();
        for(CountryLogin country : info.getSiteLogin().getCountries()) {
            for (RegionLogin reg : country.getRegions()) {
                for (AreaLogin area : reg.getAreas()) {
                    Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    Log.i("base url: ", "https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    base.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i(" Area: ", dataSnapshot.getName());
                            Area area = dataSnapshot.getValue(Area.class);
                            CRUDFlinger.addArea(area);
                            CRUDFlinger.saveRegion();
                            try{
                                Log.i("Area :", JSONUtilities.stringify(area));
                            }catch (JSONException e){

                            }
                            Log.i(" Count: ", CRUDFlinger.getAreas().size() + " " + number + " " + passes);
                            passes++;

                            if (passes == number  && number == CRUDFlinger.getAreas().size() ) {
                                Intent intent = new Intent(activity, destination);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                                passes = 0;
                            }

                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }
    }

    public static void download(final LoginObject info) {
        final int number = info.getSiteLogin().getAreaCount();
        for(CountryLogin country : info.getSiteLogin().getCountries()) {
            for (RegionLogin reg : country.getRegions()) {
                for (AreaLogin area : reg.getAreas()) {
                    Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    Log.i("base url: ", "https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    base.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i(" Area: ", dataSnapshot.getName());
                            Area area = dataSnapshot.getValue(Area.class);
                            CRUDFlinger.addArea(area);
                            CRUDFlinger.saveRegion();
                            try{
                                Log.i("Area :", JSONUtilities.stringify(area));
                            }catch (JSONException e){

                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }
    }

    //For getting the current areas on the firebase
    public static void syncDownload(LoginObject info) {
        for(CountryLogin country : info.getSiteLogin().getCountries()) {
            for (RegionLogin reg : country.getRegions()) {
                for (AreaLogin area : reg.getAreas()) {
                    Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    Log.i("base url: ", "https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + country.getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                    base.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Area area = dataSnapshot.getValue(Area.class);
                            CRUDFlinger.getTempRegion().addArea(area);
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }
    }

    public static HashMap buildMap(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String,Object> o = mapper.readValue(json, typeRef);

        return o;
    }
}
