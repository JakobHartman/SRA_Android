package com.special;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Note;
import java.util.ArrayList;


public class DrillDownNotes extends Fragment {
    private String viewId;
    private String type;
    private Dialog dialog;


    public static DrillDownNotes newInstance(String viewId, String type) {
        DrillDownNotes fragment = new DrillDownNotes();
        Bundle args = new Bundle();
        args.putString("viewId", viewId);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public DrillDownNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewId = getArguments().getString("viewId");
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drill_down_notes, container, false);

        final TransitionListAdapterNote adapter = new TransitionListAdapterNote(getActivity(), getListData());
        final ListView listView = (ListView) view.findViewById(R.id.drillNotes);

        Button addNote = (Button) view.findViewById(R.id.newNoteButton);


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity(),
                        android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                dialog.setTitle("New Note");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.new_note);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bar_separator_color));
                dialog.show();

                // Cancel Button
                Button note_cancel = (Button) dialog.findViewById(R.id.note_cancel);
                note_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // Save Button
                Button note_save = (Button) dialog.findViewById(R.id.note_save);
                note_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a new note
                        EditText noteTitle = (EditText) dialog.findViewById(R.id.noteTitle);
                        EditText noteContent = (EditText) dialog.findViewById(R.id.note_text);
                        Note newNote = null;

                        if (type.equals("area")) {
                            newNote = new Note("Area", noteTitle.getText().toString(), noteContent.getText().toString(), viewId);
                        } else if (type.equals("household")) {
                            newNote = new Note("Household", noteTitle.getText().toString(), noteContent.getText().toString(), "", viewId);
                        }

                        // Notify Note created
                        Toast toast = new Toast(getActivity());
                        toast.makeText(getActivity(), "Note Created: " + newNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                        CRUDFlinger.addNote(newNote);
                        adapter.notifyDataSetChanged();
                        // Close the view
                        dialog.cancel();
                    }
                });
            }
        });

        listView.setAdapter(adapter);
        //Clicking on a note from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        return view;
    }

    // This gets the note data for the ListItem object Type
    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();
        for (Note note : CRUDFlinger.getNotes()) {
            if (type.equals("area")) {
                if (note.getAreaName().equals(viewId)) {
                    listData.add(new ListItem(R.drawable.ic_like,"Title: " + note.getNoteTitle(),"Updated: " + note.getDateUpdated(),null,null,null));
                }
            } else if (type.equals("household")) {
                if (note.getHouseholdID().equals(viewId)) {
                    listData.add(new ListItem(R.drawable.ic_like,"Title: " + note.getNoteTitle(),"Updated: " + note.getDateUpdated(),null,null,null));
                }
            }

        }
        return listData;
    }


}
