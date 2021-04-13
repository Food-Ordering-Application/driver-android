package com.foa.driver.entity;

import java.util.ArrayList;
import java.util.List;

public class StatisticItem {
    private int id;
    private float value;

    public StatisticItem(int id, float value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public static List<StatisticItem> getSampleList(){
        List<StatisticItem> list = new ArrayList<>();
        list.add(new StatisticItem(1,10));
        list.add(new StatisticItem(2,12));
        list.add(new StatisticItem(3,13));
        list.add(new StatisticItem(4,14));
        list.add(new StatisticItem(5,7));
        return list;
    }
}
