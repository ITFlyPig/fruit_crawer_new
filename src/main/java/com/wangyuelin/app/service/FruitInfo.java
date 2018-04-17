package com.wangyuelin.app.service;

import com.wangyuelin.app.bean.Fruit;
import com.wangyuelin.app.dao.FruitInfoDao;
import com.wangyuelin.app.service.itf.IFruitInfo;
import com.wangyuelin.app.utils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitInfo implements IFruitInfo {
    private final Logger logger = LoggerFactory.getLogger(FruitInfo.class);

    @Autowired
    private FruitInfoDao fruitInfoDao;

    @Override
    public void addAll(List<Fruit> fruits) {
        if (fruits == null || fruits.size() == 0){
            logger.info("要添加的水果的基本信息为空");
            return;

        }
        for (Fruit fruit : fruits){
            Fruit old = fruitInfoDao.getFruitByName(fruit.getName());
            if (old == null){
                logger.info("插入");
                fruitInfoDao.insertOneFruit(fruit);
            }else {
                fruitInfoDao.updateFruitInfo(fruit);
                logger.info("更新");

            }
            logger.info("水果:" + fruit.toString());
        }
    }

    @Override
    public void add(Fruit fruit) {
        if (fruit == null){
            return;
        }
        Fruit old = fruitInfoDao.getFruitByName(fruit.getName());
        if (old == null){
            fruitInfoDao.insertOneFruit(fruit);
            logger.info("插入");
        }else {
            fruitInfoDao.updateFruitInfo(fruit);
            logger.info("更新");
        }

    }

    @Override
    public void updateDesc(String name, String desc) {
        if (TextUtil.isEmpty(name) || TextUtil.isEmpty(desc)){
            logger.info("更新水果简介时，名称或者简介为空");
            return;
        }
        logger.info("更新简介");
        fruitInfoDao.updateDescByName(name, desc);

    }

}
