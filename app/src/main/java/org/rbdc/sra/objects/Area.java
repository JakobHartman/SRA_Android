package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jakobhartman on 11/15/14.
 */
public class Area implements Serializable {
    private String name;
    private String country;
    private ArrayList<Household> resources;
    private String region;
    private ArrayList<Interview> interviews;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Area(){
        resources = new ArrayList<Household>();
        interviews = new ArrayList<Interview>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Household> getResources() {
        return resources;
    }

    public Household getHousehold(String householdName) {
        for (Household h : resources) {
            if (h.getName().equals(householdName))
                return h;
        }
        return null;
    }

    public void addHousehold(Household household){
        resources.add(household);
    }

    public void setResources(ArrayList<Household> resources) {
        this.resources = resources;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }
    public ArrayList<Interview> getInterviews() {
        return interviews;
    }
}
