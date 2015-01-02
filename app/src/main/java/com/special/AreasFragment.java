package com.special;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.loginObject;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

public class AreasFragment extends Fragment {

    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ResideMenu resideMenu;
    private Button button;
    Button btn, btnCancel;
    Dialog dialog;
    private static String navigation;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        button = (Button) parentView.findViewById(R.id.button3);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        navigation = "area";
        initView();
        return parentView;
    }

    private void initView(){
        mAdapter = new TransitionListAdapter(getActivity(), listArea());
        listView.setActionLayout(R.id.hidden_view2);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigation == "area"){
                    addArea();
                }else if(navigation == "household"){
                    
                }
            }
        });
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

    private void addArea(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final Spinner regionText = (Spinner) dialog.findViewById(R.id.spinner);
        final loginObject loginObject = CRUDFlinger.load("User",loginObject.class);
        ArrayList<String> regions = new ArrayList<String>();
        regions.add("Select Region");
        regions.addAll(loginObject.getRegions());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,regions);
        regionText.setAdapter(adapter);

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Areas newArea = new Areas();

                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Area Name", Toast.LENGTH_LONG).show();
                }else if(regionText.getSelectedItemPosition() == 0){
                    toast.makeText(getActivity(),"Please Select A Valid Region", Toast.LENGTH_LONG).show();
                }else{
                    newArea.setAreaName(areaText.getText().toString());
                    newArea.setRegion(loginObject.getRegions().get(regionText.getSelectedItemPosition() - 1));
                    CRUDFlinger.addArea(newArea);
                    mAdapter = new TransitionListAdapter(getActivity(),listArea());
                    listView.setAdapter(mAdapter);
                    dialog.cancel();
                    CRUDFlinger.saveRegion();
                }
            }
        });

        btnCancel = (Button) dialog.findViewById(R.id.btncancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        dialog.show();
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
