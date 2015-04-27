package org.rbdc.sra.helperClasses;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.openbeans.BeanInfo;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import org.json.JSONArray;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.CountryLogin;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.LoginObject;
import org.rbdc.sra.objects.Note;
import org.rbdc.sra.objects.QuestionSet;
import org.rbdc.sra.objects.Region;
import org.rbdc.sra.objects.RegionLogin;

import java.io.Serializable;
import java.util.ArrayList;

import quickconnectfamily.json.JSONException;
import quickconnectfamily.json.JSONUtilities;

public class CRUDFlinger {

    private static CRUDFlinger instance = null;
    private static SharedPreferences loader = null;
    private static SharedPreferences.Editor saver = null;
    private static Region region = null;
    private static Application application = null;
    private static Region tempRegion = null;
    private static ArrayList<QuestionSet> tempQuestionSets = new ArrayList<>();
    private static LoginObject user;

    protected CRUDFlinger(){

    }

    public static LoginObject getUser() {
        if(user == null){
            CRUDFlinger.setUser();
        }
        return user;
    }

    public static void setUser(){
        CRUDFlinger.user = CRUDFlinger.load("User",LoginObject.class);
    }
    public static void setUser(LoginObject login){
        CRUDFlinger.user = login;
    }


    public static CRUDFlinger getInstance() {

        if(instance == null) {
            CRUDFlinger.instance = new CRUDFlinger();
            CRUDFlinger.setPassword("test");
            CRUDFlinger.setUserName("test");
        }
        return instance;
    }

    public static Region getTempRegion() {
        if(tempRegion == null){
            tempRegion = new Region();
        }
        return tempRegion;
    }

    public static void setTempRegion(Region tempRegion) {
        CRUDFlinger.tempRegion = tempRegion;
    }

    public static void setApplication(Application applicationPassed){
        CRUDFlinger.application = applicationPassed;
    }

    public static void setPreferences(){
        if(application == null){

        }
        if (loader == null || saver == null) {
            System.out.println("hello crud" + application);
            CRUDFlinger.loader = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE);
            CRUDFlinger.saver = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE).edit();

        }

    }


    public static void save(String key,Serializable serializable){
        setPreferences();
        try{
            saver.putString(key,JSONUtilities.stringify(serializable));
            saver.commit();
        }catch (JSONException e){
            //
        }
    }

    public static void save(String key,int integer){
        setPreferences();
        saver.putInt(key,integer);
        saver.commit();

    }

    public static <Any> Any load(String key,Class className){
        //setPreferences();
        String json = loader.getString(key,null);
        Gson gson = new GsonBuilder().create();
        Object object = gson.fromJson(json,className);

        return (Any)object;
    }

    public static int load(String key){
        return Integer.valueOf(loader.getInt(key,0));
    }



    public static void commit(){
        setPreferences();
        saver.commit();
    }

    public static void clearCRUD(){
        setPreferences();
        saver.clear().commit();
    }

    private static void loadRegion(){
        setPreferences();
        if(loader.contains("Country")){
            String json = loader.getString("Country",null);
            Gson gson = new GsonBuilder().create();
            Log.i("JSON : ",json);
            Region region = gson.fromJson(json,Region.class);
            CRUDFlinger.region = region;
        } else{
            region = new Region();
        }

    }

    public static void saveRegion(){
        setPreferences();
        if(region == null){
            loadRegion();
        }else{
            try{
                saver.putString("Country",JSONUtilities.stringify(region));
                boolean a = saver.commit();
                Log.i("Region Contents: ",JSONUtilities.stringify(region));
                Log.i("Region saved: ",a + "");
            }catch (JSONException e){
                //
            }
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

    public static Region getRegion(){
        if(region == null){
            loadRegion();
        }
        return region;
    }

    public static void addArea(Area area){
        if(region == null){
            region = new Region();
            saveRegion();
        }
        region.addArea(area);
    }

    public static void addHousehold(int area,Household households){
        getAreas().get(area).addHousehold(households);
    }

    public static void removeLocal(String key){
        setPreferences();
        saver.remove(key);
        saver.commit();
    }

    public static <Any> Any merge(Any target, Any destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

            // Only copy writable attributes
            if (descriptor.getWriteMethod() != null) {
                Object originalValue = descriptor.getReadMethod()
                        .invoke(target);

                // Only copy values values where the destination values is null
                if (originalValue == null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(
                            destination);
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }

            }
        }
        return (Any)destination;
    }


    /************************* Question set bank stuff ***********************/

    private static ArrayList<QuestionSet> questionSets = null;

    public static ArrayList<QuestionSet> getQuestionSets() {
        if (questionSets == null) { loadQuestionSets(); }
        return questionSets;
    }
    public static ArrayList<QuestionSet> getQuestionSets(String type) {
        if (questionSets == null) { loadQuestionSets(); }
        ArrayList<QuestionSet> sets = new ArrayList<QuestionSet>();
        for (QuestionSet qs : questionSets) {
            if (qs.getType().equals(type)) sets.add(qs);
        }
        return sets;
    }
    public static QuestionSet getQuestionSet(int index) {
        if (questionSets == null) {loadQuestionSets();}
        return questionSets.get(index);
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
                    //
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
    public static void clearQuestionSets() {
        setPreferences();
        questionSets = null;
        saveQuestionSets();
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

    public static void addTempQuestionSet(QuestionSet qs) {
        setPreferences();
        tempQuestionSets.add(qs);
    }

    public static ArrayList<QuestionSet> getTempQuestionSets() {

        return tempQuestionSets;
    }
    /************************ END QUESTION SET STUFF *******************************/



    public static ArrayList<Area> getAreas(){
        if(region == null){
            loadRegion();
        }
        return region.getAreas();
    }

    public static ArrayList<Area>getTempAreas(){
        if(tempRegion == null){
            setTempRegion(new Region());
        }
        return tempRegion.getAreas();
    }


    public static void setRegion(Region region) {
        CRUDFlinger.region = region;
    }


    public static String getCountryName(String regionName){
        LoginObject login = CRUDFlinger.load("User",LoginObject.class);
        String countryName = new String();
        for(CountryLogin country : login.getSiteLogin().getCountries()){
            for(RegionLogin region : country.getRegions()){
                if(region.getName().matches(regionName)){
                    countryName = country.getName();
                }
            }
        }
        return countryName;
    }

    /******************************* Notes Stuff *********************************/

    private static ArrayList<Note> notes = null;
    private static ArrayList<Note> tempNotes = new ArrayList<Note>();

    // Get Notes
    public static ArrayList<Note> getNotes() {
        if (notes == null) { loadNotes(); }
        return notes;
    }

    // Get Single Note
    public static Note getNote(int pos) {
        if (notes == null) { loadNotes();}

        return notes.get(pos);
    }

    public static void removeNotes(){
        setPreferences();
        notes = null;
        saveNotes();
    }

    // Save Notes
    public static void saveNotes() {
        setPreferences();
        if (notes == null) {
            loadNotes();
            return;
        }
        try {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(notes);
            saver.putString("NotesBank", json);
            saver.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: Couldn't store Notes bank using KVStore.");
        }
    }

    // Add Note
    public static void addNote(Note added_note) {
        setPreferences();
        if (notes == null) { loadNotes(); }
        notes.add(added_note);
        saveNotes();
    }

    public static void addTempNote(Note added_note){
        setPreferences();
        tempNotes.add(added_note);
    }

    public static ArrayList<Note> getTempNotes() {return tempNotes;}

    // Delete Note
    public static void deleteNote(Note deleted_note) {
        setPreferences();
        if (notes == null) { loadNotes(); }
        notes.remove(deleted_note);
        saveNotes();
    }

    // Load Notes
    private static void loadNotes() {
        setPreferences();
        notes = new ArrayList<Note>();
        JSONArray json_notes = null;
        String loadedJSON = null;
        try {
            loadedJSON = loader.getString("NotesBank", null);
            if (loadedJSON != null) {
                json_notes = new JSONArray(loadedJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json_notes == null) {
            saveNotes();
            return;
        }
        for (int i = 0; i < json_notes.length(); i++) {
            try {
                try {
                    String notesJSON = json_notes.getString(i);
                    Gson gson = new GsonBuilder().create();
                    Note aNote = gson.fromJson(notesJSON, Note.class);
                    notes.add(aNote);
                } catch (Exception e) {
                    //
                }
            } catch (NullPointerException e){
                System.out.println("Nothing Here");
            }
        }

    }

    /***************************** End Notes Stuff ************************************/

    public static String getUserName() {
        setPreferences();
        String username = loader.getString("username", "empty");
        return username;
    }

    public static void setUserName(String uName) {
        setPreferences();
        saver.putString("username", uName);
        saver.commit();
    }

    public static String getPassword() {
        setPreferences();
        String password = loader.getString("password", "empty");
        return password;
    }

    public static void setPassword(String pWord) {
        setPreferences();
        saver.putString("password", pWord);
        saver.commit();
    }




}
