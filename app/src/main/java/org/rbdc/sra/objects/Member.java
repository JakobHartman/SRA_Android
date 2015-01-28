package org.rbdc.sra.objects;

import com.shaded.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by imac on 1/5/15.
 */
public class Member implements Serializable {
    private String name;
    private String relationship;
    private String birthday;
    private String educationLevel;
    private String gender;
    private boolean inschool;

    @JsonIgnore
    private int memberId;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Member(){

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

    public boolean isInschool() {
        return inschool;
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

    public void setInschool(boolean inschool) {
        this.inschool = inschool;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
