package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 1/7/15.
 */
public class CountryLogin implements Serializable {
    private String name;
    private ArrayList<RegionLogin> regions;
    private ArrayList<String> regionNames;

    public CountryLogin(){
        regions = new ArrayList<RegionLogin>();

    }

    public void addRegion(RegionLogin region){
        regions.add(region);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegions(ArrayList<RegionLogin> regions) {
        this.regions = regions;
    }

    public String getName() {
        return name;
    }

    public ArrayList<RegionLogin> getRegions() {
        return regions;
    }
}
