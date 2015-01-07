package org.rbdc.sra;

<<<<<<< Updated upstream

import com.special.AreasFragment;
import com.special.DashboardFragment;
import com.special.LogoutFragment;
import com.special.NotesFragment;
import com.special.QuestionsFragment;
import com.special.StatsFragment;
import com.special.menu.ResideMenu;
import com.special.menu.ResideMenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.rbdc.sra.helperClasses.CRUDFlinger;

public class Dashboard extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemDashboard;
    private ResideMenuItem itemAreas;
    private ResideMenuItem itemQuestions;
    private ResideMenuItem itemNotes;
    private ResideMenuItem itemStats;
    private ResideMenuItem itemLogout;
    private TextView title;
=======
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.special.ElementsFragment;
import com.special.HomeFragment;
import com.special.ListFragment;
import com.special.TransitionListFragment;
import com.special.menu.ResideMenu;
import com.special.menu.ResideMenuItem;


public class Dashboard extends FragmentActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemElements;
    private ResideMenuItem itemList1;
    private ResideMenuItem itemList2;
>>>>>>> Stashed changes

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpMenu();
        changeFragment(new DashboardFragment());
        title = (TextView)findViewById(R.id.title);
        title.setText("Dashboard");
    }

<<<<<<< Updated upstream
    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.grass_cut);

        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(true);
        resideMenu.setHeaderView(findViewById(R.id.actionbar));
        resideMenu.setMenuListener(menuListener);

        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);
=======
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() { }

        @Override
        public void closeMenu() { }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemElements){
            changeFragment(new ElementsFragment());
        }else if (view == itemList1){
            changeFragment(new ListFragment());
        }else if (view == itemList2){
            changeFragment(new TransitionListFragment());
        }

        resideMenu.closeMenu();
    }

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(com.special.R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void setupMenu(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(com.special.R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(true);
        resideMenu.setHeaderView(findViewById(com.special.R.id.actionbar));
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        itemHome     = new ResideMenuItem(this, com.special.R.drawable.ic_home,     "Home");
        itemElements  = new ResideMenuItem(this, com.special.R.drawable.ic_elements_alternative,  "Elements");
        itemList1 = new ResideMenuItem(this, com.special.R.drawable.ic_list_2, "List 1");
        itemList2 = new ResideMenuItem(this, com.special.R.drawable.ic_list_1, "List 2");

        itemHome.setOnClickListener(this);
        itemElements.setOnClickListener(this);
        itemList1.setOnClickListener(this);
        itemList2.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome);
        resideMenu.addMenuItem(itemElements);
        resideMenu.addMenuItem(itemList1);
        resideMenu.addMenuItem(itemList2);

        findViewById(com.special.R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });
    }
>>>>>>> Stashed changes

        itemDashboard = new ResideMenuItem(this, R.drawable.ic_dash, "Dashboard");
        itemAreas = new ResideMenuItem(this, R.drawable.ic_map,  "Areas");
        itemQuestions = new ResideMenuItem(this, R.drawable.ic_question, "Question Sets");
        itemNotes = new ResideMenuItem(this, R.drawable.ic_notes, "Notes");
        itemStats = new ResideMenuItem(this,R.drawable.ic_stats,"Stats");
        itemLogout = new ResideMenuItem(this,R.drawable.ic_logout,"Logout");

        itemDashboard.setOnClickListener(this);
        itemAreas.setOnClickListener(this);
        itemQuestions.setOnClickListener(this);
        itemNotes.setOnClickListener(this);
        itemStats.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemDashboard);
        resideMenu.addMenuItem(itemAreas);
        resideMenu.addMenuItem(itemQuestions);
        resideMenu.addMenuItem(itemNotes);
        resideMenu.addMenuItem(itemStats);
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
            changeFragment(new DashboardFragment());
            title.setText("Dashboard");
        }else if (view == itemAreas){
            changeFragment(new AreasFragment());
            title.setText("Areas");
        }else if (view == itemQuestions){
            changeFragment(new QuestionsFragment());
            title.setText("Question Sets");
        }else if (view == itemNotes){
            changeFragment(new NotesFragment());
            title.setText("Notes");
        }else if(view == itemStats){
            changeFragment(new StatsFragment());
            title.setText("Statistics");
        }else if(view == itemLogout){
            changeFragment(new LogoutFragment());
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

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()){
            resideMenu.closeMenu();
        } else {
            resideMenu.openMenu();
        }
    }
}
