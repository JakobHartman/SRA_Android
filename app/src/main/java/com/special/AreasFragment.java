package com.special;


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
import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

public class AreasFragment extends Fragment {

    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ResideMenu resideMenu;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        initView();
        return parentView;
    }

    private void initView(){
        mAdapter = new TransitionListAdapter(getActivity(), getListData());
        listView.setActionLayout(R.id.hidden_view2);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {



            }
        });
    }

    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();
        listData.add(new ListItem(R.drawable.ph_1, "Henry Smith", "Vacation!", null, null));
        listData.add(new ListItem(R.drawable.ph_2, "Martinez", "Still exited from my trip last week!", null, null));
        listData.add(new ListItem(R.drawable.ph_3, "Olivier Smith", "Visiting Canada next week!", null, null));
        listData.add(new ListItem(R.drawable.ph_4, "Aria Thompson", "Can not go shopping tomorrow :(", null, null));
        listData.add(new ListItem(R.drawable.ph_5, "Sophie Hill", "Live every day like it is the last one!", null, null));
        listData.add(new ListItem(R.drawable.ph_6, "Addison Adams", "Not available, working...", null, null));
        return listData;
    }
}
