package org.rbdc.sra.objects;

import java.io.Serializable;

/**
 * Created by imac on 1/7/15.
 */
public class AreaLogin implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
