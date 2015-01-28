package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */
public class Question implements Serializable {

    private String name;
    private Boolean multiUse;
    private ArrayList<Datapoint> dataPoints;

    /*
     * Constructor
     */
    public Question(String refUrl) {
        this.name = "";
        this.multiUse = false;
        this.dataPoints = new ArrayList<Datapoint>();
    }

    public Question(){}


    /*
     * Modifiers
     */
    public void setName(String name) { this.name = name; }
    public void setMultiUse(Boolean isMultiUse) { this.multiUse = isMultiUse; }
    public void setDataPoints(ArrayList<Datapoint> dataPoints) { this.dataPoints = dataPoints; }
    public void addDataPoint(Datapoint datapoint) {
        for (Datapoint dp : dataPoints) {
            if (dp == datapoint) {
                return;
            }
        }
        dataPoints.add(datapoint);
    }
    public void deleteDataPoint(Datapoint dp) { dataPoints.remove(dp); }
    public void deleteDataPoint(String label) {
        for (Datapoint dp : dataPoints) {
            if (dp.getLabel().equals(label)) {
                dataPoints.remove(dp);
                return;
            }
        }
    }

    /*
     * Accessors
     */
    public String getName() { return name; }
    public Boolean getMultiUse() { return multiUse; }
    public ArrayList<Datapoint> getDataPoints() { return dataPoints; }
    public Datapoint getDataPoint(String label) {
        for (Datapoint dp : dataPoints) {
            if (dp.getLabel().equals(label)) {
                return dp;
            }
        }
        return null;
    }
}
