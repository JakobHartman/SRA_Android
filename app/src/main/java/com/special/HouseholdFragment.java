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

        // Notes Button
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrillDownNotes viewNotes = DrillDownNotes.newInstance(CRUDFlinger.getAreas().get(areaId).getName(), "area");
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,viewNotes,"drilldown notes fragment")
                        .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null).commit();

//                dialog = new Dialog(getActivity(),
//                        android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//                dialog.setTitle("New Note");
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.new_note);
//                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bar_separator_color));
//                dialog.show();
//
//                // Cancel Button
//                Button note_cancel = (Button) dialog.findViewById(R.id.note_cancel);
//                note_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.cancel();
//                    }
//                });
//
//                // Save Button
//                Button note_save = (Button) dialog.findViewById(R.id.note_save);
//                note_save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Create a new note
//                        EditText noteTitle = (EditText) dialog.findViewById(R.id.noteTitle);
//                        EditText noteContent = (EditText) dialog.findViewById(R.id.note_text);
//                        Note newNote;
//
//                        newNote = new Note("Household", noteTitle.getText().toString(), noteContent.getText().toString(), CRUDFlinger.getAreas().get(areaId).getName());
//                        // Add note to the list
//                        notes_list.add(newNote);
//                        // Notify Note created
//                        Toast toast = new Toast(getActivity());
//                        toast.makeText(getActivity(), "Note Created: " + newNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
//                        CRUDFlinger.addNote(newNote);
//                        // Close the view
//                        dialog.cancel();
//                    }
//                });
            }
        });

        title.setText("Households");
        mAdapter = new TransitionListAdapterHousehold(getActivity(), listHouseholds(areaId));
        listView.setActionLayout(R.id.hidden);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // lists members
                MemberFragment goToHouse = new MemberFragment();
                Bundle args = new Bundle();
                args.putInt("Area Index", areaId);
                args.putInt("House Index", position);

                // Save the index of the household we are in
                CRUDFlinger.save("house", position);
                goToHouse.setArguments(args);

                // Change the fragment
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, goToHouse, "member fragment")
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

        final String region = CRUDFlinger.getAreas().get(areaId).getRegion();

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
                    newHousehold.setHouseholdID(newHousehold.getName().substring(0, 3).toUpperCase(), "");
                    newHousehold.setArea(CRUDFlinger.getAreas().get(areaId).getName());
                    newHousehold.setCountry(CRUDFlinger.getAreas().get(areaId).getCountry());
                    newHousehold.setRegion(region);
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
        AddMember goToAddMember = new AddMember();
        Bundle args = new Bundle();
        args.putInt("Area Index",areaId);
        args.putInt("Household Id", householdID);
        goToAddMember.setArguments(args);

        // Change the fragment
        getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToAddMember, "AddMember")
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();

    }

    /********************** list Households *********************************/

    private ArrayList<ListItem> listHouseholds(int pos){
        ArrayList<ListItem>listData = new ArrayList<ListItem>();
        Household sample = null;
        for(Household households : CRUDFlinger.getAreas().get(pos).getResources()){
            sample = households;
            if (households.getImageCollection().size() > 0) {
                int last = households.getImageCollection().size() - 1;
                ImageData image = households.getImageCollection().get(last);
                String imageData = image.getImageData();
                Bitmap actImage = BitmapFactory.decodeByteArray(Base64.decode(imageData, Base64.DEFAULT), 0, Base64.decode(imageData, Base64.DEFAULT).length);
                listData.add(new ListItem(R.drawable.ic_home,households.getName(),households.getMembers().size() + " Members","" + areaId,null,actImage));
            } else {
                listData.add(new ListItem(R.drawable.ic_home, households.getName(), households.getMembers().size() + " Members", "" + areaId, null, null));
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
