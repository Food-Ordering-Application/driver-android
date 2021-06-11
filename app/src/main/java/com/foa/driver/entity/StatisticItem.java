package com.foa.driver.entity;

import com.foa.driver.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticItem {
    private int id;
    private float value;
    private String day;

    public StatisticItem(int id, String day,float value) {
        this.id = id;
        this.day =day;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public static List<StatisticItem> getSampleList(int radioButton){
        List<StatisticItem> list = new ArrayList<>();
        switch (radioButton){
            case R.id.todayRadioButton:
                list.add(new StatisticItem(1,"Hôm nay",10000));
                break;
            case R.id.thisWeekRadioButton:
                list.clear();
                list.add(new StatisticItem(0,"Hai",10000));
                list.add(new StatisticItem(1,"Ba",12000));
                list.add(new StatisticItem(2,"Tư",13000));
                list.add(new StatisticItem(3,"Năm",14000));
                list.add(new StatisticItem(4,"Sáu",7000));
                list.add(new StatisticItem(5,"Bảy",10000));
                list.add(new StatisticItem(6,"CN",12000));
                break;
            case R.id.thisMonthRadioButton:
                list.clear();
                list.add(new StatisticItem(1,"1/6/2021",10000));
                list.add(new StatisticItem(2,"2/6/2021",20000));
                list.add(new StatisticItem(3,"3/6/2021",30000));
                list.add(new StatisticItem(4,"4/6/2021",40000));
                list.add(new StatisticItem(6,"4/6/2021",22000));
                list.add(new StatisticItem(7,"4/6/2021",33000));
                list.add(new StatisticItem(8,"4/6/2021",31000));
                list.add(new StatisticItem(9,"4/6/2021",53000));
                list.add(new StatisticItem(10,"4/6/2021",10000));
                list.add(new StatisticItem(11,"4/6/2021",10000));
                list.add(new StatisticItem(12,"4/6/2021",10000));
                list.add(new StatisticItem(13,"4/6/2021",10000));
                list.add(new StatisticItem(14,"4/6/2021",10000));
                list.add(new StatisticItem(15,"4/6/2021",10000));
                list.add(new StatisticItem(16,"4/6/2021",10000));
                list.add(new StatisticItem(17,"4/6/2021",10000));
                list.add(new StatisticItem(18,"4/6/2021",10000));
                list.add(new StatisticItem(19,"4/6/2021",10000));
                list.add(new StatisticItem(20,"4/6/2021",10000));
                list.add(new StatisticItem(21,"4/6/2021",10000));
                list.add(new StatisticItem(22,"4/6/2021",10000));
                list.add(new StatisticItem(23,"4/6/2021",10000));
                list.add(new StatisticItem(24,"4/6/2021",10000));
                list.add(new StatisticItem(25,"4/6/2021",10000));
                list.add(new StatisticItem(26,"4/6/2021",10000));
                list.add(new StatisticItem(27,"4/6/2021",10000));
                list.add(new StatisticItem(28,"4/6/2021",10000));
                list.add(new StatisticItem(29,"4/6/2021",10000));
                list.add(new StatisticItem(30,"4/6/2021",10000));
                break;

        }

        return list;
    }
}
