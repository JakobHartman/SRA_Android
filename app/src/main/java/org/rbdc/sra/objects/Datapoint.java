package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String label;
    private String dataType;
    private ArrayList<String> answers;

    private String optionListType;
    private ArrayList<String> options;

    /*
     * Constructor
     */
    public Datapoint() {
        this.label = "";
        this.dataType = DatapointTypes.TEXT;
        this.answers = new ArrayList<String>();

        optionListType = "List";
        options = new ArrayList<String>();
    }

    /*
     * Modifiers
     */
    public void setLabel(String label) {
        this.label = label;
        if (!dataTypeIsAList())
            options.clear();
    }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public void addAnswer(String answer) { this.answers.add(answer); }
    public void setSingleAnswer(String answer) {
        if (this.answers.isEmpty()) this.answers.add(answer);
        else this.answers.set(0, answer);
    }
    public void setAnswers(ArrayList<String> answers) { this.answers = answers; }
    public void setOptionsType(String type) { this.optionListType = type; }
    public void addOption(String option) { options.add(option); }
    public void setOption(int index, String option) { options.set(index, option); }
    public void deleteOption(String option) { options.remove(option); }
    public void deleteOption(int index) { options.remove(index); }

    /*
     * Accessors
     */
    public String getLabel() { return label; }
    public String getDataType() { return dataType; }
    public boolean dataTypeIsAList() {
        return dataType.equals(DatapointTypes.LIST_SINGLE_ANSWER) || dataType.equals(DatapointTypes.LIST_MULTI_ANSWER);
    }
    public ArrayList<String> getAnswers() { return answers; }
    public String getSingleAnswer() {
        if (answers.isEmpty()) return "";
        return answers.get(0);
    }
    public String getOptionListType() { return optionListType; }
    public ArrayList<String> getOptions() { return options; }
}
