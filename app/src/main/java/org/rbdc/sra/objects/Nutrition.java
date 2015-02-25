package org.rbdc.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;


public class Nutrition implements Serializable {
    private String householdID;
    private ArrayList<FoodItem> foodItems;
    private String householdName;

    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public void addFoodItem(FoodItem item){
        foodItems.add(item);
    }

    public void removeFoodItem(int item){
        foodItems.remove(item);
    }

    public String getHouseholdID() {
        return householdID;
    }

    public void setHouseholdID(String householdID) {
        this.householdID = householdID;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }
}
