package com.special;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Base64;
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
    private View parentView;
    private UISwipableList listView;
    public TransitionListAdapter mAdapter;
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
    private Button noteButton;
    private List<Note> notes_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_member, container, false);
        args = getArguments();
        if (args  != null && args.containsKey("Area Index")){
            areaId = args.getInt("Area Index");
            householdId = args.getInt("House Index");
        }
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


                        newNote = new Note(navigation, noteTitle.getText().toString(), noteContent.getText().toString(), CRUDFlinger.getAreas().get(areaId).getName(), CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getHouseholdID());
                        // Add note to the list
                        System.out.println("HouseholdID for the Note = " + CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).getHouseholdID());
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
        mAdapter = new TransitionListAdapter(getActivity(), listMembers(areaId, householdId));
        listView.setAdapter(mAdapter);
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
}
