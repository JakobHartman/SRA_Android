package com.special;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DownloadData;
import org.rbdc.sra.helperClasses.SyncUpload;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                        DownloadData.syncDownload(login);
                        try{
                            region = CRUDFlinger.merge(CRUDFlinger.getTempRegion(), CRUDFlinger.getRegion());
                            String json = JSONUtilities.stringify(region);
                            Gson gson = new GsonBuilder().create();
                            Region newRegion = gson.fromJson(json,Region.class);
                            CRUDFlinger.setRegion(newRegion);
                            syncUp.uploadRegion();
                        }catch (Exception e){}
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
