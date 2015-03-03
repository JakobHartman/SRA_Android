package org.rbdc.sra.objects;

import com.shaded.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

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

    private ArrayList<ImageData> imageCollection;


    public void addImage(ImageData image){
        if (imageCollection == null){
            imageCollection = new ArrayList<>();
        }
        imageCollection.add(image);
    }

    public ArrayList<ImageData> getImageCollection() {
        return imageCollection;
    }
    public void setImageCollection(ArrayList<ImageData> imageCollection) {
        this.imageCollection = imageCollection;
    }

    public Member(){
        imageCollection = new ArrayList<>();
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
