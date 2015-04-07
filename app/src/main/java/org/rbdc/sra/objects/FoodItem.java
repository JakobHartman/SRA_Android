package org.rbdc.sra.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodItem implements Serializable {

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("nf_water_grams")
    @Expose
    private Object nfWaterGrams;
    @SerializedName("nf_calories")
    @Expose
    private Object nfCalories;
    @SerializedName("nf_calories_from_fat")
    @Expose
    private Object nfCaloriesFromFat;
    @SerializedName("nf_total_fat")
    @Expose
    private Object nfTotalFat;
    @SerializedName("nf_saturated_fat")
    @Expose
    private Object nfSaturatedFat;
    @SerializedName("nf_trans_fatty_acid")
    @Expose
    private Object nfTransFattyAcid;
    @SerializedName("nf_cholesterol")
    @Expose
    private Object nfCholesterol;
    @SerializedName("nf_sodium")
    @Expose
    private Object nfSodium;
    @SerializedName("nf_total_carbohydrate")
    @Expose
    private Object nfTotalCarbohydrate;
    @SerializedName("nf_dietary_fiber")
    @Expose
    private Object nfDietaryFiber;
    @SerializedName("nf_sugars")
    @Expose
    private Object nfSugars;
    @SerializedName("nf_protein")
    @Expose
    private Object nfProtein;
    @SerializedName("nf_vitamin_a_dv")
    @Expose
    private Object nfVitaminADv;
    @SerializedName("nf_vitamin_c_dv")
    @Expose
    private Object nfVitaminCDv;
    @SerializedName("nf_calcium_dv")
    @Expose
    private Object nfCalciumDv;
    @SerializedName("nf_iron_dv")
    @Expose
    private Object nfIronDv;
    @SerializedName("servings")
    @Expose
    private Object servings;

    /**
     *
     * @return
     * The itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The item_id
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param itemName
     * The item_name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     *
     * @return
     * The nfWaterGrams
     */
    public Object getNfWaterGrams() {
        return nfWaterGrams;
    }

    /**
     *
     * @param nfWaterGrams
     * The nf_water_grams
     */
    public void setNfWaterGrams(Object nfWaterGrams) {
        this.nfWaterGrams = nfWaterGrams;
    }

    /**
     *
     * @return
     * The nfCalories
     */
    public Object getNfCalories() {
        return nfCalories;
    }

    /**
     *
     * @param nfCalories
     * The nf_calories
     */
    public void setNfCalories(Object nfCalories) {
        this.nfCalories = nfCalories;
    }

    /**
     *
     * @return
     * The nfCaloriesFromFat
     */
    public Object getNfCaloriesFromFat() {
        return nfCaloriesFromFat;
    }

    /**
     *
     * @param nfCaloriesFromFat
     * The nf_calories_from_fat
     */
    public void setNfCaloriesFromFat(Object nfCaloriesFromFat) {
        this.nfCaloriesFromFat = nfCaloriesFromFat;
    }

    /**
     *
     * @return
     * The nfTotalFat
     */
    public Object getNfTotalFat() {
        return nfTotalFat;
    }

    /**
     *
     * @param nfTotalFat
     * The nf_total_fat
     */
    public void setNfTotalFat(Object nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    /**
     *
     * @return
     * The nfSaturatedFat
     */
    public Object getNfSaturatedFat() {
        return nfSaturatedFat;
    }

    /**
     *
     * @param nfSaturatedFat
     * The nf_saturated_fat
     */
    public void setNfSaturatedFat(Object nfSaturatedFat) {
        this.nfSaturatedFat = nfSaturatedFat;
    }

    /**
     *
     * @return
     * The nfTransFattyAcid
     */
    public Object getNfTransFattyAcid() {
        return nfTransFattyAcid;
    }

    /**
     *
     * @param nfTransFattyAcid
     * The nf_trans_fatty_acid
     */
    public void setNfTransFattyAcid(Object nfTransFattyAcid) {
        this.nfTransFattyAcid = nfTransFattyAcid;
    }

    /**
     *
     * @return
     * The nfCholesterol
     */
    public Object getNfCholesterol() {
        return nfCholesterol;
    }

    /**
     *
     * @param nfCholesterol
     * The nf_cholesterol
     */
    public void setNfCholesterol(Object nfCholesterol) {
        this.nfCholesterol = nfCholesterol;
    }

    /**
     *
     * @return
     * The nfSodium
     */
    public Object getNfSodium() {
        return nfSodium;
    }

    /**
     *
     * @param nfSodium
     * The nf_sodium
     */
    public void setNfSodium(Object nfSodium) {
        this.nfSodium = nfSodium;
    }

    /**
     *
     * @return
     * The nfTotalCarbohydrate
     */
    public Object getNfTotalCarbohydrate() {
        return nfTotalCarbohydrate;
    }

    /**
     *
     * @param nfTotalCarbohydrate
     * The nf_total_carbohydrate
     */
    public void setNfTotalCarbohydrate(Object nfTotalCarbohydrate) {
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
    }

    /**
     *
     * @return
     * The nfDietaryFiber
     */
    public Object getNfDietaryFiber() {
        return nfDietaryFiber;
    }

    /**
     *
     * @param nfDietaryFiber
     * The nf_dietary_fiber
     */
    public void setNfDietaryFiber(Object nfDietaryFiber) {
        this.nfDietaryFiber = nfDietaryFiber;
    }

    /**
     *
     * @return
     * The nfSugars
     */
    public Object getNfSugars() {
        return nfSugars;
    }

    /**
     *
     * @param nfSugars
     * The nf_sugars
     */
    public void setNfSugars(Object nfSugars) {
        this.nfSugars = nfSugars;
    }

    /**
     *
     * @return
     * The nfProtein
     */
    public Object getNfProtein() {
        return nfProtein;
    }

    /**
     *
     * @param nfProtein
     * The nf_protein
     */
    public void setNfProtein(Object nfProtein) {
        this.nfProtein = nfProtein;
    }

    /**
     *
     * @return
     * The nfVitaminADv
     */
    public Object getNfVitaminADv() {
        return nfVitaminADv;
    }

    /**
     *
     * @param nfVitaminADv
     * The nf_vitamin_a_dv
     */
    public void setNfVitaminADv(Object nfVitaminADv) {
        this.nfVitaminADv = nfVitaminADv;
    }

    /**
     *
     * @return
     * The nfVitaminCDv
     */
    public Object getNfVitaminCDv() {
        return nfVitaminCDv;
    }

    /**
     *
     * @param nfVitaminCDv
     * The nf_vitamin_c_dv
     */
    public void setNfVitaminCDv(Object nfVitaminCDv) {
        this.nfVitaminCDv = nfVitaminCDv;
    }

    /**
     *
     * @return
     * The nfCalciumDv
     */
    public Object getNfCalciumDv() {
        return nfCalciumDv;
    }

    /**
     *
     * @param nfCalciumDv
     * The nf_calcium_dv
     */
    public void setNfCalciumDv(Object nfCalciumDv) {
        this.nfCalciumDv = nfCalciumDv;
    }

    /**
     *
     * @return
     * The nfIronDv
     */
    public Object getNfIronDv() {
        return nfIronDv;
    }

    /**
     *
     * @param nfIronDv
     * The nf_iron_dv
     */
    public void setNfIronDv(Object nfIronDv) {
        this.nfIronDv = nfIronDv;
    }

}