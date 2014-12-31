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
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.Region;

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
        mAdapter = new TransitionListAdapter(getActivity(), listArea());
        listView.setActionLayout(R.id.hidden_view2);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa,final int i, long l) {

            mAdapter = new TransitionListAdapter(getActivity(),listHouseholds(i));
            listView.setAdapter(mAdapter);
             listView.setOnItemClickListener(new OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     mAdapter = new TransitionListAdapter(getActivity(),listMembers(i,position));
                     listView.setAdapter(mAdapter);
                 }
             });

            }
        });
    }

    private ArrayList<ListItem> listArea(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();
        for(Areas area : CRUDFlinger.getRegion().getAreas()){
            listData.add(new ListItem(R.drawable.ic_like,area.getAreaName(),area.getHouseholds().size() + " Households",null,null));
        }
        return listData;
    }


    private ArrayList<ListItem> listHouseholds(int pos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(Households households : CRUDFlinger.getRegion().getAreas().get(pos).getHouseholds()){
            listData.add(new ListItem(R.drawable.ic_like,households.getHouseholdName(),households.getMembers().size() + " Members",null,null));
        }
        return listData;
    }

    private ArrayList<ListItem> listMembers(int areaPos,int householdPos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(String member : CRUDFlinger.getRegion().getAreas().get(areaPos).getHouseholds().get(householdPos).getMembers()){
            listData.add(new ListItem(R.drawable.ic_like,member,"",null,null));
        }
        return listData;
    }

}
