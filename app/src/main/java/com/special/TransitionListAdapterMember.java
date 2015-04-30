package com.special;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;
import com.special.utils.UITabs;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DeleteRecord;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.ImageData;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransitionListAdapterMember extends BaseAdapter {

	   ViewHolder viewHolder;
        private ArrayList<ListItem> mItems = new ArrayList<>();
        private Context mContext;


        public TransitionListAdapterMember(Context context, ArrayList<ListItem> list) {
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
            Log.i("posotion",houseId + " " + areaId);
            viewHolder.title.setText(item);
            viewHolder.descr.setText(desc);
            final ImageButton hiddenView = (ImageButton) v.findViewById(R.id.hidden_view1);
            final ImageButton hiddenViewEdit = (ImageButton) v.findViewById(R.id.hidden_view2);
            final ImageView image = (ImageView) v.findViewById(R.id.item_image);

//            image.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                        CRUDFlinger.save("house", houseId);
//                        CRUDFlinger.save("area", areaId);
//                        CRUDFlinger.save("pos", position);
//
//                    image.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    Activity act = (Dashboard) mContext;
//                    act.startActivityForResult(intent, 1);
//                }
//            });


            hiddenViewEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    editListItemMember(areaId,houseId,position);
                    UISwipableList list = (UISwipableList)parent;
                    list.onTouchEvent(buildEvent());


                }
            });

            hiddenView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    deleteListItemMember(areaId, houseId, position);
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




    /**************************** delete List Item Member **********************************/

    private void deleteListItemMember(final int area,final int house,final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete member: " + CRUDFlinger.getAreas().get(area).getResources().get(house).getMembers().get(position).getName());
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    DeleteRecord.addMember(CRUDFlinger.getAreas().get(area).getResources().get(house).getMembers().get(position));
                    CRUDFlinger.getAreas().get(area).getResources().get(house).getMembers().remove(position);
                    updateMember(area,house);
                    CRUDFlinger.saveRegion();
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


    /**************************** Edit List Item Member **********************************/

        public void editListItemMember(final int position,final int house,final int member){
            DeleteRecord.addMember(CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member));
            final Dialog dialog = new Dialog(mContext,
                    android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_member_dialog);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
            areaText.setText(CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).getName());
            final Spinner relationship = (Spinner)dialog.findViewById(R.id.spinner1);
            relationship.setSelection(getIndex(relationship,CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).getRelationship()));
            final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
            final Spinner education = (Spinner)dialog.findViewById(R.id.spinner2);
            education.setSelection(getIndex(education,CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).getEducationLevel()));
            final UITabs gender = (UITabs)dialog.findViewById(R.id.toggle);
            final UITabs school = (UITabs)dialog.findViewById(R.id.toggle3);


            Button btn = (Button) dialog.findViewById(R.id.btn);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = new Toast(mContext);
                    if (areaText.getText().toString().matches("")) {
                        toast.makeText(mContext,"Please Enter A Valid Name",Toast.LENGTH_SHORT).show();
                    } else if (relationship.getSelectedItemPosition() == 0) {
                        toast.makeText(mContext,"Please Select A Valid Relationship",Toast.LENGTH_SHORT).show();
                    } else if (education.getSelectedItemPosition() == 0) {
                        toast.makeText(mContext,"Please Select A Valid Education Level",Toast.LENGTH_SHORT).show();
                    } else {
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setName(areaText.getText().toString());
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setRelationship(relationship.getSelectedItem().toString());
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setBirthday(getDateFromDatePicker(datePicker));
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setEducationLevel(education.getSelectedItem().toString());
                        int genderSelected = gender.getCheckedRadioButtonId();
                        RadioButton gSelected = (RadioButton)gender.findViewById(genderSelected);
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setGender(gSelected.getText().toString());
                        int schoolSelected = school.getCheckedRadioButtonId();
                        RadioButton sSelected = (RadioButton)school.findViewById(schoolSelected);
                        boolean isInSchool;
                        switch (sSelected.getText().toString()){
                            case "Yes":
                                isInSchool = true;
                                break;
                            case "No":
                                isInSchool = false;
                                break;
                            default:
                                isInSchool = false;
                                break;
                        }
                        CRUDFlinger.getAreas().get(position).getResources().get(house).getMembers().get(member).setInschool(isInSchool);
                        dialog.cancel();
                        CRUDFlinger.saveRegion();
                        updateMember(position,house);
                    }

                }
            });


            Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);

            btnCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }

            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
            dialog.show();
        }

        private ArrayList<ListItem> listMembers(int areaPos,int householdPos){
            ArrayList<ListItem>listData = new ArrayList<>();
            for(Member member : CRUDFlinger.getAreas().get(areaPos).getResources().get(householdPos).getMembers()){
                if (member.getImageCollection().size() > 0) {
                    int last = member.getImageCollection().size() - 1;
                    ImageData image = member.getImageCollection().get(last);
                    String imageData = image.getImageData();
                    Bitmap actImage = BitmapFactory.decodeByteArray(Base64.decode(imageData, Base64.DEFAULT), 0, Base64.decode(imageData, Base64.DEFAULT).length);
                    listData.add(new ListItem(R.drawable.ic_like, member.getName(), "Age: " + getAge(member.getBirthday()) + " Relationship:  " + member.getRelationship(), "" + areaPos, "" + householdPos, actImage));
                } else {
                    listData.add(new ListItem(R.drawable.ic_like, member.getName(), "Age: " + getAge(member.getBirthday()) + " Relationship:  " + member.getRelationship(), "" + areaPos, "" + householdPos,null));
                }
            }
            return listData;
        }


        public void updateMember(int area,int house){
            this.mItems = listMembers(area,house);
            this.notifyDataSetChanged();
        }


        public String getAge(String bday){
            DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            String time;
            time = new String();
            try {
                Date date = formatter.parse(bday);
                Date current = new Date();
                long diff = current.getTime() - date.getTime();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date.getTime());
                int mYear = c.get(Calendar.YEAR);
                c.setTimeInMillis(current.getTime());
                int cYear = c.get(Calendar.YEAR);

                time = cYear - mYear + "";
            }catch (ParseException e){

            }
            return time;
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

        public static String getDateFromDatePicker(DatePicker datePicker){
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year =  datePicker.getYear();

            String time = day + "/" + month + "/" + year;

            return time;
        }

}