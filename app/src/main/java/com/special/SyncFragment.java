package com.special;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.special.menu.ResideMenu;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.SyncUpload;

/**
 * Created by imac on 1/14/15.
 */
public class SyncFragment extends Fragment {

    //Layouts
    private ResideMenu resideMenu;
    Button btn, btnCancel;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v =  (ScrollView) inflater.inflate(R.layout.fragment_stats, container, false);
            SyncUpload syncUp = new SyncUpload();
            syncUp.startUpload();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
