package com.special;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionsFragment extends Fragment {
    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private QuestionSetListAdapter mAdapter;
    private ResideMenu resideMenu;

    private ArrayList<QuestionSet> questionSets;
    private ArrayList<ListItem> listItems;

    private QuestionListAdapter questionListAdapter;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_questions, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();

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
        mAdapter = new QuestionSetListAdapter(getActivity(), listItems);
        listView.setActionLayout(R.id.hidden_view1);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
//                ListItem item = (ListItem) listView.getAdapter().getItem(i);
                openQuestionSetDialog(questionSets.get(i));
            }
        });
    }

    private void populateListItems() {
        listItems.clear();
        for (QuestionSet qs : questionSets) {
            listItems.add(new ListItem(R.drawable.ic_home, qs.getName(), qs.getType(), null, null));
        }
    }

    private void saveQuestionSets() {
        CRUDFlinger.saveQuestionSets();
        questionSets = CRUDFlinger.getQuestionSets();
        populateListItems();
        mAdapter.notifyDataSetChanged();
    }

    private void addQuestionSet() {
        QuestionSet qs = new QuestionSet("", "");
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

    private void openQuestionSetDialog(final QuestionSet qSet) {
        final Dialog alert = new Dialog(getActivity());
        alert.setContentView(R.layout.edit_question_set_dialog);
//        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

        final Spinner type = (Spinner) alert.findViewById(R.id.type_spinner);
        String[] typesArray = getResources().getStringArray(R.array.question_set_categories_array);
        final ArrayList<String> typesArrayList = new ArrayList<String>(Arrays.asList(typesArray));
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, typesArrayList);
        type.setAdapter(typesAdapter);
        type.setSelection(typesArrayList.indexOf(qSet.getType()));
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qSet.setType(typesArrayList.get(position));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        questionListAdapter = new QuestionListAdapter(getActivity(), qSet);
        final ListView questionList = (ListView) alert.findViewById(R.id.list_view);
        questionList.setAdapter(questionListAdapter);
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
                saveQuestionSets();
                alert.dismiss();
            }
        });

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

    private void addQuestion() {

    }

    private void openQuestionDialog(final Question q) {
        final Dialog alert = new Dialog(getActivity());
        alert.setContentView(R.layout.edit_question_dialog);
//        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

        final ArrayList<String> dataPointListItems = new ArrayList<String>();
        final ArrayList<Datapoint> points = q.getDataPoints();
        for (Datapoint dp : points) { dataPointListItems.add(dp.getLabel()); }
        final QuestionListAdapter dpAdapter = new QuestionListAdapter(getActivity(), null);
        final ListView questionList = (ListView) alert.findViewById(R.id.list_view);
        questionList.setAdapter(dpAdapter);

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

        Button addButton = (Button) alert.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPointListItems.add("");
                dpAdapter.notifyDataSetChanged();
            }
        });

        alert.show();
    }



    /*
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
            TextView hiddenView = (TextView) v.findViewById(R.id.hidden_view1);
            hiddenView.setText(R.string.delete_question_set_text);
            hiddenView.setOnClickListener(new View.OnClickListener() {

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
     *
     */
    public class QuestionListAdapter extends BaseAdapter {
        private final Context context;
        private final QuestionSet questionSet;

        public QuestionListAdapter(Context context, QuestionSet questionSet) {
            this.context = context;
            this.questionSet = questionSet;
        }

        @Override public long getItemId(int position) { return position;}
        @Override
        public Object getItem(int position) {
            if (questionSet != null && questionSet.getQuestions() != null) {
                return questionSet.getQuestions().get(position);
            }
            return null;
        }
        @Override
        public int getCount() {
            if (questionSet != null && questionSet.getQuestions() != null) {
                return questionSet.getQuestions().size();
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = inflater.inflate(R.layout.question_list_item, parent, false);
            TextView textView = (TextView) itemView.findViewById(R.id.list_item_name);
            final Question q = questionSet.getQuestions().get(position);
            textView.setText(q.getName());

            final QuestionListAdapter theAdapter = this;

            Button removeInterviewButton = (Button) itemView.findViewById(R.id.delete_button);
            removeInterviewButton.setOnClickListener(new View.OnClickListener() {
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
