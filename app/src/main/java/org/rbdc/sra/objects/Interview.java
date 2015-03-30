package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/17/14.
 */
public class Interview implements Serializable {
    private String dateCreated;
    private ArrayList<QuestionSet> questionsets;

    public void addQuestionSets(QuestionSet questionSet){
        questionsets.add(questionSet);
    }

    public void removeQuestionSet(int position) {questionsets.remove(position);}

    public Interview(){
        questionsets = new ArrayList<QuestionSet>();
    }


    public ArrayList<QuestionSet> getQuestionsets() {
        return questionsets;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setQuestionsets(ArrayList<QuestionSet> questionsets) {
        this.questionsets = questionsets;
    }
}
