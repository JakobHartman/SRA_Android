package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String label;
    private String type;
    private ArrayList<String> answers;
    private ArrayList<String> options;

    /*
     * Constructor
     */
    public Datapoint() {
        this.label = "";
        this.type = DatapointTypes.TEXT;
        this.options = new ArrayList<String>();
        this.answers = new ArrayList<String>();
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    /*
    * Modifiers
    */
    public void setLabel(String label) {
        this.label = label;
    }
    public void setType(String type) { this.type = type; }
    public void addAnswer(String answer) { this.answers.add(answer); }
    public void setSingleAnswer(String answer) {
        if (this.answers.isEmpty()) this.answers.add(answer);
        else this.answers.set(0, answer);
    }
    public void setAnswers(ArrayList<String> answers) { this.answers = answers; }

    /*
     * Accessors
     */
    public String getLabel() { return label; }
    public String getType() { return type; }
    public boolean dataTypeIsAList() {
        return type.equals(DatapointTypes.LIST_SINGLE_ANSWER) || type.equals(DatapointTypes.LIST_MULTI_ANSWER);
    }
    public ArrayList<String> getAnswers() { return answers; }
    public String getSingleAnswer() {
        if (answers.isEmpty()) return "";
        return answers.get(0);
    }
}
