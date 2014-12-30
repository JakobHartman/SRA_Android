package org.rbdc.sra;


import com.special.AreasFragment;
import com.special.DashboardFragment;
import com.special.NotesFragment;
import com.special.QuestionsFragment;
import com.special.StatsFragment;
import com.special.menu.ResideMenu;
import com.special.menu.ResideMenuItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Dashboard extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemDashboard;
    private ResideMenuItem itemAreas;
    private ResideMenuItem itemQuestions;
    private ResideMenuItem itemNotes;
    private ResideMenuItem itemStats;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpMenu();
        changeFragment(new DashboardFragment());
        title = (TextView)findViewById(R.id.title);
        title.setText("Dashboard");
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.grass_cut);

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

        itemDashboard.setOnClickListener(this);
        itemAreas.setOnClickListener(this);
        itemQuestions.setOnClickListener(this);
        itemNotes.setOnClickListener(this);
        itemStats.setOnClickListener(this);

        resideMenu.addMenuItem(itemDashboard);
        resideMenu.addMenuItem(itemAreas);
        resideMenu.addMenuItem(itemQuestions);
        resideMenu.addMenuItem(itemNotes);
        resideMenu.addMenuItem(itemStats);

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
