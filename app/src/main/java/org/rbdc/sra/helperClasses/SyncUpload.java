package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.Firebase;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Region;

import java.lang.reflect.Field;

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
