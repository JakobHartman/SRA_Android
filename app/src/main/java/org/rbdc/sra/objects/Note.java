package org.rbdc.sra.objects;

import org.rbdc.sra.helperClasses.CRUDFlinger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chad on 2/3/15.
 */
public class Note implements Serializable {
    public String noteType;
    private String dateUpdated;
    private String dateCreated;
    private String noteTitle;
    private String noteContents;
    private String author;
    private String areaName;
    private String householdID;
    private String noteID;

    public Note(String noteType, String title, String noteContents, String area) {
        this.noteType = noteType;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        this.dateUpdated = sdf.format(Calendar.getInstance().getTime());
        this.dateCreated = sdf.format(Calendar.getInstance().getTime());
        this.noteTitle = title;
        this.noteContents = noteContents;
        // Need to add author to CRUDflinger so we can store it here
        // Need a temporary author in order for it do be stored in Firebase
        this.author = CRUDFlinger.getUser().getUsername();
        this.householdID = " ";
        this.areaName = area;
        this.noteID = "id_" + Calendar.getInstance().getTimeInMillis();
    }

    public Note(String noteType, String title, String noteContents, String area, String householdId) {
        this.noteType = noteType;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        this.dateUpdated = sdf.format(Calendar.getInstance().getTime());
        this.dateCreated = sdf.format(Calendar.getInstance().getTime());

        this.noteTitle = title;
        this.noteContents = noteContents;
        this.author = CRUDFlinger.getUser().getUsername();

        this.areaName = area;
        this.householdID = householdId;
        this.noteID = "id_" + Calendar.getInstance().getTimeInMillis();
    }

    public Note() {

    }

    public void editNoteContents(String contents) {
        this.noteContents = contents;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        this.dateUpdated = sdf.format(Calendar.getInstance().getTime());
    }

    public void editNoteTitle(String title) {
        this.noteTitle = title;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        this.dateUpdated = sdf.format(Calendar.getInstance().getTime());
    }

    public String getNoteContents() {
        return noteContents;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getNoteID() {
        return noteID;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getHouseholdID() {
        return householdID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }
}

