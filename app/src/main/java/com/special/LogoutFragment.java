package com.special;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.login;
import org.rbdc.sra.objects.loginObject;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

public class LogoutFragment extends Fragment {

    //Views & Widgets
    private View parentView;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);
        loginObject login = CRUDFlinger.load("User", loginObject.class);
        login.setLoggedIn(false);
        CRUDFlinger.save("User",login);
        Intent intent = new Intent(parentView.getContext(),login.class);
        startActivity(intent);
        return parentView;
    }

}
