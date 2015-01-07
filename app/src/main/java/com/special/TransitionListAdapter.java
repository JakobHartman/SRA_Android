package com.special;

import java.util.ArrayList;

<<<<<<< Updated upstream:app/src/main/java/com/special/TransitionListAdapter.java
import org.rbdc.sra.R;
=======
>>>>>>> Stashed changes:app/src/main/java/com/special/TransitionListAdapter.java
import com.special.utils.UICircularImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if(convertView==null){
                
                // inflate the layout
            	LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(com.special.R.layout.fragment_list_item_transition, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) v.findViewById(com.special.R.id.item_title);
                viewHolder.descr = (TextView) v.findViewById(com.special.R.id.item_description);
                viewHolder.image = (UICircularImage) v.findViewById(com.special.R.id.item_image);
 
                // store the holder with the view.
                v.setTag(viewHolder);
                 
            }else{
                // just use the viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String item = mItems.get(position).getTitle();
            final String desc = mItems.get(position).getDesc();
            final int imageid = mItems.get(position).getImageId();
            
            viewHolder.image.setImageResource(imageid);
            viewHolder.title.setText(item);
            viewHolder.descr.setText(desc);
<<<<<<< Updated upstream:app/src/main/java/com/special/TransitionListAdapter.java
            TextView hiddenView = (TextView) v.findViewById(R.id.hidden_view1);
=======
            TextView hiddenView = (TextView) v.findViewById(com.special.R.id.hidden_view);
>>>>>>> Stashed changes:app/src/main/java/com/special/TransitionListAdapter.java
            hiddenView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, item + " hidden view clicked",
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

    }