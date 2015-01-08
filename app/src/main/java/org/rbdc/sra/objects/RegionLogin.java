package org.rbdc.sra.objects;

import org.rbdc.sra.objects.AreaLogin;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 1/7/15.
 */
public class RegionLogin implements Serializable {
    private String name;
    private ArrayList<AreaLogin> areas;

    public void setName(String name) {
        this.name = name;
    }

    public void setAreas(ArrayList<AreaLogin> areas) {
        this.areas = areas;
    }

    public String getName() {
        return name;
    }

    public ArrayList<AreaLogin> getAreas() {
        return areas;
    }
}
