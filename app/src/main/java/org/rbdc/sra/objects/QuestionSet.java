package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private String name;
    private String type;
    private ArrayList<Question> Questions;

    /*
     * Constructor
     */
    public QuestionSet(String name, String url) {
        this.name = name;
        this.type = "Household";
        this.Questions = new ArrayList<Question>();
    }

    /*
     * Modifiers
     */
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void addQuestion(Question qs) { Questions.add(qs); }
    public void setQuestions(ArrayList<Question> questions) { this.Questions = questions; }

    /*
     * Accessors
     */
    public String getName() { return name; }
    public String getType() { return type; }
    public void deleteQuestion(Question qs) { Questions.remove(qs); }
    public ArrayList<Question> getQuestions() { return Questions; }
    public Question getQuestion(String questionName) {
        for (Question q : Questions) {
            if (q.getName().equals(questionName)) {
                return q;
            }
        }
        return null;
    }
}
