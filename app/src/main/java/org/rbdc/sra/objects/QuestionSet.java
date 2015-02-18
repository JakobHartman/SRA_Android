package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private String name;
    private String type;
    private ArrayList<Question> questions;

    /*
     * Constructor
     */
    public QuestionSet(String name, String url, String type) {
        this.name = name;
        this.type = type;
        this.questions = new ArrayList<Question>();
    }

    public QuestionSet(){
        this.name = "";
        this.type = "HOUSEHOLD";
        this.questions = new ArrayList<Question>();
    }

    /*
     * Modifiers
     */
    public void setName(String name) { this.name = name; }
    public void setType(String type) {
        if (type.equals("Community") ) {
            this.type = "AREA";
            System.out.println("type==Community");
        } else if (type.equals("Household")){
            this.type = "HOUSEHOLD";
            System.out.println("type==Household");

        }
    }
    public void addQuestion(Question qs) { questions.add(qs); }
    public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }

    /*
     * Accessors
     */
    public String getName() { return name; }
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
