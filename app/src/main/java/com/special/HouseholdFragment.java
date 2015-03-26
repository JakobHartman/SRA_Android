package com.special;

import android.support.v4.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

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

import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.ImageData;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HouseholdFragment extends Fragment {

    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    public TransitionListAdapterHousehold mAdapter;
    private ResideMenu resideMenu;
    private Button button;
    Button btn, btnCancel;
    Dialog dialog;
    private Bundle args;
    private static int areaId;
    private static int householdId;
    public TextView title;
    private Button interviewButton;
    private Button noteButton;
    private List<Note> notes_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get the parentview which is the dashboard
        parentView = inflater.inflate(R.layout.fragment_households, container, false);


        notes_list = new ArrayList<>();

        title = (TextView) getActivity().findViewById(R.id.title);
        listView = (UISwipableList) parentView.findViewById(R.id.listView);
        button = (Button) parentView.findViewById(R.id.button3);
        interviewButton = (Button) parentView.findViewById(R.id.interview_button);
        noteButton = (Button) parentView.findViewById(R.id.new_note_button);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();

        // Questions button
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("areaID", areaId);
                intent.putExtra("householdID", householdId);
                intent.putExtra("interviewType", "area");
                startActivity(intent);
            }
        });

        // New Note Button
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity(),
                        android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                dialog.setTitle("New Note");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.new_note);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bar_separator_color));
                dialog.show();

                // Cancel Button
                Button note_cancel = (Button) dialog.findViewById(R.id.note_cancel);
                note_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // Save Button
                Button note_save = (Button) dialog.findViewById(R.id.note_save);
                note_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a new note
                        EditText noteTitle = (EditText)dialog.findViewById(R.id.noteTitle);
                        EditText noteContent = (EditText)dialog.findViewById(R.id.note_text);
                        Note newNote;

                            newNote = new Note("Household", noteTitle.getText().toString(), noteContent.getText().toString(), CRUDFlinger.getAreas().get(areaId).getName());
                            // Add note to the list
                            System.out.println("Area name for the Note = "+CRUDFlinger.getAreas().get(areaId).getName());
                            notes_list.add(newNote);
                            // Notify Note created
                            Toast toast = new Toast(getActivity());
                            toast.makeText(getActivity(),"Note Created: " + newNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                            CRUDFlinger.addNote(newNote);
                        // Close the view
                        dialog.cancel();
                    }
                });
            }
        });

        title.setText("Households");
        mAdapter = new TransitionListAdapterHousehold(getActivity(), listHouseholds(areaId));
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // lists members
                MemberFragment goToHouse = new MemberFragment();
                Bundle args = new Bundle();
                args.putInt("Area Index",areaId);
                args.putInt("House Index",position);
                goToHouse.setArguments(args);

                // Change the fragment
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToHouse)
                        .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null).commit();
            }
        });


        button.setText("Add Household");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHousehold();
            }
        });

        return parentView;
    }

    /************************* add Household *******************************/

    private void addHousehold(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_household_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<>();
        regions.add("Select Region");
        regions.addAll(loginObject.getSiteLogin().getRegionNames());

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Household newHousehold = new Household();
                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Household Name", Toast.LENGTH_SHORT).show();
                }else{

                    newHousehold.setName(areaText.getText().toString());
                    newHousehold.setHouseholdID(newHousehold.getName().substring(0,3).toUpperCase(),"");
                    newHousehold.setArea(CRUDFlinger.getAreas().get(areaId).getName());
                    newHousehold.setCountry(CRUDFlinger.getAreas().get(areaId).getCountry());
                    CRUDFlinger.addHousehold(areaId, newHousehold);
                    mAdapter = new TransitionListAdapterHousehold(getActivity(),listHouseholds(areaId));
                    listView.setAdapter(mAdapter);
                    dialog.cancel();
                    CRUDFlinger.saveRegion();
                    addMember(CRUDFlinger.getAreas().get(areaId).getResources().size() - 1);
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


    private void addMember(final int householdID){
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

                    CRUDFlinger.getAreas().get(areaId).getResources().get(householdID).addMember(member);
                    //mAdapter = new TransitionListAdapter(getActivity(),listMembers(areaId,householdID));
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

    /********************** list Households *********************************/

    private ArrayList<ListItem> listHouseholds(int pos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        for(Household households : CRUDFlinger.getAreas().get(pos).getResources()){
            if (households.getImageCollection().size() > 0) {
                int last = households.getImageCollection().size() - 1;
                ImageData image = households.getImageCollection().get(last);
                String imageData = image.getImageData();
                Bitmap actImage = BitmapFactory.decodeByteArray(Base64.decode(imageData, Base64.DEFAULT), 0, Base64.decode(imageData, Base64.DEFAULT).length);
                listData.add(new ListItem(R.drawable.ic_like,households.getName(),households.getMembers().size() + " Members","" + areaId,null,actImage));
            } else {
                listData.add(new ListItem(R.drawable.ic_like, households.getName(), households.getMembers().size() + " Members", "" + areaId, null, null));
            }
        }
        return listData;
    }

    public static String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        return day + "/" + month + "/" + year;
    }

    public String getAge(String bday){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String time = new String();
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
            //
        }
        return time;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("", "restarted");
        Bundle args = getArguments();
        if (args  != null && args.containsKey("Area Index")){
            areaId = args.getInt("Area Index");
        }

        mAdapter = new TransitionListAdapterHousehold(getActivity(),listHouseholds(areaId));
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
