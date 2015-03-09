package org.rbdc.sra.objects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chad on 2/3/15.
 */
public class Note implements Serializable{
    public String noteType;
    private Calendar dateUpdated;
    private Calendar dateCreated;
    private String title;
    private String noteContents;
    private String author;
    private String area;
    private String householdID;
    private String noteID;

    public Note (String noteType, String title, String noteContents, String area) {
        this.noteType = noteType;
        this.dateUpdated = Calendar.getInstance();
        this.dateCreated = Calendar.getInstance();
        this.title = title;
        this.noteContents = noteContents;
        // Need to add author to CRUDflinger so we can store it here
        // Need a temporary author in order for it do be stored in Firebase
        this.author = "John Doe";
        this.householdID = " ";
        this.area = area;
        this.noteID = "id_" + dateCreated.getTimeInMillis();
    }

    public Note (String noteType, String title, String noteContents, String area, String householdId) {
        this.noteType = noteType;
        this.dateUpdated = Calendar.getInstance();
        this.dateCreated = Calendar.getInstance();
        this.title = title;
        this.noteContents = noteContents;
        // Need to add author to CRUDflinger so we can store it here
        // Need a temporary author in order for it do be stored in Firebase
        this.author = "John Doe";

        this.area = area;
        this.householdID = householdId;
        this.noteID = "id_" + dateCreated.getTimeInMillis();
    }

    public void editNoteContents(String contents) {
        this.noteContents = contents;
        this.dateUpdated = Calendar.getInstance();
    }

    public void editNoteTitle(String title) {
        this.title = title;
        this.dateUpdated = Calendar.getInstance();
    }

    public String getNoteContents () { return noteContents;}

    public String getNoteTitle() {return title;}

    public String getAuthor() {return author;}

    public String getNoteID() {return noteID;}

    public String getAreaName() {return area;}

    public String getHouseholdID() {return householdID;}

    public Calendar getDateCreated() {return dateCreated;}


    public String getDateUpdated() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(this.dateUpdated.getTime());}
}
