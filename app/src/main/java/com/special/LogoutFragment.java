package com.special;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rbdc.sra.Login;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;

public class LogoutFragment extends Fragment {

    //Views & Widgets
    private View parentView;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);
        LoginObject login = CRUDFlinger.load("User", LoginObject.class);
        login.setLoggedIn(false);


        Log.e("log out # of Areas: ", CRUDFlinger.getAreas().size() + "");
        Log.e("log out # of Notes", CRUDFlinger.getNotes().size() + "");
        Intent intent = new Intent(parentView.getContext(),Login.class);
        startActivity(intent);
        return parentView;
    }

}
