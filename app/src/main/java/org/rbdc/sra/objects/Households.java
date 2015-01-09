package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jakobhartman on 11/15/14.
 */
public class Households implements Serializable {
    private String householdName;
    private long id;
    private String ref;
    private ArrayList<Interviews> interviews;
    private ArrayList<Member> members;

    public Households(){
        householdName = "";
        id = 0;
        interviews = new ArrayList<Interviews>();
        members = new ArrayList<Member>();
    }

    public void addMember(Member name){
        members.add(name);
    }

    public void addInterview(Interviews interview){
        interviews.add(interview);
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setInterviews(ArrayList<Interviews> interviews) {
        this.interviews = interviews;
    }

    public ArrayList<Interviews> getInterviews() {
        return interviews;
    }

    public void addQuestionSet(QuestionSet qs) {
        if (interviews.isEmpty()) {
            interviews.add(new Interviews());
        }
        Interviews i = interviews.get(0);
        i.addQuestionSets(qs);
    }

    public QuestionSet getQuestionSet(String name) {
        if (interviews.isEmpty()) return null;
        Interviews i = interviews.get(0);
        ArrayList<QuestionSet> sets = i.getQuestionSets();
        for (QuestionSet qs : sets) {
            if (qs.getName().equals(name))
                return qs;
        }
        return null;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}

