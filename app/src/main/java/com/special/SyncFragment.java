package com.special;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.Asyncer;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DownloadData;
import org.rbdc.sra.helperClasses.SyncUpload;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.Region;

import java.util.ArrayList;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;


public class SyncFragment extends Fragment {

    //Layouts
    Button btn;
    Region region;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.relogin_dialog, container, false);
        final SyncUpload syncUp = new SyncUpload();
            final LoginObject login = syncUp.startUpload(getActivity());


        final EditText password = (EditText) v.findViewById(R.id.password);
        final TextView textView = (TextView) v.findViewById(R.id.username);
        textView.setText("Enter password for " + login.getUsername().split("@")[0]);

        btn = (Button) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/");
                base.authWithPassword(login.getUsername(),password.getText().toString(),new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        for(Area area : CRUDFlinger.getAreas()){
                            for(Household household : area.getResources()) {
                                int areaId = CRUDFlinger.getAreas().indexOf(area);
                                int householdId = CRUDFlinger.getAreas().get(areaId).getResources().indexOf(household);
                                Log.i("Food: ", areaId + "");
                                ArrayList<Question> questions = null;
                                try {
                                    questions = CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getQuestionSet("nutrition").getQuestions();
                                }
                                catch(NullPointerException e){
                                  //
                                }
                                if(questions != null) {
                                    for (Question question : questions) {
                                        for(Datapoint datapoint : question.getDataPoints()){
                                            for(String food : datapoint.getAnswers()){
                                                CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getNutrition().clear();
                                                Log.i("Food: ", food);
                                                new Asyncer().execute(food, areaId, householdId);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        DownloadData.downloadToSync(login,getActivity().getBaseContext());
                        // Download and merge question sets
                        DownloadData.downloadTempQuestions();
                        try {
                            CRUDFlinger.merge(CRUDFlinger.getTempQuestionSets(), CRUDFlinger.getQuestionSets());

                        }catch (Exception e) {
                            System.out.println("There was an exception merging questions");
                        }

                        // Download and merge notes
//                        DownloadData.downloadTempNotes();
//                        try {
//                            CRUDFlinger.merge(CRUDFlinger.getTempNotes(), CRUDFlinger.getNotes());
//                        }catch (Exception e) {
//                            System.out.println("There was an exception merging notes");
//                        }

                        try{
                            region = CRUDFlinger.merge(CRUDFlinger.getTempRegion(), CRUDFlinger.getRegion());
                            CRUDFlinger.setRegion(region);
                            syncUp.removeFromDeleteRecord();
                            try{
                                Log.i("Being Pushed",JSONUtilities.stringify(CRUDFlinger.getAreas().get(0)));
                                syncUp.uploadAreas();
                                syncUp.uploadHouses();
                                syncUp.uploadQuestions();
                                syncUp.uploadNotes();

                                CRUDFlinger.saveRegion();
                            }catch (JSONException e){return;}


                        }catch (Exception e){return;}
                        getFragmentManager().beginTransaction().replace(R.id.main_fragment,new DashboardFragment(), "dashboard")
                                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null).commit();
                        TextView view = (TextView)getActivity().findViewById(R.id.title);
                                view.setText("Dashboard");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
