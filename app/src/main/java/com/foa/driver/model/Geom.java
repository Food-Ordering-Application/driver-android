package com.foa.driver.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geom {
    @SerializedName("type")
    private String type;
    @SerializedName("coordinates")
    private List<Float> coordinates;

    public Geom(String type, List<Float> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
