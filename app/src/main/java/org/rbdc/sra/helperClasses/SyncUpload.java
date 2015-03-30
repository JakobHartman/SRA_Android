package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;
import org.rbdc.sra.objects.Nutrition;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;

import java.io.IOException;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;


public class SyncUpload {

    public LoginObject startUpload(Activity activity) {
        Firebase.setAndroidContext(activity.getApplication());
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        System.out.println("LoginObject for Upload "+ loginObject);
        return loginObject;
    }

    public void uploadQuestions() {
        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/sra/question sets");
        //String url = UrlBuilder.buildHouseUrl("question sets");
        //Firebase base = new Firebase(url);
        // For each qs, create a new node with the ID as the name
        for(QuestionSet qs : CRUDFlinger.getQuestionSets()) {
            base.child(qs.getqSetId()).setValue(qs);
        }
        //base.setValue(CRUDFlinger.getQuestionSets());
    }

    public void uploadNotes() {
        //String url = UrlBuilder.buildHouseUrl("notes");
        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/sra/notes");
        for (Note note : CRUDFlinger.getNotes()) {
            base.child(note.getNoteID()).setValue(note);
        }
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

    public void uploadHouses(String org){
        Region region = CRUDFlinger.getRegion();
        for(Area area : region.getAreas()){
            for(final Household household : area.getResources()) {

                Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + org  + "/resources/");
                Log.i("Household ID: ",household.getHouseholdID());
                Query query = base.orderByChild("householdID").startAt(household.getHouseholdID()).endAt(household.getHouseholdID());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       Log.i("Datasnapshot Children: ",dataSnapshot.getChildrenCount() + "");
                       for(DataSnapshot data : dataSnapshot.getChildren()){
                           Firebase newBase = new Firebase(data.getRef().toString());
                           try{
                               newBase.setValue(DownloadData.buildMap(JSONUtilities.stringify(household)));
                           }catch (JSONException e){return;}catch (IOException e){
                               //
                           }
                       }
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
                if(area1.getName().equals(area.getName())){
                    CRUDFlinger.getAreas().remove(area);
                }
            }
        }
        for(Area area : CRUDFlinger.getAreas()){
            for(Household household : area.getResources()){
                for (Household household1 : DeleteRecord.getHouseholds()){
                    if(household.getHouseholdID().equals(household1.getHouseholdID())){
                        CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().remove(household);
                    }
                }
            }
        }
        for(Area area : CRUDFlinger.getAreas()){
            for(Household household : area.getResources()){
                for(Member member : household.getMembers()) {
                    for (Member member1 : DeleteRecord.getMembers()) {
                        if(member.getName().equals(member1.getName()) && member.getBirthday().equals(member1.getBirthday())){
                            CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().get(CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().indexOf(household)).getMembers().remove(member1);
                        }
                    }
                }
            }
        }

        Firebase fbnotes = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/sra/notes");
        fbnotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Note note : DeleteRecord.getNotes()) {
                    Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/sra/notes/" + note.getNoteID());
                    if (dataSnapshot.getChildrenCount() > 1) {
                        Log.i("Delete Record: ", "remove at " + base);
                        base.removeValue();
                        CRUDFlinger.getNotes().remove(note);
                    }
                }
                DeleteRecord.initData();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
