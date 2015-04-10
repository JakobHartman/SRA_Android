package com.special;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.special.menu.ResideMenu;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class StatsFragment extends Fragment {

    //Layouts
    private ResideMenu resideMenu;
    Button btn, btnCancel;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ScrollView v =  (ScrollView) inflater.inflate(R.layout.fragment_stats, container, false);
        View parentView = inflater.inflate(R.layout.fragment_stats, container, false);

        TextView title = (TextView) parentView.findViewById(R.id.title);
        TextView areaCount = (TextView) parentView.findViewById(R.id.areas);
        areaCount.setText("Number of Areas: " + CRUDFlinger.getAreas().size() + "");
        TextView qsCount = (TextView) parentView.findViewById(R.id.qs);
        qsCount.setText("Number of Question Sets: " + CRUDFlinger.getQuestionSets().size() + "");
        TextView noteCount = (TextView) parentView.findViewById(R.id.notes);
        noteCount.setText("Number of Notes: " +CRUDFlinger.getNotes().size() +"" );




        GraphView graph = (GraphView) parentView.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, -1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);

        title.setText("Sample Charts");


        return parentView;
    }

    public void setUp() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Number areas: ", CRUDFlinger.getAreas().size() + "");
        Log.e("Number of qs: ", CRUDFlinger.getQuestionSets().size() + "");
        Log.e("Number of Notes: ", CRUDFlinger.getNotes().size() +"");



    }

}
