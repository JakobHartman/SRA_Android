package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/17/14.
 */
public class Interviews implements Serializable {
    private String createdDate;
    private ArrayList<QuestionSet> questionSets;

    public void addQuestionSets(QuestionSet questionSet){
        questionSets.add(questionSet);
    }

    public Interviews(){
        this.questionSets = new ArrayList<QuestionSet>();
    }

    public ArrayList<QuestionSet> getQuestionSets() {
        return questionSets;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setQuestionSets(ArrayList<QuestionSet> questionSets) {
        this.questionSets = questionSets;
    }
}
