package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jakobhartman on 11/15/14.
 */
public class Household implements Serializable {
    private String Name;
    private ArrayList<Interview> Interviews;
    private ArrayList<Member> Members;
    private String Region;
    private String Country;
    private String Area;

    public Household(){
        Interviews = new ArrayList<Interview>();
        Members = new ArrayList<Member>();
    }

    public String getCountry() {
        return Country;
    }

    public String getRegion() {
        return Region;
    }

    public String getArea() {
        return Area;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public void setArea(String area) {
        Area = area;
    }

    public void addMember(Member name){
        Members.add(name);
    }

    public void addInterview(Interview interview){
        Interviews.add(interview);
    }

    public void setMembers(ArrayList<Member> members) {
        this.Members = members;
    }

    public ArrayList<Member> getMembers() {
        return Members;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.Interviews = interviews;
    }

    public ArrayList<Interview> getInterviews() {
        return Interviews;
    }

    public void addQuestionSet(QuestionSet qs) {
        if (Interviews.isEmpty()) {
            Interviews.add(new Interview());
        }
        Interview i = Interviews.get(0);
        i.addQuestionSets(qs);
    }

    public QuestionSet getQuestionSet(String name) {
        if (Interviews.isEmpty()) return null;
        Interview i = Interviews.get(0);
        ArrayList<QuestionSet> sets = i.getQuestionsets();
        for (QuestionSet qs : sets) {
            if (qs.getName().equals(name))
                return qs;
        }
        return null;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }
}

