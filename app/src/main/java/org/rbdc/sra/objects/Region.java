package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/15/14.
 */
public class Region implements Serializable {
    private String regionName;
    private ArrayList<Areas> areas;

    public Region(){
        regionName = new String();
        areas = new ArrayList<Areas>();
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setAreas(ArrayList<Areas> areas) {
        this.areas = areas;
    }

    public ArrayList<Areas> getAreas() {
        return areas;
    }

    public void addArea(Areas area){
        areas.add(area);
    }

    public Areas getArea(String areaName) {
        for (Areas a : areas) {
            if (a.getAreaName().equals(areaName))
                return a;
        }
        return null;
    }
}
