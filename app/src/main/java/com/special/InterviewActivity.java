package com.special;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.special.menu.ResideMenu;
import com.special.utils.UISwipableList;

import org.rbdc.sra.R;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;

public class InterviewActivity extends ActionBarActivity {
    private UISwipableList questionSetListView;
//    private QuestionSetListAdapter questionSetListAdapter;
    private ResideMenu resideMenu;

    private ArrayList<QuestionSet> questionSets;
    private ArrayList<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
