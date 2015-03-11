package org.rbdc.sra.helperClasses;

import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Member;
import org.rbdc.sra.objects.Note;
import org.rbdc.sra.objects.Region;

import java.util.ArrayList;

/**
 * Created by jakobhartman on 1/27/15.
 */
public class DeleteRecord {
    private static DeleteRecord instance = null;
    private static ArrayList<Area> areas = null;
    private static ArrayList<Household> households = null;
    private static ArrayList<Member> members = null;
    private static ArrayList<Note> notes = null;

    public static void initData(){
        areas = new ArrayList<Area>();
        households = new ArrayList<Household>();
        members = new ArrayList<Member>();
        notes = new ArrayList<Note>();
    }

    public static void addArea(Area area){
        areas.add(area);
    }

    public static void addHousehold(Household household){
        households.add(household);
    }

    public static void addMember(Member member){
        members.add(member);
    }

    public static void addNote(Note note) {
        notes.add(note);
    }

    public static ArrayList<Area> getAreas() {
        if(areas == null){
            initData();
        }
        return areas;
    }

    public static ArrayList<Household> getHouseholds() {
        return households;
    }

    public static ArrayList<Member> getMembers() {
        return members;
    }

    public static ArrayList<Note> getNotes() { return notes;}

    protected DeleteRecord() {
        // Exists only to defeat instantiation.
    }

    public static DeleteRecord getInstance() {
        if(instance == null) {
            instance = new DeleteRecord();
        }
        return instance;
    }


}
