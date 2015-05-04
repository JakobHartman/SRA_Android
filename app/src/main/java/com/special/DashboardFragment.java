package com.special;

import org.rbdc.sra.Login;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.ExpandableListAdapter;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

public class DashboardFragment extends Fragment {

    private View parentView;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private TextView listItem;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        CRUDFlinger.setApplication(getActivity().getApplication());
        for (Area area : CRUDFlinger.getAreas()){
            for(Household household : area.getResources()){
                Log.i("House ID: ",household.getHouseholdID());
            }
        }
        // get the listview
        expListView = (ExpandableListView) parentView.findViewById(R.id.lvExp);
        // get the listItem
        listItem = (TextView) parentView.findViewById(R.id.lblListItem);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // Tell us what we clicked

                if (CRUDFlinger.getAreas() == null) {
                     // need to be taken to login activity
                    Intent goToLogin = new Intent(getActivity().getBaseContext(), Login.class);
                    startActivity(goToLogin);

                } else {
                    Toast.makeText(
                            parentView.getContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();


                    // Set up to change the fragment with a specific method called
                    // pass a bundle containing the household to the fragment
                    MemberFragment goToHouse = new MemberFragment();
                    Bundle args = new Bundle();
                    args.putInt("Area Index",groupPosition);
                    CRUDFlinger.save("area", groupPosition);
                    args.putInt("House Index", childPosition);
                    CRUDFlinger.save("house", childPosition);
                    Log.i("Position :",childPosition + "");
                    goToHouse.setArguments(args);

                    // Change the fragment
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToHouse, "household")
                            .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null).commit();
                }
                return false;
            }
        });
        // Get Households

        // preparing list data
        try{
            prepareListData();
        } catch (JSONException e){

        }

        //System.out.println("After Preparing..."+listDataChild.get("Juja"));
        listAdapter = new ExpandableListAdapter(parentView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        //setUpViews();
        return parentView;
    }



    private void prepareListData() throws JSONException{
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> area0 = new ArrayList<>();
        List<Household> households = new ArrayList<>();
        ArrayList<Area> areas = new ArrayList<>();

        // Get areas
        try {
            areas = CRUDFlinger.getAreas();
        } catch (Exception e) {
            return;
        }

        if (areas.isEmpty() || areas == null ){
            listDataHeader.add("Hello!");
            area0.add("You need to sync or add areas");
            listDataChild.put(listDataHeader.get(0), area0);

        } else {

            // Add categories
            for (Area a : areas) {
                // add area name to the list of headers for the adapter
                listDataHeader.add(a.getName());
                //Get the households in area
                households = a.getResources();
                //Log.i("Dashboard ", households);
                ArrayList<String> houseNames = new ArrayList<String>();
                //For each household in the list of households
                for (Household h : households) {
                    Log.i("Household ID : ", h.getHouseholdID());
                    //for each household add their name to the list
                    houseNames.add(h.getName());
                }
                //put the list of names with the area
                listDataChild.put(a.getName(), houseNames);
                //System.out.println("listDataChild ="+a.getName()+" "+houseNames.get(0));

            }

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
