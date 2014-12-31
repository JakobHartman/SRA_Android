package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private String name;
    private String refUrl;
    private String type;
    private ArrayList<Question> questions;

    /*
     * Constructor
     */
    public QuestionSet(String name, String url) {
        this.name = name;
        this.refUrl = url;
        this.type = "Region";
        this.questions = new ArrayList<Question>();
    }

    /*
     * Modifiers
     */
    public void setName(String name) { this.name = name; }
    public void setRefUrl(String refUrl) { this.refUrl = refUrl; }
    public void setType(String type) { this.type = type; }
    public void addQuestion(Question qs) { questions.add(qs); }
    public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }

    /*
     * Accessors
     */
    public String getName() { return name; }
    public String getRefUrl() { return refUrl; }
    public String getType() { return type; }
    public void deleteQuestion(Question qs) { questions.remove(qs); }
    public ArrayList<Question> getQuestions() { return questions; }
    public Question getQuestion(String questionName) {
        for (Question q : questions) {
            if (q.getName().equals(questionName)) {
                return q;
            }
        }
        return null;
    }
}
