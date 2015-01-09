package org.rbdc.sra.helperClasses;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.rbdc.sra.objects.Areas;
import org.rbdc.sra.objects.Country;
import org.rbdc.sra.objects.Households;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;

import org.json.JSONArray;
import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/17/14.
 */
public class CRUDFlinger {

    private static CRUDFlinger instance = null;
    private static SharedPreferences loader = null;
    private static SharedPreferences.Editor saver = null;
    private static Country country = null;
    private static Application application = null;

    protected CRUDFlinger(){

    }

    public static CRUDFlinger getInstance() {

        if(instance == null) {
            CRUDFlinger.instance = new CRUDFlinger();
        }
        return instance;
    }

    public static void setApplication(Application applicationPassed){
        CRUDFlinger.application = applicationPassed;
    }

    public static void setPreferences(){
        if(application == null){
            throw new NullPointerException();
        }
        else if (loader == null || saver == null) {
            CRUDFlinger.loader = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE);
            CRUDFlinger.saver = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE).edit();
        }

    }


    public static void save(String key,Serializable serializable){
        setPreferences();
        try{
            saver.putString(key,JSONUtilities.stringify(serializable));
            saver.commit();
        }catch (JSONException e){}
    }

    public static <Any> Any load(String key,Class className){
        setPreferences();
        String json = loader.getString(key,null);
        Gson gson = new GsonBuilder().create();
        Object object = gson.fromJson(json,className);

        return (Any)object;
    }

    private static void loadCountry(){
        setPreferences();
        String json = loader.getString("Country",null);
        Gson gson = new GsonBuilder().create();
        Country country = gson.fromJson(json,Country.class);
        CRUDFlinger.country = country;
    }

    public static void saveCountry(){
        setPreferences();
        if(country == null){
            loadCountry();
            return;
        }else{
            try{
                saver.putString("Country",JSONUtilities.stringify(country));
                saver.commit();
            }catch (JSONException e){}
        }
   }

    public static boolean checkLocal(String key){
        boolean check;
        check = true;
        try{
            loader.contains(key);
        }catch (NullPointerException e){
            check = false;
        }
        return check;
    }

    public static Country getCountry(){
        if(country == null){
            loadCountry();
        }
        return country;
    }

    public static void addArea(Areas area){
        int i = 0;
        for(Region regions : country.getRegions()){
            System.out.println(regions.getRegionName() + " " + area.getRegion());
            if(regions.getRegionName().equals(area.getRegion())){
                country.getRegions().get(i).addArea(area);
                System.out.println(country.getRegions().get(i).getRegionName());
            }
            i++;
        }

    }

    public static void addHousehold(int area,Households households){
        getAreas().get(area).addHousehold(households);
    }

    public static void removeLocal(String key){
        setPreferences();
        saver.remove(key);
    }




    /*
     * Question set bank stuff.
     */
    private static ArrayList<QuestionSet> questionSets = null;
    public static ArrayList<QuestionSet> getQuestionSets() {
        if (questionSets == null) { loadQuestionSets(); }
        return questionSets;
    }
    private static void loadQuestionSets() {
        setPreferences();
        questionSets = new ArrayList<QuestionSet>();
        JSONArray sets = null;
        String loadedJSON = null;
        try {
            loadedJSON = loader.getString("QuestionSetBank", null);
            if (loadedJSON != null) {
                sets = new JSONArray(loadedJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sets == null) {
            saveQuestionSets();
            return;
        }

        for (int i = 0; i < sets.length(); i++) {
            try {
                try {
                    String questionSetJSON = sets.getString(i);
                    Gson gson = new GsonBuilder().create();
                    QuestionSet set = gson.fromJson(questionSetJSON, QuestionSet.class);
                    questionSets.add(set);
                } catch (Exception e) {

                }
            } catch (NullPointerException e){
                System.out.println("Nothing Here");
            }
        }
    }

    public static void saveQuestionSets() {
        setPreferences();
        if (questionSets == null) {
            loadQuestionSets();
            return;
        }
        try {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(questionSets);
            saver.putString("QuestionSetBank", json);
            saver.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: Couldn't store QuestionSet bank using KVStore.");
        }
    }

    public static QuestionSet getQuestionSet(String name) {
        setPreferences();
        if (questionSets == null) { loadQuestionSets(); }
        for (QuestionSet set : questionSets) {
            if (set.getName().equals(name)) {
                return set;
            }
        }
        return null;
    }

    public static void deleteQuestionSet(QuestionSet qs) {
        setPreferences();
        if (questionSets == null) { loadQuestionSets(); }
        questionSets.remove(qs);
        saveQuestionSets();
    }

    public static void addQuestionSet(QuestionSet qs) {
        setPreferences();
        if (questionSets == null) { loadQuestionSets(); }
        questionSets.add(qs);
        saveQuestionSets();
    }

    public static ArrayList<Areas> getAreas(){
        ArrayList<Areas> areas = new ArrayList<Areas>();
        for(Region region : CRUDFlinger.getCountry().getRegions()){
            areas.addAll(region.getAreas());
        }
        return areas;
    }
}
