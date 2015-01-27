package com.special;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;
import com.special.utils.UITabs;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AreasFragment extends Fragment {

    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ResideMenu resideMenu;
    private Button button;
    Button btn, btnCancel;
    Dialog dialog;
    public static String navigation;
    private Bundle args;
    private static int areaId;
    private static int householdId;
    public TextView title;
    private Button interviewButton;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);

        title = (TextView) getActivity().findViewById(R.id.title);
        listView = (UISwipableList) parentView.findViewById(R.id.listView);
        button = (Button) parentView.findViewById(R.id.button3);
        interviewButton = (Button) parentView.findViewById(R.id.interview_button);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        navigation = "area";


        //System.out.println("AreaFrag tag = "+this.getTag());

        // If the fragment is reached via menu, there will be no args
        try {
            args = getArguments();
            if (!args.isEmpty()) {
                navigation = "members";
            }
        } catch (Exception e) {}
        initView();

        return parentView;
    }

    public void initView(){
        if (navigation == "members") {

            householdId = args.getInt("Household Id");
            areaId = args.getInt("Area Index");
            memberView(areaId,householdId);

        } else if (navigation == "household") {
            householdView(areaId);
        }

        else if (navigation == "area") {
            // Lists Areas
            mAdapter = new TransitionListAdapter(getActivity(), listArea());
            listView.setActionLayout(R.id.hidden);
            listView.setItemLayout(R.id.front_layout);
            listView.setAdapter(mAdapter);
            listView.setIgnoredViewHandler(resideMenu);
            interviewButton.setVisibility(View.INVISIBLE);

            button.setText("Add Area");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addArea();
                }
            });

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View viewa, final int i, long l) {

                    // Lists the households
                    areaId = i;
                    householdView(areaId);
                }
            });


        }

    }

    private void householdView(int pos) {
        navigation = "household";
        areaId = pos;
        title.setText("Households");
        mAdapter = new TransitionListAdapter(getActivity(), listHouseholds(areaId));
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // lists members
                householdId = position;
                memberView(areaId, householdId);

            }
        });
        button.setText("Add Household");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHousehold();
            }
        });

        // Questions button
        interviewButton.setVisibility(View.VISIBLE);
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("areaID", areaId);
                intent.putExtra("householdID", householdId);
                intent.putExtra("interviewType", navigation);
                startActivity(intent);
            }
        });

    }

    private void memberView(int areaPos, int housePos) {
        areaId = areaPos;
        householdId = housePos;
        navigation = "members";
        title.setText(listHouseholds(areaId).get(householdId).getTitle());
        mAdapter = new TransitionListAdapter(getActivity(), listMembers(areaId, householdId));
        listView.setAdapter(mAdapter);
        button.setText("Add Member");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

        // Questions button
        interviewButton.setVisibility(View.VISIBLE);
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("areaID", areaId);
                intent.putExtra("householdID", householdId);
                intent.putExtra("interviewType", navigation);
                startActivity(intent);
            }
        });

    }



    private void addArea(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_area_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final Spinner regionText = (Spinner) dialog.findViewById(R.id.spinner);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<String>();
        regions.add("Select Region");
        regions.addAll(loginObject.getSiteLogin().getRegionNames());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,regions);
        regionText.setAdapter(adapter);

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Area newArea = new Area();

                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Area Name", Toast.LENGTH_LONG).show();
                }else if(regionText.getSelectedItemPosition() == 0){
                    toast.makeText(getActivity(),"Please Select A Valid Region", Toast.LENGTH_LONG).show();
                }else{
                    newArea.setRegion(regionText.getSelectedItem().toString());
                    newArea.setCountry(CRUDFlinger.getCountryName(regionText.getSelectedItem().toString()));
                    newArea.setName(areaText.getText().toString());
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

    private void addHousehold(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_household_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<String>();
        regions.add("Select Region");
        regions.addAll(loginObject.getSiteLogin().getRegionNames());

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Household newHousehold = new Household();
                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Household Name", Toast.LENGTH_LONG).show();
                }else{
                    newHousehold.setName(areaText.getText().toString());
                    CRUDFlinger.addHousehold(areaId,newHousehold);
                    mAdapter = new TransitionListAdapter(getActivity(),listHouseholds(areaId));
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

    private void addMember(){
        final Member member = new Member();
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_member_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
                Toast toast = new Toast(getActivity());
                if (areaText.getText().toString().matches("")) {
                    toast.makeText(getActivity(),"Please Enter A Valid Name",Toast.LENGTH_SHORT).show();
                } else if (relationship.getSelectedItemPosition() == 0) {
                    toast.makeText(getActivity(),"Please Select A Valid Relationship",Toast.LENGTH_SHORT).show();
                } else if (education.getSelectedItemPosition() == 0) {
                    toast.makeText(getActivity(),"Please Select A Valid Education Level",Toast.LENGTH_SHORT).show();
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
                    member.setInschool(isInSchool);
                    dialog.cancel();

                    CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).addMember(member);
                    mAdapter = new TransitionListAdapter(getActivity(),listMembers(areaId,householdId));
                    listView.setAdapter(mAdapter);
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
        for(Area area : CRUDFlinger.getAreas()){
            listData.add(new ListItem(R.drawable.ic_like,area.getName(),area.getResources().size() + " Households",null,null));
        }
        return listData;
    }

    private ArrayList<ListItem> listHouseholds(int pos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(Household households : CRUDFlinger.getAreas().get(pos).getResources()){
            listData.add(new ListItem(R.drawable.ic_like,households.getName(),households.getMembers().size() + " Members","" + areaId,null));
        }
        return listData;
    }

    private ArrayList<ListItem> listMembers(int areaPos,int householdPos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(Member member : CRUDFlinger.getAreas().get(areaPos).getResources().get(householdPos).getMembers()){
            listData.add(new ListItem(R.drawable.ic_like,member.getName(), "Age: " + getAge(member.getBirthday()) + " Relationship:  " + member.getRelationship(),null,null));
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

    public String getAge(String bday){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String time;
        time = new String();
        try {
            Date date = formatter.parse(bday);
            Date current = new Date();
            long diff = current.getTime() - date.getTime();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(date.getTime());
            int mYear = c.get(Calendar.YEAR);
            c.setTimeInMillis(current.getTime());
            int cYear = c.get(Calendar.YEAR);

            time = cYear - mYear + "";
        }catch (ParseException e){

        }
        return time;
    }


}


