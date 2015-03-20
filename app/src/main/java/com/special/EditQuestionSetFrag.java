package com.special;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
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

        // Add button for adding a question to the set
        Button addButton = (Button) parentView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question("");
                q.setName("");
                questionSet.addQuestion(q);
                CRUDFlinger.saveQuestionSets();
                questionListAdapter.notifyDataSetChanged();
                questionListAdapter.openQuestionDialog(q);
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
                questionSet.setType(typeText);

                CRUDFlinger.addQuestionSet(questionSet);

                questionListAdapter.saveQuestionSets();


                getFragmentManager().popBackStackImmediate();
            }
        });

        // Sets the question set type
        if (questionSet.getType().equals("HOUSEHOLD")) {
            RadioButton houseButton = (RadioButton) parentView.findViewById(R.id.radio_household);
            houseButton.setChecked(true);
        } else if (questionSet.getType().equals("AREA")) {
            RadioButton areaButton = (RadioButton) parentView.findViewById(R.id.radio_community);
            areaButton.setChecked(true);
        }

        // The list of questions
        questionListAdapter = new QuestionAdapter(getActivity(), questionSet);
        final ListView questionList = (ListView) parentView.findViewById(R.id.list_view);
        questionList.setAdapter(questionListAdapter);

        return parentView;

    }

}


