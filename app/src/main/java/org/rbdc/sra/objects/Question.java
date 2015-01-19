package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */
public class Question implements Serializable {

    private String Name;
    private Boolean multiUse;
    private ArrayList<Datapoint> DataPoints;

    /*
     * Constructor
     */
    public Question(String refUrl) {
        this.Name = "";
        this.multiUse = false;
        this.DataPoints = new ArrayList<Datapoint>();
    }

    public Question(){}


    /*
     * Modifiers
     */
    public void setName(String name) { this.Name = name; }
    public void setMultiUse(Boolean isMultiUse) { this.multiUse = isMultiUse; }
    public void setDataPoints(ArrayList<Datapoint> dataPoints) { this.DataPoints = dataPoints; }
    public void addDataPoint(Datapoint datapoint) {
        for (Datapoint dp : DataPoints) {
            if (dp == datapoint) {
                return;
            }
        }
        DataPoints.add(datapoint);
    }
    public void deleteDataPoint(Datapoint dp) { DataPoints.remove(dp); }
    public void deleteDataPoint(String label) {
        for (Datapoint dp : DataPoints) {
            if (dp.getLabel().equals(label)) {
                DataPoints.remove(dp);
                return;
            }
        }
    }

    /*
     * Accessors
     */
    public String getName() { return Name; }
    public Boolean getMultiUse() { return multiUse; }
    public ArrayList<Datapoint> getDataPoints() { return DataPoints; }
    public Datapoint getDataPoint(String label) {
        for (Datapoint dp : DataPoints) {
            if (dp.getLabel().equals(label)) {
                return dp;
            }
        }
        return null;
    }
}
