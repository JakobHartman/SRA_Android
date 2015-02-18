package com.special;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.special.menu.ResideMenu;
import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Datapoint;
import org.rbdc.sra.objects.DatapointTypes;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;
import java.util.Arrays;

/*
    This Fragment is displayed when someone clicks on the Question Sets option
    in the dashboard menu. This file also contains the adapters that
    are used.

 */
public class QuestionsFragment extends Fragment {
    //Views & Widgets
    private View parentView;
    private UISwipableList questionSetListView;
    private ResideMenu resideMenu;

    private QuestionSetListAdapter questionSetListAdapter;
    private ArrayList<QuestionSet> questionSets;
    private ArrayList<ListItem> listItems;

    private QuestionAdapter questionListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_questions, container, false);
        questionSetListView = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        CRUDFlinger.setApplication(getActivity().getApplication());

        questionSets = CRUDFlinger.getQuestionSets();
        listItems = new ArrayList<ListItem>();
        populateListItems();
        setupList();

        Button newButton = (Button) parentView.findViewById(R.id.new_question_set_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionSet();
            }
        });

        return parentView;
    }

    private void setupList() {
        questionSetListAdapter = new QuestionSetListAdapter(getActivity(), listItems);
        questionSetListView.setActionLayout(R.id.hidden);
        questionSetListView.setItemLayout(R.id.front_layout);
        questionSetListView.setAdapter(questionSetListAdapter);
        questionSetListView.setIgnoredViewHandler(resideMenu);

        // Opens a dialog when a question set is selected
        questionSetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
                openQuestionSetDialog(questionSets.get(i));
            }
        });
    }

    private void populateListItems() {
        listItems.clear();
        String[] typesArray = getResources().getStringArray(R.array.question_set_types_array);
        for (QuestionSet qs : questionSets) {
            listItems.add(new ListItem(
                    R.drawable.ic_home,
                    qs.getName(),
                    qs.getType(),
                    null, null));
        }
    }

    private void saveQuestionSets() {
        CRUDFlinger.saveQuestionSets();
        questionSets = CRUDFlinger.getQuestionSets();
        populateListItems();
        questionSetListAdapter.notifyDataSetChanged();
    }

    private void addQuestionSet() {
        QuestionSet qs = new QuestionSet("", "","");
        CRUDFlinger.addQuestionSet(qs);
        saveQuestionSets();
        openQuestionSetDialog(qs);
    }

    private void removeQuestionSet(int position) {
        QuestionSet qs = questionSets.get(position);
        CRUDFlinger.deleteQuestionSet(qs);
        saveQuestionSets();

        Toast toast = Toast.makeText(getActivity(), "Deleted " + qs.getName(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    // Opens the dialog for editing or viewing a question set
    //
    private void openQuestionSetDialog(final QuestionSet qSet) {
        final Dialog alert = new Dialog(getActivity());
        alert.setContentView(R.layout.edit_question_set_dialog);
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // The name of the question set
        final EditText nameField = (EditText) alert.findViewById(R.id.name_field);
        nameField.setText(qSet.getName());
        nameField.setSelection(nameField.length());
        nameField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qSet.setName(nameField.getText().toString());
            }
        });

        // Radio button fo whether the question is meant for households or areas
        if (qSet.getType() == "HOUSEHOLD") {
            RadioButton houseButton = (RadioButton) alert.findViewById(R.id.radio_household);
            System.out.println("house button");
            houseButton.setChecked(true);
        } else if (qSet.getType() ==  "AREA") {
            RadioButton areaButton = (RadioButton) alert.findViewById(R.id.radio_community);
            areaButton.setChecked(true);
            System.out.println("area button");

        }


        // The list of questions
        questionListAdapter = new QuestionAdapter(getActivity(), qSet);
        final ListView questionList = (ListView) alert.findViewById(R.id.list_view);
        questionList.setAdapter(questionListAdapter);

        // Opens a dialog to view or edit the individual questions
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
                ArrayList<Question> questions = qSet.getQuestions();
                openQuestionDialog(questions.get(i));
            }
        });

        Button finishButton = (Button) alert.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gets the selected button's id
                RadioGroup radioTypeGroup = (RadioGroup) alert.findViewById(R.id.radio_type);
                int selectedRadioId = radioTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) alert.findViewById(selectedRadioId);
                String typeText = selectedButton.getText().toString();
                System.out.println("Type selected: "+ typeText);
                qSet.setType(typeText);
                saveQuestionSets();
                alert.dismiss();
            }
        });

        // Add button for adding a question to the set
        Button addButton = (Button) alert.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question("");
                q.setName("");
                qSet.addQuestion(q);
                CRUDFlinger.saveQuestionSets();
                questionListAdapter.notifyDataSetChanged();
                openQuestionDialog(q);
            }
        });

        alert.show();
    }

    private void addOption(String option, final LinearLayout optionsList, final Datapoint dp) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout optionListItem = (LinearLayout) inflater.inflate(R.layout.option_list_item, null);
        final EditText optionView = (EditText) optionListItem.findViewById(R.id.option_field);
        optionView.setText(option);
        optionView.setSelection(optionView.length());
        optionView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = optionsList.indexOfChild(optionListItem);
                dp.getOptions().set(index, optionView.getText().toString());
            }
        });

        Button deleteButton = (Button) optionListItem.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = optionsList.indexOfChild(optionListItem);
                dp.getOptions().remove(index);
                optionsList.removeView(optionListItem);
            }
        });
        optionsList.addView(optionListItem);
    }

    private void addDataPoint(final LinearLayout dpList, final Datapoint dp, final Question q) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout dpItemView = (LinearLayout) inflater.inflate(R.layout.data_point_list_item, null);
        dpList.addView(dpItemView);

        final EditText label = (EditText) dpItemView.findViewById(R.id.label_field);
        label.setText(dp.getLabel());
        label.setSelection(label.length());
        label.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dp.setLabel(label.getText().toString());
            }
        });

        final LinearLayout optionsContainer = (LinearLayout) dpItemView.findViewById(R.id.options_container);
        final LinearLayout optionsList = (LinearLayout) dpItemView.findViewById(R.id.options_list_view);
        final ArrayList<String> options = dp.getOptions();
        for (int i = 0; i < options.size(); i++) {
            addOption(options.get(i), optionsList, dp);
        }

        Button addOption = (Button) dpItemView.findViewById(R.id.add_option_button);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "";
                options.add(option);
                addOption(option, optionsList, dp);
            }
        });

        Spinner dataTypeSpinner = (Spinner) dpItemView.findViewById(R.id.data_type_spinner);
        String[] types = getResources().getStringArray(R.array.data_point_types_array);
        ArrayList<String> typesList = new ArrayList<String>(Arrays.asList(types));
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, typesList);
        dataTypeSpinner.setAdapter(typesAdapter);
        dataTypeSpinner.setSelection(DatapointTypes.getTypeIndex(dp.getType()));
        if (dp.dataTypeIsAList()) {
            optionsContainer.setVisibility(View.VISIBLE);
        }
        dataTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                dp.setType(DatapointTypes.getTypeFromIndex(position));
                if (dp.dataTypeIsAList()) optionsContainer.setVisibility(View.VISIBLE);
                else {
                    optionsContainer.setVisibility(View.GONE);
                    dp.getOptions().clear();
                    optionsList.removeAllViews();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parentView) {
                optionsContainer.setVisibility(View.GONE);
            }
        });

        Button delete = (Button) dpItemView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.deleteDataPoint(dp);
                dpList.removeView(dpItemView);
                saveQuestionSets();
            }
        });
    }

    //Opens a single question to edit from a question set
    //
    private void openQuestionDialog(final Question q) {
        final Dialog alert = new Dialog(getActivity());
        alert.setContentView(R.layout.edit_question_dialog);
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // Name of the question
        final EditText nameField = (EditText) alert.findViewById(R.id.name_field);
        nameField.setText(q.getName());
        nameField.setSelection(nameField.length());
        nameField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                q.setName(nameField.getText().toString());
            }
        });


        // A checkbox for whether or not the question can be used multiple times
        final CheckBox multiUseBox = (CheckBox) alert.findViewById(R.id.multi_use_checkbox);
        multiUseBox.setChecked(q.getMultiUse());
        multiUseBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q.setMultiUse(multiUseBox.isChecked());
            }
        });

        // List of data points
        final LinearLayout dataPointList = (LinearLayout) alert.findViewById(R.id.list_view);
        ArrayList<Datapoint> datapoints = q.getDataPoints();
        for (Datapoint dp : datapoints) {
            addDataPoint(dataPointList, dp, q);
        }

        Button finishButton = (Button) alert.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestionSets();
                if (questionListAdapter != null) {
                    questionListAdapter.notifyDataSetChanged();
                }
                alert.dismiss();
            }
        });

        // Add a new data point to the question
        Button addButton = (Button) alert.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datapoint dp = new Datapoint();
                q.addDataPoint(dp);
                addDataPoint(dataPointList, dp, q);
            }
        });

        alert.show();
    }

    /*
     * Questions Set List Adapter and delete listener
     *
     *
     *
     *
     *
     *
     *
     *
     */
    class QuestionSetListAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        private ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private Context mContext;

        public QuestionSetListAdapter(Context context, ArrayList<ListItem> list) {
            mContext = context;
            mItems = list;
        }

        @Override public int getCount() {
            return mItems.size();
        }
        @Override public Object getItem(int position) {
            return mItems.get(position);
        }
        @Override public long getItemId(int position) {
            return position;
        }
        @Override public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

//            if(convertView==null){

            // inflate the layout
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.fragment_list_item_transition, null);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) v.findViewById(R.id.item_title);
            viewHolder.descr = (TextView) v.findViewById(R.id.item_description);
            viewHolder.image = (UICircularImage) v.findViewById(R.id.item_image);

            // store the holder with the view.
            v.setTag(viewHolder);

//            }else{
//                // just use the viewHolder
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
            final String item = mItems.get(position).getTitle();
            final String desc = mItems.get(position).getDesc();
            final int imageid = mItems.get(position).getImageId();

            viewHolder.image.setImageResource(imageid);
            viewHolder.title.setText(item);
            viewHolder.descr.setText(desc);

            ImageButton edit = (ImageButton) v.findViewById(R.id.hidden_view2);
            edit.setVisibility(View.GONE);

            ImageButton delete = (ImageButton) v.findViewById(R.id.hidden_view1);
            delete.setClickable(true);
            delete.setEnabled(true);
            //delete.setText(R.string.delete_question_set_text);
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog alert = new Dialog(getActivity());
                    alert.setContentView(R.layout.confirmation_dialog);
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Button yesButton = (Button) alert.findViewById(R.id.yes_button);
                    Button noButton = (Button) alert.findViewById(R.id.no_button);

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            removeQuestionSet(position);
                        }
                    });

                    noButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });

                    alert.show();
                }
            });

            return v;
        }

        class ViewHolder {
            TextView title;
            TextView descr;
            UICircularImage image;
            int position;
        }
    }

    /*
     * Question Adapter
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    public class QuestionAdapter extends BaseAdapter {
        private final Context context;
        private final QuestionSet questionSet;

        public QuestionAdapter(Context context, QuestionSet questionSet) {
            this.context = context;
            this.questionSet = questionSet;
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

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuestionDialog(questionSet.getQuestions().get(position));
                }
            });

            return itemView;
        }
    }
}
