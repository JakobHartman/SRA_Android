package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Nutrition;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import quickconnectfamily.json.JSONException;
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

    public void uploadQuestions() {
        Firebase base = new Firebase("https://testrbdc.firebaseio.com/organizations/sra/question sets");
        base.setValue(CRUDFlinger.getQuestionSets());
    }

    public void uploadAreas(){
        Region region = CRUDFlinger.getRegion();
        for(Area area : region.getAreas()){
            String url = UrlBuilder.buildAreaUrl(area);
            Firebase base = new Firebase(url);
            Firebase newBase = base.child(area.getName().toLowerCase());
            newBase.child("region").setValue(capitalize(area.getRegion()));
            newBase.child("country").setValue(capitalize(area.getCountry()));
            newBase.child("name").setValue(capitalize(area.getName()));
        }
    }

    public void uploadHouses(){
        Region region = CRUDFlinger.getRegion();
        for(Area area : region.getAreas()){
            for(final Household household : area.getResources()) {
                for(Nutrition nutrition : household.getNutrition()){
                    DownloadData.nutritionixFetch(nutrition.getFoodName());
                }
                String url = UrlBuilder.buildHouseUrl(household);
                Firebase base = new Firebase(url);
                Query query = base.orderByChild("name").startAt(household.getName()).endAt(household.getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       Firebase string = dataSnapshot.getRef();
                        try{
                            string.setValue(DownloadData.buildMap(JSONUtilities.stringify(household)));
                        }catch (JSONException e){}catch (IOException e){}
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        }
    }

    private String capitalize(String line)
    {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public void removeFromDeleteRecord(){
        for(Area area : DeleteRecord.getAreas()){
            for(Area area1 : CRUDFlinger.getAreas()){
                if(area1.getName() == area.getName()){
                    CRUDFlinger.getAreas().remove(area);
                }
            }
        }
        for(Area area : CRUDFlinger.getAreas()){
            for(Household household : area.getResources()){
                for (Household household1 : DeleteRecord.getHouseholds()){
                    if(household.getName() == household1.getName()){
                        CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().remove(household);
                    }
                }
            }
        }
        for(Area area : CRUDFlinger.getAreas()){
            for(Household household : area.getResources()){
                for(Member member : household.getMembers()) {
                    for (Member member1 : DeleteRecord.getMembers()) {
                        if(member.getName() == member1.getName() && member.getBirthday() == member1.getBirthday()){
                            CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().get(CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().indexOf(household)).getMembers().remove(member1);
                        }
                    }
                }
            }
        }
        DeleteRecord.initData();
    }

}
