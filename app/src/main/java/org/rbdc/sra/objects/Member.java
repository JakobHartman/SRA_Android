package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * Created by imac on 1/5/15.
 */
public class Member implements Serializable {
    private String name;
    private String relationship;
    private String birthday;
    private String educationLevel;
    private String gender;
    private boolean inSchool;
    private String areaName;
    private String householdName;

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public boolean isInSchool() {
        return inSchool;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setInSchool(boolean inSchool) {
        this.inSchool = inSchool;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
