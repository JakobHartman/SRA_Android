package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String Label;
    private String Type;
    private ArrayList<String> Answers;

    /*
     * Constructor
     */
    public Datapoint() {
        this.Label = "";
        this.Type = DatapointTypes.TEXT;
        this.Answers = new ArrayList<String>();
    }

    /*
     * Modifiers
     */
    public void setLabel(String label) {
        this.Label = label;
    }
    public void setType(String type) { this.Type = type; }
    public void addAnswer(String answer) { this.Answers.add(answer); }
    public void setSingleAnswer(String answer) {
        if (this.Answers.isEmpty()) this.Answers.add(answer);
        else this.Answers.set(0, answer);
    }
    public void setAnswers(ArrayList<String> answers) { this.Answers = answers; }

    /*
     * Accessors
     */
    public String getLabel() { return Label; }
    public String getType() { return Type; }
    public boolean dataTypeIsAList() {
        return Type.equals(DatapointTypes.LIST_SINGLE_ANSWER) || Type.equals(DatapointTypes.LIST_MULTI_ANSWER);
    }
    public ArrayList<String> getAnswers() { return Answers; }
    public String getSingleAnswer() {
        if (Answers.isEmpty()) return "";
        return Answers.get(0);
    }
}
