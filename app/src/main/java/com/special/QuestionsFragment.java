package com.special;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;

public class QuestionsFragment extends Fragment {
    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ArrayList<ListItem> listData;
    private ResideMenu resideMenu;

    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_questions, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        initView();
        return parentView;
    }

    private void initView(){
        listData = new ArrayList<ListItem>();
        loadListData();
        mAdapter = new TransitionListAdapter(getActivity(), listData);
        listView.setActionLayout(R.id.hidden_view2);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) {
//                listData.remove(i);
//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadListData(){
        listData.clear();
        ArrayList<QuestionSet> sets = CRUDFlinger.getQuestionSets();
        for (QuestionSet s : sets) {
            listData.add(new ListItem(R.drawable.ic_question, s.getName(), "", "", ""));
        }
        listData.add(new ListItem(R.drawable.ic_question, "Nutrition", "Household", null, null));
        listData.add(new ListItem(R.drawable.ic_question, "Water Access", "Community", null, null));
    }
}
