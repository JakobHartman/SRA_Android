package org.rbdc.sra.helperClasses;

import android.app.Activity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.LoginObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chad on 1/12/15.
 */
public class SyncUpload {
    private Activity activity;
    private String databaseString;
    private ArrayList<Areas> areas;

    public SyncUpload(Activity activity) {

        // Set up Area to give Firebase a context
        this.activity = activity;
        areas = new ArrayList<Areas>();

        // Get the user's areas, store them in an array,
        // to go through each area for sync
        for (Areas myArea:CRUDFlinger.getAreas()) {
            areas.add(myArea);
        }

        testUpload();

    }

    public void testUpload() {
        Firebase.setAndroidContext(activity.getApplicationContext());
        Firebase testBase = new Firebase("https://luminous-fire-7752.firebaseio.com/");

    /************** Map Example ************/
        Map<String, String> alanisawesomeMap = new HashMap<String, String>();
        alanisawesomeMap.put("birthYear", "1912");
        alanisawesomeMap.put("fullName", "Alan Turing");
        Map<String, String> gracehopMap = new HashMap<String, String>();
        gracehopMap.put("birthYear", "1906");
        gracehopMap.put("fullName", "Grace Hopper");
        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
        users.put("alanisawesome", alanisawesomeMap);
        users.put("gracehop", gracehopMap);

       // Map<String, Object> jsonMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());
        GsonBuilder builder = new GsonBuilder();
        Gson obj = builder.create();
        String areaJson = obj.toJson(areas.get(0));
        testBase.child("Areas").setValue(areaJson);
        //Takes a json string, converts it into a specified object type
        Map<String, Object> retMap = new Gson().fromJson(areaJson, new TypeToken<HashMap<String, Object>>() {}.getType());
        testBase.child("Areas").setValue(retMap);


        /********* Gson Example
        GsonBuilder builder = new GsonBuilder();
        Gson obj = builder.create();
        testBase.child("Areas").setValue(obj.toJson(areas.get(0)));
        System.out.println(obj.toJson(areas.get(0)));

        obj = areas.get(0);

        for(int i = 0; i < areas.size(); i++) {
            testBase.child("Name").setValue(areas.get(i).getAreaName());
            System.out.println("writing to " + areas.get(i).getAreaName());
        } */




    }

    public void startUpload() {
        Firebase.setAndroidContext(activity.getApplicationContext());

        /*********************************
        int i = 0;
        for(FirebaseArea area : fareas) {
            String url = urls.get(i);
            url = url.replaceAll("%20", " ");
            base = new Firebase(url);
            base.child("Name").setValue(area.getName());
            if (area.Resources.size() > 0) {
                for (FirebaseHousehold household : area.Resources) {
                    Firebase houses = base.child("Resources").child(household.getName());
                    houses.child("Name").setValue(household.getName());
                    if (household.Interviews.size() > 0) {

                        for (FirebaseInterview interview : household.Interviews) {
                            Firebase inter = houses.child("Interviews").push();
                            inter.child("Date Created").setValue(interview.getDateCreated());
                            if (interview.QuestionSets.size() > 0) {
                                for (FirebaseQuestionSet questionSet : interview.QuestionSets) {
                                    Firebase qs = inter.child("Question Sets").push();
                                    qs.child("Name").setValue(questionSet.getName());
                                    if(questionSet.Questions.size() > 0) {
                                        for (FirebaseQuestion question : questionSet.Questions) {
                                            Firebase q = qs.child("Questions").push();
                                            q.child("Name").setValue(question.getName());
                                            if(question.Datapoints.size() > 0) {
                                                for (FirebaseDatapoint datapoint : question.Datapoints) {
                                                    Firebase data = q.child("Data Points").push();
                                                    data.child("Type").setValue(datapoint.getType());
                                                    data.child("Label").setValue(datapoint.getLabel());
                                                    for (String answer : datapoint.Answers) {
                                                        Firebase answers = data.child("Answers").push();
                                                        answers.child("Value").setValue(answer);
                                                    }
                                                }
                                            }else{
                                                Firebase data = q.child("Data Points").push();
                                                data.child("Type").setValue("String");
                                                data.child("Label").setValue("Label");
                                                Firebase answers = data.child("Answers").push();
                                                answers.child("Value").setValue("Answer");
                                            }
                                        }
                                    }else{
                                        Firebase q = qs.child("Questions").push();
                                        q.child("Name").setValue("QuestionName");
                                        Firebase data = q.child("Data Points").push();
                                        data.child("Type").setValue("String");
                                        data.child("Label").setValue("Label");
                                        Firebase answers = data.child("Answers").push();
                                        answers.child("Value").setValue("Answer");
                                    }
                                }
                            }else{
                                Firebase qs = inter.child("Question Sets").push();
                                qs.child("Name").setValue("QuestionSetName");
                                Firebase q = qs.child("Questions").push();
                                q.child("Name").setValue("QuestionName");
                                Firebase data = q.child("Data Points").push();
                                data.child("Type").setValue("String");
                                data.child("Label").setValue("Label");
                                Firebase answers = data.child("Answers").push();
                                answers.child("Value").setValue("Answer");
                            }
                        }
                        if(household.Members.size() > 0) {
                            for (FirebaseMember member : household.Members) {
                                houses.child("Members").child(member.getName()).child("Name").setValue(member.getName());
                            }
                        }else{
                            houses.child("Members").child("Member").child("Name").setValue("Name");
                        }
                    }else{
                        Firebase inter = houses.child("Interviews").push();
                        inter.child("Date Created").setValue("9/99/99");
                        Firebase qs = inter.child("Question Sets").push();
                        qs.child("Name").setValue("QuestionSetName");
                        Firebase q = qs.child("Questions").push();
                        q.child("Name").setValue("QuestionName");
                        Firebase data = q.child("Data Points").push();
                        data.child("Type").setValue("String");
                        data.child("Label").setValue("Label");
                        Firebase answers = data.child("Answers").push();
                        answers.child("Value").setValue("Answer");
                    }
                }
                i++;
            }else{
                Firebase houses = base.child("Resources").child("Household");
                houses.child("Name").setValue("Household");
                Firebase inter = houses.child("Interviews").push();
                inter.child("Date Created").setValue("9/99/99");
                Firebase qs = inter.child("Question Sets").push();
                qs.child("Name").setValue("QuestionSetName");
                Firebase q = qs.child("Questions").push();
                q.child("Name").setValue("QuestionName");
                Firebase data = q.child("Data Points").push();
                data.child("Type").setValue("String");
                data.child("Label").setValue("Label");
                Firebase answers = data.child("Answers").push();
                answers.child("Value").setValue("Answer");
            }
        }



    ****/


}

    public void startDownload(String url) {
        Firebase database = new Firebase(url);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot resChildren: dataSnapshot.getChildren() ) {
                    System.out.println("Resource children " + resChildren.getName());
                    // Gets Resources children
                    if (resChildren.hasChildren()) {
                        for (DataSnapshot theChildren: resChildren.getChildren()) {
                            // Gets Resources grandchildren
                            if (theChildren.hasChildren()) {
                                for (DataSnapshot resGrandkids: theChildren.getChildren()) {
                                    System.out.println("Resources Grandkids "+resGrandkids.getName());
                                }
                            }
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
