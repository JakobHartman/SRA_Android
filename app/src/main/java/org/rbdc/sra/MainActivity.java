package org.rbdc.sra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.helperClasses.DeleteRecord;
import org.rbdc.sra.helperClasses.DownloadData;
import org.rbdc.sra.objects.LoginObject;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CRUDFlinger.setApplication(getApplication());
        CRUDFlinger.setPreferences();
        DeleteRecord.initData();
        DownloadData.setOrganization("sra");

        if(CRUDFlinger.checkLocal("User")){
            LoginObject login = CRUDFlinger.load("User",LoginObject.class);
            try{
                if(login.isLoggedIn()){
                    goToDashboard();
                }
            } catch (NullPointerException e){


            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);

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

    public void goToLogin(View v) { goToLogin(); }

    public void goToLogin(){
        Intent intent0 = new Intent(this, Login.class);
        startActivity(intent0);
    }

    private void goToDashboard(View v) { goToDashboard(); }

    public void goToDashboard(){
        Intent intent0 = new Intent(this, Dashboard.class);
        startActivity(intent0);
    }
}
