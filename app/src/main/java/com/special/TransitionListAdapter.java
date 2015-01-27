package com.special;

import java.util.ArrayList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;

import com.special.utils.UICircularImage;
import com.special.utils.UISwipableList;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

class TransitionListAdapter extends BaseAdapter {
	
	   ViewHolder viewHolder;

        private ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private Context mContext;

        public TransitionListAdapter(Context context, ArrayList<ListItem> list) {
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
            if(mItems.get(position).getNr() != null){
               id  = Integer.parseInt(mItems.get(position).getNr());
            }
            final int areaId = id;
            
            viewHolder.image.setImageResource(imageid);
            viewHolder.title.setText(item);
            viewHolder.descr.setText(desc);
            final ImageView hiddenView = (ImageView) v.findViewById(R.id.hidden_view1);
            final ImageView hiddenViewEdit = (ImageView) v.findViewById(R.id.hidden_view2);
            hiddenViewEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = desc.split("\\s++")[1];
                    if(text.matches("Households")){
                        editListItemArea(position);
                        UISwipableList list = (UISwipableList)parent;
                        list.onTouchEvent(buildEvent());

                    }else if(text.matches("Members")){
                        editListItemHousehold(areaId,position);
                        UISwipableList list = (UISwipableList)parent;
                        list.onTouchEvent(buildEvent());
                    }else{


                    }

                }
            });

            hiddenView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext, item ,
                            Toast.LENGTH_SHORT).show();

                }
            });
            viewHolder.image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, item + " icon clicked",
                            Toast.LENGTH_SHORT).show();
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

        public void editListItemArea(final int position){
            final Area area = CRUDFlinger.getAreas().get(position);
            final Dialog dialog = new Dialog(mContext,
                    android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_area_dialog);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
            areaText.setText(area.getName());

            final Spinner regionText = (Spinner) dialog.findViewById(R.id.spinner);
            regionText.setSelection(getIndex(regionText, area.getRegion()));

            final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
            ArrayList<String> regions = new ArrayList<String>();

            regions.add("Select Region");
            regions.addAll(loginObject.getSiteLogin().getRegionNames());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,regions);
            regionText.setAdapter(adapter);

            Button btn = (Button) dialog.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = new Toast(mContext);

                    Area newArea = new Area();
                    newArea.setResources(area.getResources());
                    newArea.setCountry(area.getCountry());
                    newArea.setInterviews(area.getInterviews());

                    if(areaText.getText().toString().matches("")){
                        toast.makeText(mContext,"Please Enter A Valid Area Name", Toast.LENGTH_LONG).show();
                    }else if(regionText.getSelectedItemPosition() == 0){
                        toast.makeText(mContext,"Please Select A Valid Region", Toast.LENGTH_LONG).show();
                    }else{

                        newArea.setRegion(regionText.getSelectedItem().toString());
                        newArea.setCountry(CRUDFlinger.getCountryName(regionText.getSelectedItem().toString()));
                        newArea.setName(areaText.getText().toString());
                        CRUDFlinger.getAreas().set(position,newArea);
                        dialog.cancel();
                        CRUDFlinger.saveRegion();
                        updateArea(position);
                    }
                }
            });

            Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }

            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
            dialog.show();
        }

        public void editListItemHousehold(final int position,final int house){
            Household household = CRUDFlinger.getAreas().get(position).getResources().get(house);
            final Dialog dialog = new Dialog(mContext,
                    android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_household_dialog);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
                areaText.setText(household.getName());
            Button btn = (Button) dialog.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = new Toast(mContext);

                    Household newHousehold = new Household();
                    if(areaText.getText().toString().matches("")){
                        toast.makeText(mContext,"Please Enter A Valid Household Name", Toast.LENGTH_LONG).show();
                    }else{
                        newHousehold.setName(areaText.getText().toString());
                        CRUDFlinger.getAreas().get(position).getResources().set(house,newHousehold);
                        dialog.cancel();
                        CRUDFlinger.saveRegion();
                        updateHousehold(position);
                    }
                }
            });

            Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }

            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
            dialog.show();
        }

        private ArrayList<ListItem> listArea(){
            ArrayList<ListItem> listData = new ArrayList<ListItem>();
            for(Area area : CRUDFlinger.getAreas()){
                listData.add(new ListItem(R.drawable.ic_like,area.getName(),area.getResources().size() + " Households",null,null));
            }
            return listData;
        }

        private ArrayList<ListItem> listHouseholds(int pos){
            ArrayList<ListItem>listData = new ArrayList<ListItem>();
            for(Household households : CRUDFlinger.getAreas().get(pos).getResources()){
                listData.add(new ListItem(R.drawable.ic_like,households.getName(),households.getMembers().size() + " Members","" + pos,null));
            }
            return listData;
        }

        public void updateArea(int position){
            this.mItems = listArea();
            this.notifyDataSetChanged();
        }

        public void updateHousehold(int position){
            this.mItems = listHouseholds(position);
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