package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/17/14.
 */
public class Interview implements Serializable {
    private String DateCreated;
    private ArrayList<QuestionSet> questionsets;

    public void addQuestionSets(QuestionSet questionSet){
        questionsets.add(questionSet);
    }

    public Interview(){
        questionsets = new ArrayList<QuestionSet>();
    }


    public ArrayList<QuestionSet> getQuestionsets() {
        return questionsets;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.DateCreated = dateCreated;
    }

    public void setQuestionsets(ArrayList<QuestionSet> questionsets) {
        this.questionsets = questionsets;
    }
}
