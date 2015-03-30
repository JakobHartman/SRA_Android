package com.special;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

import org.rbdc.sra.Dashboard;
import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.ImageData;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Note;

import java.util.ArrayList;
import java.util.List;


public class AreasFragment extends Fragment {

    //Views & Widgets
    private View parentView;
    private UISwipableList listView;
    public TransitionListAdapterArea mAdapter;
    private ResideMenu resideMenu;
    private Button button;
    Button btn, btnCancel;
    Dialog dialog;
    private Bundle args;
    public TextView title;
    private List<Note> notes_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_areas, container, false);

        notes_list = new ArrayList<>();

        title = (TextView) getActivity().findViewById(R.id.title);
        listView = (UISwipableList) parentView.findViewById(R.id.listView);
        button = (Button) parentView.findViewById(R.id.button3);
        Dashboard parentActivity = (Dashboard) getActivity();
        resideMenu = parentActivity.getResideMenu();

        // Lists Areas
        mAdapter = new TransitionListAdapterArea(getActivity(), listArea());
        mAdapter.notifyDataSetChanged();
        listView.setActionLayout(R.id.hidden);
        listView.setItemLayout(R.id.front_layout);
        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);


        button.setText("Add Community");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArea();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, final int i, long l) {
                HouseholdFragment goToHouse = new HouseholdFragment();
                Bundle args = new Bundle();
                args.putInt("Area Index",i);
                goToHouse.setArguments(args);

                // Change the fragment
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,goToHouse,"household fragment")
                        .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null).commit();

            }
        });

        return parentView;
    }

    /****************** add Area ***********************/

    private void addArea(){
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_area_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final EditText areaText = (EditText) dialog.findViewById(R.id.editText);
        final Spinner regionText = (Spinner) dialog.findViewById(R.id.spinner);
        final LoginObject loginObject = CRUDFlinger.load("User",LoginObject.class);
        ArrayList<String> regions = new ArrayList<>();
        regions.add("Select Region");
        regions.addAll(loginObject.getSiteLogin().getRegionNames());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_dropdown_item,regions);
        regionText.setAdapter(adapter);

        btn = (Button) dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getActivity());

                Area newArea = new Area();

                if(areaText.getText().toString().matches("")){
                    toast.makeText(getActivity(),"Please Enter A Valid Area Name", Toast.LENGTH_SHORT).show();
                }else if(regionText.getSelectedItemPosition() == 0){
                    toast.makeText(getActivity(),"Please Select A Valid Region", Toast.LENGTH_SHORT).show();
                }else{
                    newArea.setRegion(regionText.getSelectedItem().toString());
                    newArea.setCountry(CRUDFlinger.getCountryName(regionText.getSelectedItem().toString()));
                    newArea.setName(areaText.getText().toString());
                    CRUDFlinger.addArea(newArea);
                    mAdapter = new TransitionListAdapterArea(getActivity(),listArea());
                    listView.setAdapter(mAdapter);
                    dialog.cancel();
                    CRUDFlinger.saveRegion();
                }
            }
        });

        btnCancel = (Button) dialog.findViewById(R.id.btncancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        dialog.show();
    }

    /********************** list Area *********************************/

    private ArrayList<ListItem> listArea(){
        ArrayList<ListItem> listData = new ArrayList<>();
        for(Area area : CRUDFlinger.getAreas()){

            if (area.getImageCollection().size() > 0) {
                int last = area.getImageCollection().size() - 1;
                ImageData image = area.getImageCollection().get(last);
                String imageData = image.getImageData();
                Bitmap actImage = BitmapFactory.decodeByteArray(Base64.decode(imageData, Base64.DEFAULT), 0, Base64.decode(imageData, Base64.DEFAULT).length);
                listData.add(new ListItem(R.drawable.ic_like, area.getName(), area.getResources().size() + " Households", null, null,actImage));
            } else {
                listData.add(new ListItem(R.drawable.ic_like, area.getName(), area.getResources().size() + " Households", null, null,null));
            }
        }
        return listData;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("", "restarted");
        mAdapter = new TransitionListAdapterArea(getActivity(),listArea());
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


}
