package com.special;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.special.utils.UITabs;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Member;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMember extends android.support.v4.app.Fragment {
    private View parentView;
    private Bundle args;
    private int areaId;
    private int householdId;

    public AddMember() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_add_member, container, false);

        args = getArguments();
        if (args  != null && args.containsKey("Area Index")){
            areaId = args.getInt("Area Index");
            householdId = args.getInt("House Index");
        }
        final Member member = new Member();

        final EditText areaText = (EditText) parentView.findViewById(R.id.editText);
        final Spinner relationship = (Spinner)parentView.findViewById(R.id.spinner1);
        final DatePicker datePicker = (DatePicker)parentView.findViewById(R.id.datePicker);
        final Spinner education = (Spinner)parentView.findViewById(R.id.spinner2);
        final UITabs gender = (UITabs)parentView.findViewById(R.id.toggle);
        final UITabs school = (UITabs)parentView.findViewById(R.id.toggle3);
        final Button btn, btnCancel;


        btn = (Button) parentView.findViewById(R.id.btn);
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

                    CRUDFlinger.getAreas().get(areaId).getResources().get(householdId).addMember(member);
                    System.out.println("You added " + member.getName());
                    CRUDFlinger.saveRegion();
                    getFragmentManager().popBackStackImmediate();

                }
            }
        });

        btnCancel = (Button) parentView.findViewById(R.id.btncancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
                System.out.println("You clicked on the cancel button");
            }

        });
        return parentView;
    }

    public static String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        return day + "/" + month + "/" + year;
    }


}
