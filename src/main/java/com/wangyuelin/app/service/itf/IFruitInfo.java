package com.wangyuelin.app.service.itf;

import com.wangyuelin.app.bean.Fruit;

import java.util.List;

public interface IFruitInfo {
    void addAll(List<Fruit> fruits);

    void add(Fruit fruit);

    void updateDesc(String name, String desc);

}
