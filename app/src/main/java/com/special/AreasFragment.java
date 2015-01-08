package com.special;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;
import com.special.utils.UITabs;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

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

    private static int areaId;
    private static int householdId;

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
                     householdId = position;
                     mAdapter = new TransitionListAdapter(getActivity(),listMembers(i,position));
                     listView.setAdapter(mAdapter);
                     button.setText("Add Member");
                     button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             addMember();
                         }
                     });
                }
             });
            areaId = i;
            button.setText("Add Household");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addHousehold();
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
        dialog.setContentView(R.layout.layout_area_dialog);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final Spinner regionText = (Spinner) dialog.findViewById(R.id.spinner);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<String>();
        regions.add("Select Region");
        regions.addAll(loginObject.getCountryLogin().getRegionNames());
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
                    newArea.setRegion(loginObject.getCountryLogin().getRegionNames().get(regionText.getSelectedItemPosition() - 1));
                    CRUDFlinger.addArea(CRUDFlinger.getRegionId(),newArea);
                    mAdapter = new TransitionListAdapter(getActivity(),listArea());
                    listView.setAdapter(mAdapter);
                    dialog.cancel();
                    CRUDFlinger.saveCountry();
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

    private void addHousehold(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_household_dialog);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final EditText headText = (EditText) dialog.findViewById(R.id.editText1);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<String>();
        regions.add("Select Region");
        regions.addAll(loginObject.getCountryLogin().getRegionNames());

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Households newHousehold = new Households();

                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Household Name", Toast.LENGTH_LONG).show();
                }else if(headText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Select A Valid Head Member Name", Toast.LENGTH_LONG).show();
                }else{
                    newHousehold.setHouseholdName(areaText.getText().toString());
                    newHousehold.addMember(headText.getText().toString());
                    CRUDFlinger.addHousehold(CRUDFlinger.getRegionId(),areaId,newHousehold);
                    mAdapter = new TransitionListAdapter(getActivity(),listHouseholds(areaId));
                    listView.setAdapter(mAdapter);
                    dialog.cancel();
                    CRUDFlinger.saveCountry();

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

    private void addMember(){
        final Member member = new Member();
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_member_dialog);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final Spinner relationship = (Spinner)dialog.findViewById(R.id.spinner1);
        final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
        final Spinner education = (Spinner)dialog.findViewById(R.id.spinner2);
        final UITabs gender = (UITabs)dialog.findViewById(R.id.toggle);
        final UITabs school = (UITabs)dialog.findViewById(R.id.toggle3);


        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GG");
                Toast toast = new Toast(getActivity());
                if (areaText.getText().toString().matches("")) {
                    toast.makeText(getActivity(),"Please Enter A Valid Name",Toast.LENGTH_LONG).show();
                } else if (relationship.getSelectedItemPosition() == 0) {
                    toast.makeText(getActivity(),"Please Select A Valid Relationship",Toast.LENGTH_LONG).show();
                } else if (education.getSelectedItemPosition() == 0) {
                    toast.makeText(getActivity(),"Please Select A Valid Education Level",Toast.LENGTH_LONG).show();
                } else {
                    member.setName(areaText.getText().toString());
                    member.setRelationship(relationship.getSelectedItem().toString());
                    member.setBirthday(getDateFromDatePicker(datePicker));
                    member.setEducationLevel(education.getSelectedItem().toString());
                    int genderSelected = gender.getCheckedRadioButtonId();
                    RadioButton gSelected = (RadioButton)gender.findViewById(genderSelected);
                    member.setGender(gSelected.getText().toString());
                    int schoolSelected = school.getCheckedRadioButtonId();
                    RadioButton sSelected = (RadioButton)school.findViewById(schoolSelected);
                    boolean isInSchool;
                    switch (sSelected.getText().toString()){
                        case "Yes":
                            isInSchool = true;
                            break;
                        case "No":
                            isInSchool = false;
                            break;
                        default:
                            isInSchool = false;
                            break;
                    }
                    member.setInSchool(isInSchool);
                    member.setAreaName(CRUDFlinger.getCountry().getRegions().get(CRUDFlinger.getRegionId()).getAreas().get(areaId).getAreaName());
                    member.setHouseholdName(CRUDFlinger.getCountry().getRegions().get(CRUDFlinger.getRegionId()).getAreas().get(areaId).getHouseholds().get(householdId).getHouseholdName());
                    dialog.cancel();

                    try{
                        String string = JSONUtilities.stringify(member);
                        System.out.println(string);
                    }catch (JSONException e){

                    }
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
        for(Areas area : CRUDFlinger.getCountry().getRegions().get(CRUDFlinger.getRegionId()).getAreas()){
            listData.add(new ListItem(R.drawable.ic_like,area.getAreaName(),area.getHouseholds().size() + " Households",null,null));
        }
        return listData;
    }


    private ArrayList<ListItem> listHouseholds(int pos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(Households households : CRUDFlinger.getCountry().getRegions().get(CRUDFlinger.getRegionId()).getAreas().get(pos).getHouseholds()){
            listData.add(new ListItem(R.drawable.ic_like,households.getHouseholdName(),households.getMembers().size() + " Members",null,null));
        }
        return listData;
    }
 
    private ArrayList<ListItem> listMembers(int areaPos,int householdPos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(String member : CRUDFlinger.getCountry().getRegions().get(CRUDFlinger.getRegionId()).getAreas().get(areaPos).getHouseholds().get(householdPos).getMembers()){
            listData.add(new ListItem(R.drawable.ic_like,member,"",null,null));
        }
        return listData;
    }

    public static String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        String time = day + "/" + month + "/" + year;



        return time;
    }

}
