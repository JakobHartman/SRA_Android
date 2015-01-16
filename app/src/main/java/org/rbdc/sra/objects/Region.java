package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/15/14.
 */
public class Region implements Serializable {
    private String Name;
    private ArrayList<Area> Areas;
    private String Country;

    public Region(){
        Name = new String();
        Areas = new ArrayList<Area>();
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setAreas(ArrayList<Area> areas) {
        this.Areas = areas;
    }

    public ArrayList<Area> getAreas() {
        return Areas;
    }

    public void addArea(Area area){
        Areas.add(area);
    }

    public Area getArea(String areaName) {
        for (Area a : Areas) {
            if (a.getName().equals(areaName))
                return a;
        }
        return null;
    }
}
