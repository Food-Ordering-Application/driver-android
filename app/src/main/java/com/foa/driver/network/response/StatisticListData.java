package com.foa.driver.network.response;

import com.foa.driver.model.DriverTransaction;
import com.foa.driver.model.StatisticItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatisticListData {

    @SerializedName("statistic")
    List<StatisticItem> statisticItemList;

    public List<StatisticItem> getStatisticItemList() {
        return statisticItemList;
    }

    public void setStatisticItemList(List<StatisticItem> statisticItemList) {
        this.statisticItemList = statisticItemList;
    }
}

