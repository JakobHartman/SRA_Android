package com.special;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.special.menu.ResideMenu;
import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;
import java.util.Random;

public class QuestionsFragment extends Fragment {
    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private QuestionSetListAdapter mAdapter;
    private ResideMenu resideMenu;

    private ArrayList<QuestionSet> questionSets;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_questions, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();

        questionSets = CRUDFlinger.getQuestionSets();
        final Random r = new Random();
        while (questionSets.size() < 3) {
            questionSets.add(new QuestionSet("Question Set " + r.nextInt(), ""));
        }

        initView();

        Button newButton = (Button) parentView.findViewById(R.id.new_question_set_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionSets.add(new QuestionSet("Question Set " + r.nextInt(), ""));
                mAdapter.notifyDataSetChanged();
            }
        });

        return parentView;
    }

    private void initView(){
        mAdapter = new QuestionSetListAdapter(getActivity(), questionSets);
        listView.setActionLayout(R.id.hidden_view1);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
//                ListItem item = (ListItem) listView.getAdapter().getItem(i);
//
//                Intent intent = new Intent(getActivity(), TransitionDetailActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("title", item.getTitle());
//                bundle.putInt("img", item.getImageId());
//                bundle.putString("descr", item.getDesc());
//
//                int[] screen_location = new int[2];
//                View view = viewa.findViewById(R.id.item_image);
//                view.getLocationOnScreen(screen_location);
//
//                bundle.putInt(PACKAGE + ".left", screen_location[0]);
//                bundle.putInt(PACKAGE + ".top", screen_location[1]);
//                bundle.putInt(PACKAGE + ".width", view.getWidth());
//                bundle.putInt(PACKAGE + ".height", view.getHeight());
//
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//                getActivity().overridePendingTransition(0, 0);
            }
        });
    }

    private void removeQuestionSet(QuestionSet qs) {
        questionSets.remove(qs);
        mAdapter.notifyDataSetChanged();
        CRUDFlinger.saveQuestionSets();
        Toast toast = Toast.makeText(getActivity(), "Deleted " + qs.getName(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.add("New").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getTitle().equals("New")) {
//            questionSets.add(new QuestionSet("Set 23984", ""));
//            mAdapter.notifyDataSetChanged();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /*
     *
     */
    class QuestionSetListAdapter extends BaseAdapter {

        ViewHolder viewHolder;

        //private ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private ArrayList<QuestionSet> mItems = new ArrayList<QuestionSet>();
        private Context mContext;

        public QuestionSetListAdapter(Context context, ArrayList<QuestionSet> list) {
            mContext = context;
            mItems = list;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(true){ //convertView==null){

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

            }else{
                // just use the viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String item = mItems.get(position).getName(); //getTitle();
            final String desc = mItems.get(position).getType(); //getDesc();
            final int imageid = R.drawable.ic_question; // mItems.get(position).getImageId();

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
                    //alert.setTitle("Data point");

                    Button yesButton = (Button) alert.findViewById(R.id.button_yes);
                    Button noButton = (Button) alert.findViewById(R.id.button_no);

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            removeQuestionSet(mItems.get(position));
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
//            viewHolder.image.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, item + " icon clicked",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//            });

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
