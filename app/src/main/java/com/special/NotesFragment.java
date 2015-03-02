package com.special;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Note;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

public class NotesFragment extends Fragment {

	//Views & Widgets
    private View parentView;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ResideMenu resideMenu;
    
    //Vars
    private String PACKAGE = "IDENTIFY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_notes, container, false);
        listView   = (UISwipableList) parentView.findViewById(R.id.listView);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();
        initView();
        return parentView;
    }

    private void initView(){
    	mAdapter = new TransitionListAdapter(getActivity(),getListData());
        listView.setActionLayout(R.id.hidden_view1);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) { 
                ListItem item = (ListItem) listView.getAdapter().getItem(i);
                Note theNote = CRUDFlinger.getNote(i);
                final Dialog dialog = new Dialog(getActivity());

                dialog.setTitle(theNote.getNoteTitle());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.view_note);
                TextView title = (TextView) dialog.findViewById(R.id.noteTitle);
                title.setText(theNote.getNoteTitle());
                TextView text = (TextView) dialog.findViewById(R.id.note_text);
                text.setText(theNote.getNoteContents());

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bar_separator_color));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Button ok_button = (Button) dialog.findViewById(R.id.note_ok);

                ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();

        for (Note note: CRUDFlinger.getNotes()) {
            listData.add(new ListItem(R.drawable.ic_like,"Title: " + note.getNoteTitle(),"Updated: " + note.getDate(),null,null));
        }
        return listData;
    }
}
