package org.rbdc.sra.helperClasses;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shaded.fasterxml.jackson.core.JsonFactory;
import com.shaded.fasterxml.jackson.core.type.TypeReference;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

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
                Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + info.getCountryLogin().getName() + "/regions/" + reg.getName() + "/areas/" + area.getName());
                Log.i("base url: ","https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/countries/" + info.getCountryLogin().getName() + "/regions/" + reg.getName() + "/areas/" + area.getName() );
                base.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("JSON: ", dataSnapshot.getValue().toString());
//                            Gson gson = new GsonBuilder().create();
                            Area area = dataSnapshot.getValue(Area.class);
                            Log.i("String: ", "" + area.getName());
                            CRUDFlinger.addArea(area);
                            CRUDFlinger.saveRegion();



                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
//                Firebase newBase = new Firebase("https://testrbdc.firebaseio.com");
//                        try{
//                            String json = JSONUtilities.stringify(CRUDFlinger.buildObject());
//                            Log.i(" First: ",json);
//                            Map<String, Object> son = (Map)testJackson(json);
//                            newBase.setValue(son);
//                            Gson gson = new GsonBuilder().create();
//                            Area areas = gson.fromJson(json, Area.class);
//                            Log.i(" Second: ",JSONUtilities.stringify(areas));
//                        }catch (JSONException e){
//
//                        }catch (IOException e){}

            }
        }
    }

    public static HashMap testJackson(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String,Object> o = mapper.readValue(json, typeRef);
        return o;
    }
}
