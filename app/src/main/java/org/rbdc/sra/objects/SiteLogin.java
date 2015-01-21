package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 1/21/15.
 */
public class SiteLogin implements Serializable {

    private ArrayList<CountryLogin> countries;
    private int areaCount;
    private ArrayList<String> regionNames;

    public SiteLogin(){
        countries = new ArrayList<CountryLogin>();
        regionNames = new ArrayList<String>();
    }

    public ArrayList<String> getRegionNames() {
        for(CountryLogin country : countries) {
            for (RegionLogin names : country.getRegions()) {
                regionNames.add(names.getName());
            }
        }
        return regionNames;
    }

    public ArrayList<CountryLogin> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<CountryLogin> countries) {
        this.countries = countries;
    }

    public void addCountry(CountryLogin name){
        countries.add(name);
    }

    public int getAreaCount() {
        areaCount = 0;
        for (CountryLogin country : getCountries()){
            for (RegionLogin region : country.getRegions()) {
                for (AreaLogin area : region.getAreas()) {
                    areaCount++;
                }
            }
        }
        return areaCount;
    }
}
