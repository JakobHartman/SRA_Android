package com.special;

<<<<<<< Updated upstream:app/src/main/java/com/special/DetailListAdapter.java
import org.rbdc.sra.R;
=======
>>>>>>> Stashed changes:app/src/main/java/com/special/DetailListAdapter.java
import com.special.utils.UICircularImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DetailListAdapter{
 
        @SuppressLint("InflateParams")
		public static View getView(ListItem item, Context context) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(com.special.R.layout.comment, null, false);
 
            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(com.special.R.id.name);
            TextView valueView = (TextView) rowView.findViewById(com.special.R.id.comment);
            UICircularImage imageview = (UICircularImage) rowView.findViewById(com.special.R.id.profile);
 
            // 4. Set the text for textView 
            labelView.setText(item.getTitle());
            valueView.setText(Html.fromHtml(item.getDesc()));
            imageview.setImageResource(item.getImageId());
 
            // 5. retrn rowView
            return rowView;
        }
       
}