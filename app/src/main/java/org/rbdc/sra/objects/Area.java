package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jakobhartman on 11/15/14.
 */
public class Area implements Serializable {
    private String Name;
    private String Country;
    private ArrayList<Household> Resources;
    private String Region;
    private ArrayList<Interview> interviews;


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public Area(){
        Resources = new ArrayList<Household>();
        interviews = new ArrayList<Interview>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Household> getResources() {
        return Resources;
    }

    public Household getHousehold(String householdName) {
        for (Household h : Resources) {
            if (h.getName().equals(householdName))
                return h;
        }
        return null;
    }

    public void addHousehold(Household household){
        Resources.add(household);
    }

    public void setResources(ArrayList<Household> resources) {
        this.Resources = resources;
    }

    public void setRegion(String region) {
        this.Region = region;
    }

    public String getRegion() {
        return Region;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }
    public ArrayList<Interview> getInterviews() {
        return interviews;
    }
}
