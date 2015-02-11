package org.rbdc.sra.helperClasses;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.AreaLogin;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

/**
 * Created by imac on 2/5/15.
 */
public class FlatTax {
    private static FlatTax instance = null;
    private static Activity activity = null;

    public static void setActivity(Activity activity) {
        FlatTax.activity = activity;
    }

    protected FlatTax() {
        // Exists only to defeat instantiation.
    }

    public static FlatTax getInstance() {
        if(instance == null) {
            instance = new FlatTax();
        }
        return instance;
    }


    public static Region downlaod(LoginObject info){
        Region region = new Region();
        Firebase base = new Firebase("https://testrbdc.firebaseio.com/organizations/sra/resources/");

        for (CountryLogin country : info.getSiteLogin().getCountries()){
            for(RegionLogin regions : country.getRegions()){
                for(AreaLogin area : regions.getAreas()){
                    String id = capitalizeFirstLetter(area.getName());
                    Query query = base.orderByChild("area").startAt(id).endAt(id);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Household household = child.getValue(Household.class);
                                Log.i("area",household.getArea());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            }
        }

        return region;
    }

    public static String capitalizeFirstLetter(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}
