package com.special;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.special.menu.ResideMenu;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DeleteRecord;
import org.rbdc.sra.helperClasses.DownloadData;
import org.rbdc.sra.helperClasses.SyncUpload;
import org.rbdc.sra.helperClasses.UrlBuilder;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;

import java.util.ArrayList;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

/**
 * Created by imac on 1/14/15.
 */
public class SyncFragment extends Fragment {

    //Layouts
    private ResideMenu resideMenu;
    Button btn, btnCancel;
    Region region;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v =  (LinearLayout) inflater.inflate(R.layout.relogin_dialog, container, false);
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
                        DownloadData.downloadToSync(login,getActivity().getBaseContext());
                        try{
                            region = CRUDFlinger.merge(CRUDFlinger.getTempRegion(), CRUDFlinger.getRegion());
                            CRUDFlinger.setRegion(region);
                            syncUp.removeFromDeleteRecord();
                            try{
                                Log.i("Being Pushed",JSONUtilities.stringify(CRUDFlinger.getAreas().get(0)));
                                syncUp.uploadRegion();
                                syncUp.uploadQuestions();
                                CRUDFlinger.saveRegion();
                            }catch (JSONException e){}


                        }catch (Exception e){}
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
