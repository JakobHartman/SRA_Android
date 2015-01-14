package org.rbdc.sra;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Country;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.Interviews;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

public class login extends Activity {

    EditText usernameET;
    EditText passwordET;
    private boolean status;
    TextView textview;
    ProgressBar progress;
    int passes = 0;
    private String organization = "SRA";

    public boolean getStatus(){
        return status;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set the activity for the firebase
        Firebase.setAndroidContext(this);

        //Retrieve the UI Elements from xml
        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        //set onclick for button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void login() {
        //Store the text from edittexts
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();

        //Unhide the progress wheel and text
            progress = (ProgressBar)findViewById(R.id.progressBar);
            progress.setVisibility(View.VISIBLE);
            textview = (TextView)findViewById(R.id.textView3);
            textview.setVisibility(View.VISIBLE);
            textview.setText("Logging In....");

        //Start a reference to the base firebase
        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com/Organization");
                //authenticate user using firebase.
                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    //Success
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        status = true;
                        textview.setText("Authenticating....");
                        //Get reference to User Tree
                        final String Node = username.split("@")[0];
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/Users/" + Node);
                        //Start download from firebase, once
                         users.addListenerForSingleValueEvent(new ValueEventListener() {

                             //Success
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 //loop through Users
                                 System.out.println("https://intense-inferno-7741.firebaseio.com/Users/" + Node);
                                        initialDownload(dataSnapshot);
                             }
                             //Fail
                             @Override
                             public void onCancelled(FirebaseError firebaseError) {
                                textview.setText(firebaseError.getMessage());
                             }
                         });
                    }

                    //Fail
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Hide progress wheel
                        progress.setVisibility(View.INVISIBLE);
                        //display Error
                        textview.setText(firebaseError.getMessage());
                        status = false;
                    }
                });
            }


        public void initialDownload(final DataSnapshot data){
            //Show status
            textview.setText("Saving User For Offline Use");

            // storing user data
            System.out.println(data.getValue().toString());
            String username = data.child("Email").getValue().toString();
            DataSnapshot ld = data.child("Organizations").child(organization);
            DataSnapshot role = ld.child("Roles");
            DataSnapshot country = ld.child("Countries");

            //create loginInfo Object
            final LoginObject info = new LoginObject();
            info.setLoggedIn(true);



            for(DataSnapshot rs : country.getChildren()){
                CountryLogin countryLogin = new CountryLogin();
                countryLogin.setName(rs.getName());
                for(DataSnapshot as : rs.child("Regions").getChildren()){
                    RegionLogin regionLogin = new RegionLogin();
                    regionLogin.setName(as.getName());
                    for(DataSnapshot a : as.child("Areas").getChildren()){
                        AreaLogin areaLogin = new AreaLogin();
                        areaLogin.setName(a.getName());
                        regionLogin.addArea(areaLogin);
                    }
                    countryLogin.addRegion(regionLogin);

                }
                info.setCountryLogin(countryLogin);
            }

            textview.setText("Loading User Areas");

            //add roles to loginInfo
            for(DataSnapshot roles:role.getChildren()){
                String Roles = roles.getValue().toString();
                info.addToRoles(Roles);
            }
            String userString;
            SharedPreferences.Editor user = getSharedPreferences("AppPrefs",MODE_PRIVATE).edit();
            try {
               userString = JSONUtilities.stringify(info);
               user.putString("User", userString);
               user.commit();
            }catch (JSONException e){}


            //set status to download
            textview.setText("Downloading Region");
            final Country country1 = new Country();
            country1.setCountryName(info.getCountryLogin().getName());
            int i = 0;
            for(final RegionLogin rg : info.getCountryLogin().getRegions()) {
                textview.setText("Downloading " + rg.getName());
                final Region region = new Region();
                region.setRegionName(rg.getName());
                for (AreaLogin ar : info.getCountryLogin().getRegions().get(i).getAreas()) {
                    textview.setText("Downloading area " + ar.getName());
                    //create reference to organization name
                    String urlref = "https://intense-inferno-7741.firebaseio.com/Organizations/" + organization + "/Countries/" + info.getCountryLogin().getName() + "/Regions/" + rg.getName() + "/Areas/" + ar.getName();
                    Firebase areaData = new Firebase(urlref);

                    areaData.addListenerForSingleValueEvent(new ValueEventListener() {
                        //Success

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Areas area = new Areas();
                            area.setAreaName(dataSnapshot.child("Name").getValue().toString());
                            area.setRegion(rg.getName());
                            area.setCountry(dataSnapshot.child("Country").getValue().toString());
                            area.setRegion(dataSnapshot.child("Region").getValue().toString());
                            DataSnapshot resources = dataSnapshot.child("Resources");
                            for(DataSnapshot household : resources.getChildren()){
                                Households households = new Households();
                                households.setHouseholdName(household.child("Name").getValue().toString());
                                for(DataSnapshot members : household.child("Members").getChildren() ){
                                    Member member = new Member();
                                    member.setHouseholdName(household.child("Name").getValue().toString());
                                    member.setAreaName(area.getAreaName());
                                    member.setName(members.child("Name").getValue().toString());
                                    member.setInSchool(Boolean.valueOf(members.child("Inschool").getValue().toString().toLowerCase()));
                                    member.setGender(members.child("Gender").getValue().toString());
                                    member.setEducationLevel(members.child("EducationLevel").getValue().toString());
                                    member.setRelationship(members.child("Relationship").getValue().toString());
                                    member.setBirthday(members.child("Birthday").getValue().toString());
                                    households.addMember(member);
                                }
                                for(DataSnapshot interviews : household.child("Interviews").getChildren()){
                                    Interviews interview = new Interviews();
                                    interview.setCreatedDate(interviews.child("Date Created").getValue().toString());
                                    for(DataSnapshot qs : interviews.child("Question Sets").getChildren()){
                                        QuestionSet questionSet = new QuestionSet(qs.child("Name").getValue().toString(),qs.getRef().toString());
                                        for(DataSnapshot q: qs.child("Questions").getChildren()){
                                            Question questions = new Question(q.getRef().toString());
                                            questions.setName(q.child("Name").getValue().toString());
                                            questions.setMultiUse(true);
                                            for (DataSnapshot datapoints : q.child("Data Points").getChildren()) {
                                                Datapoint newDatapoint = new Datapoint();
                                                for(DataSnapshot answer : datapoints.child("Answers").getChildren()){
                                                    newDatapoint.addAnswer(answer.getValue().toString());
                                                }
                                                newDatapoint.setDataType(datapoints.child("Type").getValue().toString());
                                                newDatapoint.setLabel(datapoints.child("Label").getValue().toString());
                                                questions.addDataPoint(newDatapoint);
                                            }
                                            questionSet.addQuestion(questions);
                                        }
                                        interview.addQuestionSets(questionSet);
                                    }
                                    households.addInterview(interview);
                                }
                                area.addHousehold(households);
                            }
                            region.addArea(area);
                            passes++;
                            if(passes == info.getCountryLogin().getRegionNames().size()){
                                SharedPreferences.Editor saveRegion = getSharedPreferences("AppPrefs",MODE_PRIVATE).edit();

                                try{
                                    String regionString = JSONUtilities.stringify(country1);

                                    saveRegion.putString("Country",regionString);
                                    saveRegion.commit();
                                }catch (JSONException e){}

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            textview.setText(firebaseError.getMessage());
                        }
                    });
                    i++;
                }
                country1.addRegion(region);
            }
        }
    }

