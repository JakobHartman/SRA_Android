package org.rbdc.sra;

import com.special.AreasFragment;
import com.special.DashboardFragment;
import com.special.LogoutFragment;
import com.special.NotesFragment;
import com.special.QuestionsFragment;
import com.special.StatsFragment;
import com.special.SyncFragment;
import com.special.menu.ResideMenu;
import com.special.menu.ResideMenuItem;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.UrlBuilder;

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
        setContentView(R.layout.activity_dashboard);
        UrlBuilder.setOrg("sra");
        setUpMenu();
        changeFragment(new DashboardFragment(), "dashboard");
        title = (TextView)findViewById(R.id.title);
        title.setText("Dashboard");
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
        itemAreas = new ResideMenuItem(this, R.drawable.ic_map,  "Areas");
        itemQuestions = new ResideMenuItem(this, R.drawable.ic_question, "Question Sets");
        itemNotes = new ResideMenuItem(this, R.drawable.ic_notes, "Notes");
        itemStats = new ResideMenuItem(this,R.drawable.ic_stats,"Stats");
        itemSync = new ResideMenuItem(this,R.drawable.ic_sync,"Sync");
        itemLogout = new ResideMenuItem(this,R.drawable.ic_logout,"Logout");

        itemDashboard.setOnClickListener(this);
        itemAreas.setOnClickListener(this);
        itemQuestions.setOnClickListener(this);
        itemNotes.setOnClickListener(this);
        itemStats.setOnClickListener(this);
        itemSync.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemDashboard);
        resideMenu.addMenuItem(itemAreas);
        resideMenu.addMenuItem(itemQuestions);
        resideMenu.addMenuItem(itemNotes);
        resideMenu.addMenuItem(itemStats);
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
            title.setText("Areas");
            name = "areas";
        }else if (view == itemQuestions){
            changeFragment(new QuestionsFragment(), "questions");
            title.setText("Question Sets");
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
        AreasFragment areaFrag = (AreasFragment)getSupportFragmentManager().findFragmentByTag("areas");

        if (resideMenu.isOpened()){
            resideMenu.closeMenu();

        }  else if (areaFrag != null && areaFrag.isVisible()){
            //Toast.makeText(getBaseContext(),"You are in the area frag",Toast.LENGTH_SHORT).show();
            System.out.println("You are in the area frag");
            if (areaFrag.navigation == "area") {
                resideMenu.openMenu();
                System.out.println("You are in the areas navigation");
            } else if (areaFrag.navigation == "household") {
                areaFrag.navigation = "area";
                areaFrag.title.setText("Areas");
                areaFrag.initView();
                System.out.println("You are in the household navigation");
            } else if (areaFrag.navigation == "members") {
                areaFrag.navigation = "household";
                areaFrag.title.setText("Households");
                areaFrag.initView();
                System.out.println("You are in the members navigation");
            }
        } else if (!resideMenu.isOpened()) {
            resideMenu.openMenu();
        }
    }
}
