package org.rbdc.sra;

import com.firebase.client.Firebase;
import com.special.AreasFragment;
import com.special.DashboardFragment;
import com.special.DrillDownNotes;
import com.special.EditQuestionSetFrag;
import com.special.HouseholdFragment;
import com.special.LogoutFragment;
import com.special.MemberFragment;
import com.special.NotesFragment;
import com.special.QuestionsFragment;
import com.special.StatsFragment;
import com.special.SyncFragment;

import com.special.menu.ResideMenu;
import com.special.menu.ResideMenuItem;
import com.special.utils.EditQuestionFrag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.UrlBuilder;
import org.rbdc.sra.objects.ImageData;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dashboard extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemDashboard;
    private ResideMenuItem itemAreas;
    private ResideMenuItem itemQuestions;
    private ResideMenuItem itemNotes;
    private ResideMenuItem itemStats;
    private ResideMenuItem itemLogout;
    private ResideMenuItem itemSync;
    private String name;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("User: " + CRUDFlinger.getUserName());
        System.out.println("Logged In: " + CRUDFlinger.getLoggedIn());
        setContentView(R.layout.activity_dashboard);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        UrlBuilder.setOrg("sra");
        setUpMenu();
        changeFragment(new DashboardFragment(), "dashboard");
        title = (TextView)findViewById(R.id.title);
        title.setText("Dashboard");
        Firebase.setAndroidContext(getApplicationContext());

    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.theme_color);


        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(true);
        resideMenu.setHeaderView(findViewById(R.id.actionbar));
        resideMenu.setMenuListener(menuListener);

        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);


        itemDashboard = new ResideMenuItem(this, R.drawable.ic_dash, "Dashboard");
        itemAreas = new ResideMenuItem(this, R.drawable.ic_map,  "Communities");
        itemQuestions = new ResideMenuItem(this, R.drawable.ic_question, "Surveys");
        itemNotes = new ResideMenuItem(this, R.drawable.ic_notes, "Notes");
        // hide stats
        //itemStats = new ResideMenuItem(this,R.drawable.ic_stats,"Stats");
        itemSync = new ResideMenuItem(this,R.drawable.ic_sync,"Sync");
        itemLogout = new ResideMenuItem(this,R.drawable.ic_logout,"Logout");

        itemDashboard.setOnClickListener(this);
        itemAreas.setOnClickListener(this);
        itemQuestions.setOnClickListener(this);
        itemNotes.setOnClickListener(this);
        //hide stats
        //itemStats.setOnClickListener(this);
        itemSync.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemDashboard);
        resideMenu.addMenuItem(itemAreas);
        resideMenu.addMenuItem(itemQuestions);
        resideMenu.addMenuItem(itemNotes);
        //hide stats
        //resideMenu.addMenuItem(itemStats);
        resideMenu.addMenuItem(itemSync);
        resideMenu.addMenuItem(itemLogout);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemDashboard){
            changeFragment(new DashboardFragment(), "dashboard");
            title.setText("Dashboard");
            name = "dashboard";
        }else if (view == itemAreas){
            changeFragment(new AreasFragment(), "areas");
            title.setText("Communities");
            name = "areas";
        }else if (view == itemQuestions){
            changeFragment(new QuestionsFragment(), "questions");
            title.setText("Surveys");
        }else if (view == itemNotes){
            changeFragment(new NotesFragment(), "notes");
            title.setText("Notes");
        }else if(view == itemStats){
            changeFragment(new StatsFragment(), "stats");
            title.setText("Statistics");
        }else if(view == itemLogout){
            changeFragment(new LogoutFragment(), "logout");
        }else if(view == itemSync){
            changeFragment(new SyncFragment(), "sync");
            title.setText("Sync");
        }

        resideMenu.closeMenu();
    }

    //Example of menuListener
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() { }

        @Override
        public void closeMenu() { }
    };

    private void changeFragment(Fragment targetFragment, String tag){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, tag)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    @Override
    public void onBackPressed() {
        EditQuestionSetFrag editQuestionSetFrag = (EditQuestionSetFrag)getSupportFragmentManager().findFragmentByTag("Edit Question Set");
        EditQuestionFrag editQuestionFrag = (EditQuestionFrag)getSupportFragmentManager().findFragmentByTag("Edit Question");
        HouseholdFragment householdFragment = (HouseholdFragment) getSupportFragmentManager().findFragmentByTag("household fragment");
        MemberFragment memberFragment = (MemberFragment) getSupportFragmentManager().findFragmentByTag("member fragment");
        DrillDownNotes drillDownNotes = (DrillDownNotes) getSupportFragmentManager().findFragmentByTag("drilldown notes fragment");

        if (resideMenu.isOpened()){
            resideMenu.closeMenu();

        } else if (householdFragment != null && householdFragment.isVisible()){
            householdFragment.getFragmentManager().popBackStackImmediate();

        } else if (memberFragment != null && memberFragment.isVisible()) {
            memberFragment.getFragmentManager().popBackStackImmediate();

        } else if (drillDownNotes != null && drillDownNotes.isVisible()) {
            drillDownNotes.getFragmentManager().popBackStackImmediate();

        } else if (editQuestionSetFrag != null && editQuestionSetFrag.isVisible()){
            editQuestionSetFrag.getFragmentManager().popBackStackImmediate();
            System.out.println("You are in the edit questions frag");

        } else if (editQuestionFrag != null && editQuestionFrag.isVisible()) {
            editQuestionFrag.getFragmentManager().popBackStackImmediate();

        } else if (!resideMenu.isOpened()) {
            resideMenu.openMenu();
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageData image = new ImageData();
                Date date = new Date();
                image.setCreationDate(getDate(date.getTime(),"dd/MM/yyyy hh:mm:ss.SSS"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                image.setImageData(imageEncoded);
                int house = CRUDFlinger.load("house");
                int area = CRUDFlinger.load("area");
                int pos = CRUDFlinger.load("pos");
                int picType = CRUDFlinger.load("picType");
                if(picType == 0){
                    CRUDFlinger.getAreas().get(area).addImage(image);
                }else if (picType == 1){
                    CRUDFlinger.getAreas().get(area).getResources().get(house).addImage(image);
                } else {
                    CRUDFlinger.getAreas().get(area).getResources().get(house).getMembers().get(pos).addImage(image);
                }
                CRUDFlinger.saveRegion();
            }
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
