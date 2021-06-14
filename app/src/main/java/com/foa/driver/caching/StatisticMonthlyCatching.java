package com.foa.driver.caching;

import com.foa.driver.model.Order;
import com.foa.driver.model.StatisticItem;

import java.util.List;

public class StatisticMonthlyCatching {
    private static List<StatisticItem> statisticItemList = null;

    public static List<StatisticItem> getOrderCatching(){
        return statisticItemList;
    }

    public static void setInstance(List<StatisticItem> statisticItems){
        statisticItemList = statisticItems;
    }

    public static void clearInstance(){
        statisticItemList = null;
    }
}
