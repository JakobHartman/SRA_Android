package com.special;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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

import org.rbdc.sra.objects.ImageData;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.rbdc.sra.R;


public class MemberFragment extends Fragment {

    //Views & Widgets

    private UISwipableList listView;
    public TransitionListAdapterMember mAdapter;
    Button btn, btnCancel;
    Dialog dialog;
    private static int areaId;
    private static int householdId;
    public TextView title;
    private List<Note> notes_list;
    Bundle args;
    private ResideMenu resideMenu;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView;
        Button interviewButton;
        Button noteButton;
        Button button;
        parentView = inflater.inflate(R.layout.fragment_member, container, false);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();

        notes_list = new ArrayList<>();

        title = (TextView) getActivity().findViewById(R.id.title);
        listView = (UISwipableList) parentView.findViewById(R.id.listView);
        button = (Button) parentView.findViewById(R.id.button3);
        interviewButton = (Button) parentView.findViewById(R.id.interview_button);
        noteButton = (Button) parentView.findViewById(R.id.new_note_button);

        // Questions button
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("areaID", areaId);
                intent.putExtra("householdID", householdId);
                intent.putExtra("interviewType", "household");
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


                        newNote = new Note("Member", noteTitle.getText().toString(), noteContent.getText().toString(), CRUDFlinger.getAreas().get(areaId).getName(), CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getHouseholdID());
                        // Add note to the list
                        System.out.println("HouseholdID for the Note = " + CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getHouseholdID());
                        System.out.println("Actual householdID = " + CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getHouseholdID());
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

        title.setText(CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getName());
        mAdapter = new TransitionListAdapterMember(getActivity(), listMembers(areaId, householdId));
        listView.setActionLayout(R.id.hidden);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        mAdapter.notifyDataSetChanged();
        System.out.println("Inside the member view");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // Questions button
        interviewButton.setVisibility(View.VISIBLE);
        noteButton.setVisibility(View.VISIBLE);

        return parentView;
    }

    /************************ add Member ******************************/

    private void addMember(){

        AddMember goToAddMember = new AddMember();
        Bundle args = new Bundle();
        args.putInt("Area Index",areaId);
        args.putInt("Household Id", householdId);
        goToAddMember.setArguments(args);

        // Change the fragment
        getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToAddMember, "AddMember")
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();


    }

    /********************** list Members *********************************/


    private ArrayList<ListItem> listMembers(int areaPos,int householdPos){
        ArrayList<ListItem>listData = new ArrayList<>();
        for(Member member : CRUDFlinger.getAreas().get(areaPos).getResources().get(householdPos).getMembers()) {
            if (member.getImageCollection().size() > 0) {
                int last = member.getImageCollection().size() - 1;
                ImageData image = member.getImageCollection().get(last);
                String imageData = image.getImageData();
                Bitmap actImage = BitmapFactory.decodeByteArray(Base64.decode(imageData, Base64.DEFAULT), 0, Base64.decode(imageData, Base64.DEFAULT).length);
                listData.add(new ListItem(R.drawable.ic_like, member.getName(), "Age: " + getAge(member.getBirthday()) + " Relationship:  " + member.getRelationship(), "" + areaId, "" + householdId, actImage));
            } else
                listData.add(new ListItem(R.drawable.ic_like, member.getName(), "Age: " + getAge(member.getBirthday()) + " Relationship:  " + member.getRelationship(), "" + areaId, "" + householdId, null));
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
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar dob = Calendar.getInstance();


        Calendar today = Calendar.getInstance();
        System.out.println("Today year: " + today.get(Calendar.YEAR));

        Date date;
        try {
            date = formatter.parse(bday);
            dob.setTimeInMillis(date.getTime());
            System.out.println("DOB Year: " + dob.get(Calendar.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }


        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age + "";
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("", "restarted");
        args = getArguments();
        if (args  != null && args.containsKey("Area Index")){
            areaId = args.getInt("Area Index");
            householdId = args.getInt("House Index");
        }
        title.setText(CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getName());
        mAdapter = new TransitionListAdapterMember(getActivity(),listMembers(areaId, householdId));
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
