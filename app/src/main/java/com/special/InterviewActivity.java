package com.special;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.special.menu.ResideMenu;
import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Interview;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;

/*
    This is the activity that is called when someone clicks
    on the Questions button from the households and members
    views

 */

public class InterviewActivity extends Activity {
    private UISwipableList responseSetList;
    private ResponseSetAdapter responseSetAdapter;
    private ResideMenu resideMenu;

    private ArrayList<QuestionSet> responseSets;
    private ArrayList<ListItem> listItems;
    private ArrayList<Interview> interviews = new ArrayList<>();

    private Interview interview;
    private int areaID;
    private int householdID;
    private String interviewType;
    private Household household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        TextView title = (TextView) findViewById(R.id.title);


        //CRUDFlinger.setApplication(getApplication());

        // Pull information from the Intent that got us here
        Intent intent = getIntent();
        // getIntExtra (tag, default value)
        areaID = intent.getIntExtra("areaID", -1);
        householdID = intent.getIntExtra("householdID", -1);
        interviewType = intent.getStringExtra("interviewType");
        System.out.println(interviewType);
        if (areaID < 0) {
            System.out.println("areaID was either not passed from AreasFragment or is invalid");
            return;
        }
        if (householdID < 0) {
            System.out.println("householdID was either not passed from AreasFragment or is invalid");
            return;
        }
        if (interviewType.equals("household")) {
            household = CRUDFlinger.getAreas().get(areaID).getResources().get(householdID);
            interviews = household.getInterviews();

            //This should never happen because there needs to be one in Firebase
            if (interviews.isEmpty()){
                Log.i("Interview Activity", "Interviews array was empty. Adding a new interview");
                interviews.add(new Interview());
            }

            interview = interviews.get(0);
            responseSets = interview.getQuestionsets();


        /*********** Testing *****************************/
            for (QuestionSet qs: responseSets) {
                Log.i("set inside Interview: ",qs.getName());
            }
        /**************************************************/

            title.setText(household.getName() + " -> Response Sets");
        }

        /***************** something is broken ************/

        else if (interviewType.equals("area")) {
            Area area = CRUDFlinger.getAreas().get(areaID);
            ArrayList<Interview> interviews = area.getInterviews();
            if (interviews.isEmpty()) {
                interviews.add(new Interview());
            }

            interview = interviews.get(0);

            // Gets the questionSets
            responseSets = interview.getQuestionsets();
            title.setText(area.getName() + " -> Response Sets");
        }

        /***************** ^^^^^^^^^^^^^^^^^^^^ ************/

        // Put the Surverys in a swipable list
        responseSetList = (UISwipableList) findViewById(R.id.list_view);
        listItems = new ArrayList<ListItem>();

        // Put items in the List
        populateListItems();
        setupList();

        // Add response set Button
        Button addButton = (Button) findViewById(R.id.add_response_set_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResponseSetSelectDialog();
            }
        });
    }

    //responseSetList is a swipable list
    private void setupList() {
        responseSetAdapter = new ResponseSetAdapter(this, listItems);
        responseSetList.setActionLayout(R.id.hidden);
        responseSetList.setItemLayout(R.id.front_layout);
        responseSetList.setAdapter(responseSetAdapter);
        responseSetList.setIgnoredViewHandler(resideMenu);
        responseSetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
                goToDataCollect(i);
            }
        });
    }


    // Method for putting items in "listItems"
    private void populateListItems() {
        listItems.clear();

        // What's the purpose of this??
        String[] typesArray = getResources().getStringArray(R.array.question_set_types_array);

        // For each question set add an icon, name, ... idk
        try {
            for (QuestionSet qs : responseSets) {
                listItems.add(new ListItem(
                        R.drawable.ic_home,
                        qs.getName(),
                        qs.getType(),
                        null, null,null));
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    private QuestionSet addResponseSet(QuestionSet qs) {
        // Why are we converting into JSON
        // then back to QuestionSet??

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(qs);
        QuestionSet clonedQS = gson.fromJson(json, QuestionSet.class);


        responseSets.add(qs);

        //set in crud
        //CRUDFlinger.getAreas().get(areaID).getResources().get(householdID).getInterviews().get(0).setQuestionsets(responseSets);
        populateListItems();
        responseSetAdapter.notifyDataSetChanged();
        return qs;
    }

    private void openResponseSetSelectDialog() {
        final Dialog alert = new Dialog(this);
        alert.setContentView(R.layout.response_set_dialog);
        alert.setCancelable(true);
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ArrayList<QuestionSet> sets = new ArrayList<QuestionSet>();
        if (interviewType.equals("household")) sets = CRUDFlinger.getQuestionSets("HOUSEHOLD");
        else if (interviewType.equals("area")) sets = CRUDFlinger.getQuestionSets("AREA");
        // final sets are the result of either household or area sets
        final ArrayList<QuestionSet> finalSets = sets;
        QuestionSetSelectionAdapter adapter = new QuestionSetSelectionAdapter(this, sets);
        final ListView list = (ListView) alert.findViewById(R.id.list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
                //clones the questions set selected
                QuestionSet clonedSet = addResponseSet(finalSets.get(i));
                alert.dismiss();
                goToDataCollect(responseSets.indexOf(clonedSet));
            }
        });

        Button cancel = (Button) alert.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void goToDataCollect(int responseSetIndex) {

        Intent intent = new Intent(this, DataCollect.class);
        intent.putExtra("areaID", areaID);
        intent.putExtra("householdID", householdID);
        intent.putExtra("interviewType", interviewType);
        intent.putExtra("responseSetIndex", responseSetIndex);
        startActivity(intent);
    }

    /**
     * Response Set Adapter
     */
    class ResponseSetAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        private ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private Context mContext;

        public ResponseSetAdapter(Context context, ArrayList<ListItem> list) {
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
                    final Dialog alert = new Dialog(mContext);
                    alert.setContentView(R.layout.confirmation_dialog);
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Button yesButton = (Button) alert.findViewById(R.id.yes_button);
                    Button noButton = (Button) alert.findViewById(R.id.no_button);

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            responseSets.remove(position);
                            listItems.remove(position);
                            notifyDataSetChanged();
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

    /**
     * Question set selection adapter
     * This is the adapter used in the dialog
     * that lists the Surveys
     */

    public class QuestionSetSelectionAdapter extends ArrayAdapter<QuestionSet> {
        private Context context;
        private ArrayList<QuestionSet> questionSets;

        public QuestionSetSelectionAdapter(Context context, ArrayList<QuestionSet> sets) {
            super(context, R.layout.response_set_selection_list_item, sets);
            this.context = context;
            this.questionSets = sets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuestionSet set = questionSets.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.response_set_selection_list_item, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.list_item_name);
            textView.setText(set.getName());
            return convertView;
        }
    }
}
