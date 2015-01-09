package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 1/7/15.
 */
public class Country implements Serializable {
    private String countryName;
    private ArrayList<Region> regions;

    public Country(){
        regions = new ArrayList<Region>();
    }

    public void addRegion(Region region){
        regions.add(region);
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
