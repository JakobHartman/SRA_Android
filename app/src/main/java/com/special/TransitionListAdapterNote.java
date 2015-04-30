package com.special;

/**
 * Created by chad on 4/13/15.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DeleteRecord;

import org.rbdc.sra.objects.Note;

import java.util.ArrayList;




public class  TransitionListAdapterNote extends BaseAdapter {

    ViewHolder viewHolder;
    private ArrayList<ListItem> mItems = new ArrayList<>();
    private Context mContext;


    public TransitionListAdapterNote(Context context, ArrayList<ListItem> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = convertView;

        if(convertView==null){

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
        final String item = mItems.get(position).getTitle();
        final String desc = mItems.get(position).getDesc();
        final int imageid = mItems.get(position).getImageId();
        int id = 0;
        int house = 0;
        if(mItems.get(position).getNr() != null){
            id  = Integer.parseInt(mItems.get(position).getNr());
        }
        if(mItems.get(position).getNrTxt() != null){
            house = Integer.parseInt(mItems.get(position).getNrTxt());
        }
        final int houseId = id;
        final int areaId = id;

        viewHolder.image.setImageResource(imageid);
        if(mItems.get(position).getPictureTaken() != null){
            viewHolder.image.setImageBitmap(mItems.get(position).getPictureTaken());
        }
        Log.i("posotion", houseId + " " + areaId);
        viewHolder.title.setText(item);
        viewHolder.descr.setText(desc);
        final ImageButton hiddenView = (ImageButton) v.findViewById(R.id.hidden_view1);
        final ImageButton hiddenViewEdit = (ImageButton) v.findViewById(R.id.hidden_view2);
        final ImageView image = (ImageView) v.findViewById(R.id.item_image);




        hiddenViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListItemNote(position);
                UISwipableList list = (UISwipableList)parent;
                list.onTouchEvent(buildEvent());
            }
        });

        hiddenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteListItemNote(position);
                UISwipableList list = (UISwipableList)parent;
                list.onTouchEvent(buildEvent());
            }
        });


        return v;
    }


    static class ViewHolder {
        TextView title;
        TextView descr;
        UICircularImage image;
        int position;
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    /**************************** Edit Note **********************************/

    public void editListItemNote(final int position) {
        final Dialog dialog = new Dialog(mContext);
        final Note theNote = CRUDFlinger.getNote(position);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bar_separator_color));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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
        dialog.show();
    }


    /**************************** delete Note **********************************/

    public void deleteListItemNote(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Translucent);
        builder.setTitle("Delete Note: " + CRUDFlinger.getNote(position).getNoteTitle());
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                DeleteRecord.addNote(CRUDFlinger.getNote(position));
                CRUDFlinger.getNotes().remove(position);
                updateNotes(position);
                CRUDFlinger.saveNotes();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private ArrayList<ListItem> listNotes(int position) {
        ArrayList<ListItem> listData = new ArrayList<>();
        for (Note note: CRUDFlinger.getNotes()) {
            listData.add(new ListItem(R.drawable.ic_like, "Title: " + note.getNoteTitle(),"Updated: " + note.getDateUpdated(),null,null,null));
        }
        return listData;
    }

    public void updateNotes(int position) {
        this.mItems = listNotes(position);
        this.notifyDataSetChanged();
    }

    private MotionEvent buildEvent(){
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = -1.0f;
        float y = 10.0f;
// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_MOVE,
                x,
                y,
                metaState
        );

        return motionEvent;
    }

}
