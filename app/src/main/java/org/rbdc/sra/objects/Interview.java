package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/17/14.
 */
public class Interview implements Serializable {
    private String DateCreated;
    private ArrayList<QuestionSet> QuestionSets;

    public void addQuestionSets(QuestionSet questionSet){
        QuestionSets.add(questionSet);
    }

    public Interview(){
        this.QuestionSets = new ArrayList<QuestionSet>();
    }

    public ArrayList<QuestionSet> getQuestionSets() {
        return QuestionSets;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.DateCreated = dateCreated;
    }

    public void setQuestionSets(ArrayList<QuestionSet> questionSets) {
        this.QuestionSets = questionSets;
    }
}
