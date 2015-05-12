package org.rbdc.sra.helperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import com.shaded.fasterxml.jackson.core.JsonFactory;
import com.shaded.fasterxml.jackson.core.type.TypeReference;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Interview;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Note;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import quickconnectfamily.json.JSONUtilities;

public class DownloadData {

    private static DownloadData instance = null;
    private static String organization = null;
    private static int passes = 0;
    private static ArrayList<Area> areas = null;



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

    public static void downloadGoToDash(LoginObject info, final Context activity){



        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/resources/");

        //Find how many areas are assigned to the user account
        //final int number = info.getSiteLogin().getAreaCount();
        final int number = CRUDFlinger.getAreaCount();
        areas = new ArrayList<>();



        // For each of the countries assigned to the user
//        for (CountryLogin country : info.getSiteLogin().getCountries()){
//            for(RegionLogin regions : country.getRegions()){

                for(final String area : CRUDFlinger.getAreaNames()){

                    // For each area belonging to the user, look through the resources node
                    // for child nodes that have areas with the same name
                    //String id = capitalizeFirstLetter(area.getName());
                    Query query = base.orderByChild("area").equalTo(area);
                    //Log.i("link: ", query.getRef().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            passes = 1;
                            Area currentArea = new Area();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Household household = child.getValue(Household.class);
                                Log.i("counts: ","" + passes);

                                Log.i("household downloaded: ", household.getHouseholdID());

                                //Check to see if Area has already been created, if so add the
                                //household to that area
                                if(passes > 1) {
//                                    for (Area area1 : CRUDFlinger.getAreas()){
//                                        if (area1.getName().equals(household.getArea())) {
//                                            area1.addHousehold(household);
//                                        } else {
//                                            createArea(household);
//                                        }
//                                    }
                                    currentArea.addHousehold(household);
                                    System.out.println("Adding " + household.getName() + " to " + currentArea.getName());

                                }else{
                                    currentArea = createArea(household);
                                    System.out.println("Adding " + household.getName() + " to new area");
                                    CRUDFlinger.getRegion().addArea(currentArea);
                                }

                                passes++;

                            }

                            //Download the areas and their interviews
                            for (final String areaName : CRUDFlinger.getAreaNames()) {
                                Firebase areaBase = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/areas/");
                                Query areaQuery = areaBase.orderByChild("name").equalTo(areaName);
                                areaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList<Area> storedAreas = CRUDFlinger.getAreas();
                                        // Get all of the interviews the the particular area
                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                            ArrayList<Interview> interviews = new ArrayList<Interview>();
                                            //System.out.println(area.child("interviews").getValue());
                                            interviews.add(areaSnapshot.child("interviews").getValue(Interview.class));

                                            //Now match up areas with interviews
                                            for (Area area : storedAreas) {
                                                if (area.getName().equals(areaSnapshot.getKey())) {
                                                    area.setInterviews(interviews);
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }

                            CRUDFlinger.saveRegion();
                            //CRUDFlinger.getRegion().getAreas().addAll(areas);
                            //areas.clear();
                            if(CRUDFlinger.getAreas().size() == number){
                                Intent intent = new Intent(activity,Dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(activity,firebaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            //}
        //}
    }

    public static void downloadToSync(LoginObject info, final Context activity){
        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/resources/");
        areas = new ArrayList<>();
        Region region;
        SyncUpload syncUp = new SyncUpload();
        // Get the country, region, and areas assigned to the user
        // For each household that has the area name, get all of the children nodes
        for (CountryLogin country : info.getSiteLogin().getCountries()){
            for(RegionLogin regions : country.getRegions()){
                for(AreaLogin area : regions.getAreas()){
                    String id = capitalizeFirstLetter(area.getName());
                    Query query = base.orderByChild("area").startAt(id).endAt(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // For each household of the area
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Household household = child.getValue(Household.class);
                                // Check if there are tempAreas
                                if(CRUDFlinger.getTempAreas().size() > 0){
                                    for(Area area : CRUDFlinger.getTempRegion().getAreas()){
                                        // If the temp area exists add the household to that
                                        // area
                                        if(area.getName().equals(household.getArea())){
                                            area.addHousehold(household);
                                        }else{
                                            createTempArea(household);
                                        }
                                    }
                                }else{
                                    createTempArea(household);
                                }

                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(activity,firebaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }

        // Save all of the temp areas
        CRUDFlinger.getTempRegion().getAreas().addAll(areas);
        areas.clear();
        String org = "sra";
        // Begin uploading updated and removed data
        try{
            region = CRUDFlinger.merge(CRUDFlinger.getTempRegion(), CRUDFlinger.getRegion());
            CRUDFlinger.setRegion(region);
            syncUp.removeFromDeleteRecord();
            try{
                Log.i("Being Pushed", JSONUtilities.stringify(CRUDFlinger.getAreas().get(0)));
                syncUp.uploadAreas();
                System.out.println("uploading HOUSES");
                syncUp.uploadHouses(org);
                System.out.println("uploading questions");
                syncUp.uploadQuestions();
                System.out.println("uploading notes");
                syncUp.uploadNotes();

                CRUDFlinger.saveRegion();
            }catch (quickconnectfamily.json.JSONException e){
                e.printStackTrace();
                return;}


        }catch (Exception e){
            e.printStackTrace();
            return;}

        CRUDFlinger.saveRegion();
    }

    public static Area createArea(Household household) {
        Area area = new Area();
        area.setCountry(household.getCountry());
        area.setRegion(household.getRegion());
        area.setName(household.getArea());
        area.addHousehold(household);
        areas.add(area);
        return area;
    }

    public static void createTempArea(Household household){

        Area area = new Area();
        area.setCountry(household.getCountry());
        area.setRegion(household.getRegion());
        area.setName(household.getArea());
        area.addHousehold(household);
        areas.add(area);
    }

    public static String capitalizeFirstLetter(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static void downloadQuestions() {
        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/question sets");
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    QuestionSet question = data.getValue(QuestionSet.class);

                    //Check to see if it is an area question
                    String type = data.child("type").getValue().toString();
                    if (type.equals("AREA")) {
                        question.setType("Community");
                    }

                    CRUDFlinger.addQuestionSet(question);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void downloadTempQuestions() {
        Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/question sets");
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    System.out.println("downloading temp questions");
                    QuestionSet question = data.getValue(QuestionSet.class);
                    CRUDFlinger.addTempQuestionSet(question);

                    // add the question merge here
                    try {
                        //CRUDFlinger.merge(CRUDFlinger.getTempQuestionSets(), CRUDFlinger.getQuestionSets());
                        CRUDFlinger.mergeQuestions();
                    }catch (Exception e) {
                        System.out.println("There was an exception merging questions");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static void downloadTempNotes() {
        Firebase fbNotes = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/"+organization+"/notes");
        fbNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Note note = data.getValue(Note.class);
                    String username = CRUDFlinger.getUser().getEmail();
                    if (note.getAuthor().equals(username)) {
                        CRUDFlinger.addTempNote(note);
                        System.out.println("downloading temp notes");
                    }

                }

                try {
                    //ArrayList<Note> notes = CRUDFlinger.merge(CRUDFlinger.getTempNotes(), CRUDFlinger.getNotes());
                    CRUDFlinger.mergeNotes(CRUDFlinger.getTempNotes(), CRUDFlinger.getNotes());

                }catch (Exception e) {
                    System.out.println("There was an exception merging notes");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void downloadNotes(final String username) {
        try {

            Firebase fbNotes = new Firebase("https://intense-inferno-7741.firebaseio.com/organizations/" + organization + "/notes");
            fbNotes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Note note = data.getValue(Note.class);
                        if (note.getAuthor().equals(username)) {
                            System.out.println("Note downloaded: " + note.getNoteTitle());
                            CRUDFlinger.addNote(note);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
