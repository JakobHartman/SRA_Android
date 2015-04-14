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

/**
 * This Fragment is displayed when someone clicks on the Question Sets option
 * in the dashboard menu. This file also contains the adapters that
 * are used.
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
        listItems = new ArrayList<>();
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
                openQuestionSetDialog(i);
            }
        });
    }

    // Set up each question set list item
    private void populateListItems() {
        listItems.clear();
        String[] typesArray = getResources().getStringArray(R.array.question_set_types_array);
        for (QuestionSet qs : questionSets) {
            listItems.add(new ListItem(
                    R.drawable.ic_home,
                    qs.getName(),
                    qs.getType(),
                    null, null,null));
        }
    }

    private void saveQuestionSets() {
        CRUDFlinger.saveQuestionSets();
        questionSets = CRUDFlinger.getQuestionSets();
        populateListItems();
        questionSetListAdapter.notifyDataSetChanged();
    }

    private void addQuestionSet() {
        QuestionSet newSet = new QuestionSet("","","");
        CRUDFlinger.addQuestionSet(newSet);
        CRUDFlinger.saveQuestionSets();
        openQuestionSetDialog(CRUDFlinger.getQuestionSets().size()-1);
    }

    private void removeQuestionSet(int position) {
        QuestionSet qs = questionSets.get(position);
        CRUDFlinger.deleteQuestionSet(qs);
        saveQuestionSets();

        Toast toast = Toast.makeText(getActivity(), "Deleted " + qs.getName(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * This is the new version of this method
     * to open up the fragment instead of the dialog
     * @param qsPosition
     */
    private void openQuestionSetDialog(int qsPosition) {
        EditQuestionSetFrag goToEdit = new EditQuestionSetFrag();
        Bundle args = new Bundle();
        args.putInt("question set",qsPosition);
        goToEdit.setArguments(args);
        // Change the fragment
        getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToEdit, "Edit Question Set")
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();
    }


    /**
     * Question Set List Adapter
     * This is the adapter used
     * to display this question sets
     */
    class QuestionSetListAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        private ArrayList<ListItem> mItems = new ArrayList<>();
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


}
