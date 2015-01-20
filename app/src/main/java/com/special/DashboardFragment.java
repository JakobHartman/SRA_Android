package com.special;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.ExpandableListAdapter;
import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Households;

import com.special.menu.ResideMenu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // get the listview
        expListView = (ExpandableListView) parentView.findViewById(R.id.lvExp);

        // Get Households

        // preparing list data
        prepareListData();

        System.out.println("After Preparing..."+listDataChild.get("Juja"));
        listAdapter = new ExpandableListAdapter(parentView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        //setUpViews();
        return parentView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> area0 = new ArrayList<String>();
        List<Households> households = new ArrayList();
        List<Areas> areas = new ArrayList();

        // Get areas
        try {
            areas = CRUDFlinger.getAreas();
        } catch (Exception e) {

        }

        if (areas.isEmpty() || areas == null) {
            listDataHeader.add("Hello!");
            area0.add("You need to sync or add areas");
            listDataChild.put(listDataHeader.get(0), area0);

        } else {

            // Add categories
            for (Areas a : areas) {
                // add area name to the list of headers for the adapter
                listDataHeader.add(a.getAreaName());
                //Get the households in area
                households = a.getHouseholds();
                List<String> houseNames = new ArrayList();
                //For each household in the list of households
                for (Households h : households) {
                    //for each household add their name to the list
                    houseNames.add(h.getHouseholdName());
                }
                //put the list of names with the area
                listDataChild.put(a.getAreaName(),houseNames);
                System.out.println("listDataChild ="+a.getAreaName()+" "+houseNames.get(0));

            }


            /*
            listDataHeader.add("Area 1");
            listDataHeader.add("Area 2");
            listDataHeader.add("Area 3");
            */

            // Fill child list 1
            List<String> area1 = new ArrayList<String>();
            area1.add("Household 1");
            area1.add("Household 2");


            // Fill Child list 2
            List<String> area2 = new ArrayList<String>();
            area2.add("Household 1");
            area2.add("Household 2");

            /*
            // Fill child list 3
            List<String> area3 = new ArrayList<String>();
            area3.add("Household 1");
            area3.add("Household 2");
            area3.add("Household 3");
            */


            //listDataChild.put(listDataHeader.get(0), area1); // Header, Child data
            //listDataChild.put(listDataHeader.get(1), area2);
            // listDataChild.put(listDataHeader.get(2), area3);

        }


    }
/*
    private void setUpViews() {
        Dashboard parentActivity = (Dashboard)getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });
        
        parentView.findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SyncUpload syncUp = new SyncUpload();
                syncUp.startUpload();
                /* TEMPLATE STUFF
            	String url = "http://codecanyon.net/user/sherdleapps/portfolio?ref=sherdleapps";
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);
            }
        });
        
        
    }
                */

}
