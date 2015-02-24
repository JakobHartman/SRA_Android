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

import org.rbdc.sra.helperClasses.DownloadData;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.RegionLogin;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

public class Login extends Activity {

    EditText usernameET;
    EditText passwordET;
    private boolean status;
    TextView textview;
    ProgressBar progress;
    private String organization = "sra";

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
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        if(id == R.id.action_settings)  {
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
        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com");
                //authenticate user using firebase.
                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    //Success
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        status = true;
                        textview.setText("Authenticating....");
                        //Get reference to User Tree
                        final String Node = username.split("@")[0];
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/users/" + Node.toLowerCase());
                        //Start download from firebase, once
                         users.addListenerForSingleValueEvent(new ValueEventListener() {

                             //Success
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 //loop through Users
                                 textview.setText("Saving User For Offline Use");
                                 System.out.println(dataSnapshot.getValue().toString());

                                 String username = dataSnapshot.child("email").getValue().toString();

                                 DataSnapshot ld = dataSnapshot.child("organizations").child(organization);
                                 DataSnapshot role = ld.child("roles");
                                 DataSnapshot country = ld.child("countries");
                                 final LoginObject info = new LoginObject();
                                 info.setUsername(username);
                                 info.setLoggedIn(true);
                                 for(DataSnapshot rs : country.getChildren()){
                                     CountryLogin countryLogin = new CountryLogin();
                                     countryLogin.setName(rs.getKey());
                                     for(DataSnapshot as : rs.child("regions").getChildren()){
                                         RegionLogin regionLogin = new RegionLogin();
                                         regionLogin.setName(as.getKey());
                                         for(DataSnapshot a : as.child("areas").getChildren()){
                                             AreaLogin areaLogin = new AreaLogin();
                                             areaLogin.setName(a.getKey());
                                             regionLogin.addArea(areaLogin);
                                         }
                                         countryLogin.addRegion(regionLogin);

                                     }
                                     info.getSiteLogin().addCountry(countryLogin);
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
                                     Firebase.setAndroidContext(getBaseContext());
                                     DownloadData.downloadQuestions();
                                     DownloadData.downloadGoToDash(info,getBaseContext());


                                 }catch (JSONException e){
                                     //
                                 }


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
    }

