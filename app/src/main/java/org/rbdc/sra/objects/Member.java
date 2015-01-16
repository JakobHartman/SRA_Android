package org.rbdc.sra.objects;

import java.io.Serializable;

/**
 * Created by imac on 1/5/15.
 */
public class Member implements Serializable {
    private String Name;
    private String Relationship;
    private String Birthday;
    private String EducationLevel;
    private String Gender;
    private boolean Inschool;

    public String getBirthday() {
        return Birthday;
    }

    public String getEducationLevel() {
        return EducationLevel;
    }

    public String getGender() {
        return Gender;
    }

    public String getName() {
        return Name;
    }

    public String getRelationship() {
        return Relationship;
    }

    public boolean isInschool() {
        return Inschool;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public void setEducationLevel(String educationLevel) {
        this.EducationLevel = educationLevel;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public void setInschool(boolean inschool) {
        this.Inschool = inschool;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setRelationship(String relationship) {
        this.Relationship = relationship;
    }
}
