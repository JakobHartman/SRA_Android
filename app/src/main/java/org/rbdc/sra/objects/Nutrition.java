package org.rbdc.sra.objects;

import java.io.Serializable;

/**
 * Created by jakobhartman on 2/19/15.
 */
public class Nutrition implements Serializable {
    private String foodName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
