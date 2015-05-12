package com.special;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DeleteRecord;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

public class EditQuestionSetFrag extends Fragment {
    int qSetPosition;
    QuestionSet questionSet;
    private EditText nameField;
    private QuestionAdapter questionListAdapter;
    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parentView;
        parentView = inflater.inflate(R.layout.edit_question_set_frag, container, false);

        Bundle args = getArguments();

        // Pulls the index of the question set to be displayed
        try {
            qSetPosition = args.getInt("question set");
            questionSet = CRUDFlinger.getQuestionSet(qSetPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sets the Name of the Question Set
        nameField = (EditText) parentView.findViewById(R.id.name_field);
        nameField.setText(questionSet.getName());

        RadioButton houseButton = (RadioButton) parentView.findViewById(R.id.radio_household);
        RadioButton areaButton = (RadioButton) parentView.findViewById(R.id.radio_community);
        RadioGroup rg = (RadioGroup) parentView.findViewById(R.id.radio_type);

        // Sets the question set type
        if (questionSet.getType().equals("HOUSEHOLD")) {
            houseButton.setChecked(true);
            rg.removeView(areaButton);
            houseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"This can't be changed",Toast.LENGTH_SHORT).show();
                }
            });

        } else if (questionSet.getType().equals("AREA")) {
            areaButton.setChecked(true);
            rg.removeView(houseButton);
            areaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"This can't be changed",Toast.LENGTH_SHORT).show();
                }
            });
        }

        // The list of questions
        questionListAdapter = new QuestionAdapter(getActivity(), getFragmentManager(),qSetPosition);
        final ListView questionList = (ListView) parentView.findViewById(R.id.list_view);
        questionList.setAdapter(questionListAdapter);

        // Add button for adding a question to the set
        Button addButton = (Button) parentView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question("");
                //q.setName("New Question");
                questionSet.addQuestion(q);
                questionListAdapter.openQuestionDialog(questionSet.getQuestions().size()-1);
            }
        });

        Button finishButton = (Button) parentView.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gets the selected button's id
                RadioGroup radioTypeGroup = (RadioGroup) parentView.findViewById(R.id.radio_type);
                int selectedRadioId = radioTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) parentView.findViewById(selectedRadioId);
                String typeText = selectedButton.getText().toString();
                System.out.println("Type selected: "+ typeText);
                boolean incomplete = false;

                for (Question question : questionSet.getQuestions()) {
                    if (question.getDataPoints().isEmpty() || question.getName().toString().equals("")) {
                        incomplete = true;
                    }
                }

                // Check for title
                if (nameField.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                    builder.setTitle("Error: No title");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (questionSet.getQuestions().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                    builder.setTitle("Please add a question");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (incomplete) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
                    builder.setTitle("Error: Incomplete Question");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    questionSet.setType(typeText);
                    questionSet.setName(nameField.getText().toString());

                    //CRUDFlinger.addQuestionSet(questionSet);

                    //Save the changes
                    questionListAdapter.saveQuestionSets();


                    getFragmentManager().popBackStackImmediate();
                }

            }
        });

        return parentView;

    }

}


