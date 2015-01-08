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

    public ArrayList<String> getRegionNames() {
        for(RegionLogin names : regions){
            regionNames.add(names.getName());
        }
        return regionNames;
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
