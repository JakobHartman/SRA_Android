package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jakobhartman on 11/15/14.
 */
public class Household implements Serializable {
    private String name;
    private ArrayList<Interview> interviews;
    private ArrayList<Member> members;
    private String region;
    private String country;
    private String area;
    private ArrayList<Nutrition> nutrition;
    private String HouseholdID;

    public String getHouseholdID() {
        return HouseholdID;
    }

    public void setHouseholdID() {
        HouseholdID = "" + System.currentTimeMillis();
    }

    public ArrayList<Nutrition> getNutrition() {
        return nutrition;
    }

    public Household(){
        interviews = new ArrayList<>();
        members = new ArrayList<>();
        nutrition = new ArrayList<>();
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getArea() {
        return area;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void addMember(Member name){
        members.add(name);
    }

    public void addInterview(Interview interview){
        interviews.add(interview);
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    public ArrayList<Interview> getInterviews() {
        return interviews;
    }

    public void addQuestionSet(QuestionSet qs) {
        if (interviews.isEmpty()) {
            interviews.add(new Interview());
        }
        Interview i = interviews.get(0);
        i.addQuestionSets(qs);
    }

    public QuestionSet getQuestionSet(String name) {
        if (interviews.isEmpty()) return null;
        Interview i = interviews.get(0);
        ArrayList<QuestionSet> sets = i.getQuestionsets();
        for (QuestionSet qs : sets) {
            if (qs.getName().equals(name))
                return qs;
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

