package com.special;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.graphics.Color;
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
    private TransitionListAdapterNote mAdapter;
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

        mAdapter = new TransitionListAdapterNote(getActivity(),getListData());
        listView.setAdapter(mAdapter);
        listView.setActionLayout(R.id.hidden);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);

        //Clicking on a note from the list
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) { 
                ListItem item = (ListItem) listView.getAdapter().getItem(i);
                final Note theNote = CRUDFlinger.getNote(i);
                final Dialog dialog = new Dialog(getActivity());

                dialog.setTitle("Preview");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.view_note);
                TextView title = (TextView) dialog.findViewById(R.id.noteTitle);
                title.setText(theNote.getNoteTitle());
                title.setTextColor(Color.BLACK);
                TextView text = (TextView) dialog.findViewById(R.id.note_text);
                text.setText(theNote.getNoteContents());
                text.setTextColor(Color.BLACK);

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

                Button edit_button = (Button) dialog.findViewById(R.id.note_edit);
                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setContentView(R.layout.new_note);
                        dialog.setTitle(("Edit"));
                        final EditText editText = (EditText) dialog.findViewById(R.id.note_text);
                        editText.setText(theNote.getNoteContents());
                        final EditText editTitle = (EditText) dialog.findViewById(R.id.noteTitle);
                        editTitle.setText(theNote.getNoteTitle());

                        Button saveButton = (Button) dialog.findViewById(R.id.note_save);
                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Save the changes
                                theNote.editNoteTitle(editTitle.getText().toString());
                                theNote.editNoteContents(editText.getText().toString());
                                dialog.dismiss();
                                // Call to a CRUD editNote method
                                // The method will need to save the change
                                CRUDFlinger.saveNotes();

                                // This refreshes the fragment to reflect the changes
                                Fragment frg = getFragmentManager().findFragmentByTag("notes");
                                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();
                            }
                        });

                        Button cancelButton = (Button) dialog.findViewById(R.id.note_cancel);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
;
                            }
                        });

                    }
                });
                dialog.show();
            }
        });
    }

    // This gets the note data for the ListItem object Type
    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();

        for (Note note: CRUDFlinger.getNotes()) {
            listData.add(new ListItem(R.drawable.ic_like,"Title: " + note.getNoteTitle(),"Updated: " + note.getDateUpdated(),null,null,null));

        }
        return listData;
    }
}
