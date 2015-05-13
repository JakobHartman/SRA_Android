package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.provider.ContactsContract;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Interview;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;
import org.rbdc.sra.objects.Nutrition;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

        // Get the snapshot for the user and delete the areas node so we can replace it
        // with an updated version
        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/users/");
        Query user = users.orderByChild("email").equalTo(CRUDFlinger.getUserName());
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // Replace the areas node
                    data.child("areas").getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // For each area
        for(final Area area : region.getAreas()){
            // Get the url of the area nodes
            String url = UrlBuilder.buildAreaUrl(area);
            Firebase base = new Firebase(url);
            // Get the url of the specific area
            Firebase newBase = base.child(area.getName().toLowerCase());
            // Set the values
            newBase.child("region").setValue(capitalize(area.getRegion()));
            newBase.child("country").setValue(capitalize(area.getCountry()));
            newBase.child("name").setValue(capitalize(area.getName()));
            //Log.i("Interview Count: ", area.getInterviews().size() + "");
            // Set the interviews
            for (Interview interview : area.getInterviews()){
                try{
                    String json = JSONUtilities.stringify(interview);
                    HashMap obj = DownloadData.buildMap(json);
                    newBase.child("interviews").setValue(obj);
                }catch (JSONException e){
                    //
                }catch (IOException e) {

                }
            }

            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Firebase areas = new Firebase(data.getRef().toString() + "/areas/");
                        areas.push().setValue(area.getName());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
    }

    public void uploadHouses(String org){
        // Get the region
        Region region = CRUDFlinger.getRegion();
        // For each area
        for(Area area : region.getAreas()){
            // For each household
            for(final Household household : area.getResources()) {
                // Get the household (Resources) node
                final Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + org  + "/resources/");
                //Log.i("Household ID: ",household.getHouseholdID());
                // query for household Id
                Query query = base.orderByChild("householdID").startAt(household.getHouseholdID()).endAt(household.getHouseholdID());
                // If the data has changed
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       //Log.i("Datasnapshot Children: ",dataSnapshot.getChildrenCount() + "");
                        if(dataSnapshot.getChildrenCount() == 0){
                            try{
                                base.push().setValue(DownloadData.buildMap(JSONUtilities.stringify(household)));
                                Log.i("Updating household: ", household.getName());
                            }catch (JSONException e){
                                //
                            }catch (IOException e){
                                //
                            }
                        }else{
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                Firebase newBase = new Firebase(data.getRef().toString());
                                try{
                                    newBase.setValue(DownloadData.buildMap(JSONUtilities.stringify(household)));
                                    Log.i("Adding household: ", household.getName());
                                }catch (JSONException e){return;}catch (IOException e){
                                    //
                                }
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
        // Remove Areas
        for(Area area : DeleteRecord.getAreas()){
            for(Area area1 : CRUDFlinger.getAreas()){
                if(area1.getName().equals(area.getName())){
                    CRUDFlinger.getAreas().remove(area);
                }
            }

        }
        // Remove households
        for(Area area : CRUDFlinger.getAreas()){
            for(final Household household : area.getResources()){
                for (final Household household1 : DeleteRecord.getHouseholds()){
                    if(household.getHouseholdID().equals(household1.getHouseholdID())){
                        CRUDFlinger.getAreas().get(CRUDFlinger.getAreas().indexOf(area)).getResources().remove(household);
                    }
                    // Remove the household from firebase
                    Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/sra/resources/");
                    Query query = base.orderByChild("householdID").equalTo(household1.getHouseholdID());

                    // double check the right node was found
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot. getChildren()) {
                                System.out.println(data);
                                if (data.child("householdID").getValue().toString().equals(household1.getHouseholdID().toString())) {
                                    String ref = data.getRef().toString();
                                    Firebase house = new Firebase(ref);
                                    house.removeValue();
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
        // Remove members
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
                // Clear the delete record
                DeleteRecord.initData();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
