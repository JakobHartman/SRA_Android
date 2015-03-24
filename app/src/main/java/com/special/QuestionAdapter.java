package com.special;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.special.utils.EditQuestionFrag;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.DatapointTypes;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chad on 3/19/15.
 */
public class QuestionAdapter extends BaseAdapter {
    private final Context context;
    private final int questionSetPosition;
    private ArrayList<QuestionSet> questionSets;
    private FragmentManager fm;
    private QuestionSet questionSet;

    public QuestionAdapter(Context context, FragmentManager fm, int questionSetPosition) {
        this.context = context;
        this.questionSetPosition = questionSetPosition;
        this.questionSet = CRUDFlinger.getQuestionSet(questionSetPosition);

        this.fm = fm;
    }

    @Override public long getItemId(int position) { return position;}

    @Override public Object getItem(int position) {
        if (questionSet != null && questionSet.getQuestions() != null) {
            return questionSet.getQuestions().get(position);
        }
        return null;
    }
    @Override public int getCount() {
        if (questionSet != null && questionSet.getQuestions() != null) {
            return questionSet.getQuestions().size();
        }
        return 0;
    }

    /**
     * Get view is where the adapter creates
     * each list item
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.question_list_item, parent, false);

        final Question q = questionSet.getQuestions().get(position);

        TextView textView = (TextView) itemView.findViewById(R.id.list_item_name);
        textView.setText(q.getName());

        final QuestionAdapter theAdapter = this;
        Button delete = (Button) itemView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionSet.deleteQuestion(q);
                theAdapter.notifyDataSetChanged();
                saveQuestionSets();
            }
        });

        // Edit Individual Questions
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openQuestionDialog(position);
            }
        });

        return itemView;
    }

    public void saveQuestionSets() {
        CRUDFlinger.saveQuestionSets();
        questionSets = CRUDFlinger.getQuestionSets();
    }

    public void openQuestionDialog(int position) {
        EditQuestionFrag goToEdit =  new EditQuestionFrag();
        Bundle args = new Bundle();
        args.putInt("question",position);
        args.putInt("question set", questionSetPosition);
        goToEdit.setArguments(args);
        // Change the fragment
        fm.beginTransaction().replace(R.id.main_fragment,goToEdit, "Edit Question")
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();
    }


}

