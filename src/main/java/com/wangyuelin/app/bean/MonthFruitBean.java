package com.wangyuelin.app.bean;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "month_fruit")
public class MonthFruitBean {
    private String month;

    @Transient
    private List<String> fruits;

    private String fruitStr;

    private int monthNum;//数字表示月份

    public String getMonth() {
        return month;
    }

    public List<String> getFruits() {
        return fruits;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setFruits(List<String> fruits) {
        this.fruits = fruits;
    }

    public String getFruitStr() {
        return fruitStr;
    }

    public void setFruitStr(String fruitStr) {
        this.fruitStr = fruitStr;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }

    @Override
    public String toString() {
        String res = "month = " + getMonth() + " fruits = " + getFruits() +  " fruitStr = " + getFruitStr() + " monthNum = " + getMonthNum();
        return res;
    }
}
