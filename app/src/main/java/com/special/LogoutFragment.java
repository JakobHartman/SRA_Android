package com.special;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rbdc.sra.Login;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.LoginObject;

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
        CRUDFlinger.save("User",login);
        Intent intent = new Intent(parentView.getContext(),Login.class);
        startActivity(intent);
        return parentView;
    }

}
