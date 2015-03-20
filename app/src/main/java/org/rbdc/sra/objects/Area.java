package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;



public class Area implements Serializable {
    private String name;
    private String country;
    private ArrayList<Household> resources;
    private String region;
    //private ArrayList<Interview> interviews;
    private ImageData mainImage;
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

    public ImageData getMainImage() {
        return mainImage;
    }

    public void setImageCollection(ArrayList<ImageData> imageCollection) {
        this.imageCollection = imageCollection;
    }

    public void setMainImage(ImageData mainImage) {
        this.mainImage = mainImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Area(){
        imageCollection = new ArrayList<>();
        resources = new ArrayList<>();
        //interviews = new ArrayList<>();
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

//    public void setInterviews(ArrayList<Interview> interviews) {
//        this.interviews = interviews;
//    }
//    public ArrayList<Interview> getInterviews() {
//        return interviews;
//    }
}
