package com.wangyuelin.app.service;

import com.wangyuelin.app.bean.MonthFruitBean;
import com.wangyuelin.app.dao.MonthFruitDao;
import com.wangyuelin.app.service.itf.IMonthFruit;
import com.wangyuelin.app.utils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonthFruitImpl implements IMonthFruit{
    private final Logger logger = LoggerFactory.getLogger(MonthFruitImpl.class);

    @Autowired
    private MonthFruitDao monthFruitDao;


    @Override
    public void saveAll(List<MonthFruitBean> data) {
        if (data == null || data.size() == 0){
            logger.info("抓取到的数据为空");
        }
        logger.info("将抓取到的数据保存到数据库");
        for (MonthFruitBean bean: data){
            if (TextUtil.isEmpty(bean.getMonth()) || bean.getMonthNum() <= 0){
                break;
            }
            MonthFruitBean oldFruit = monthFruitDao.getByMonth(bean.getMonthNum());
            if (oldFruit == null){
                logger.info("添加： 月份：" + bean.getMonthNum() + " 水果：" + bean.getFruitStr());
                monthFruitDao.add(bean);
            }else {
                logger.info("更新： 月份：" + bean.getMonthNum() + " 水果：" + bean.getFruitStr());
                monthFruitDao.updateByMonth(bean.getMonthNum(), bean.getFruitStr());
            }


        }


    }

    /**
     * 获取所有存储的水果
     * @param
     * @return
     */
    public List<String> getFruits(){

        List<MonthFruitBean> months = monthFruitDao.getAll();
        if (months == null || months.size() == 0){
            logger.info("查询到的每月对应的水果为空");
            return null;
        }

        ArrayList<String> result = new ArrayList<String>();
        for (MonthFruitBean bean : months){
            if (TextUtil.isEmpty(bean.getFruitStr())){
                continue;
            }

                String[] fruitArray = bean.getFruitStr().split("，");
                addUnique(result, fruitArray);
        }

        System.out.println("获取到的水果：" + result.toString());
        return result;


    }

    /**
     * 添加
     * @param result
     * @param fruitArray
     */
    private void addUnique(List<String> result, String[] fruitArray){
        if (result == null | fruitArray == null){
            return;
        }
        int size = fruitArray.length;
        for (int i = 0; i < size; i++){
            String fruit = fruitArray[i].replaceAll(" ", "");
            if (TextUtil.isEmpty(fruit)){
                continue;
            }
            if (result.contains(fruit)){
                continue;
            }else {
                result.add(fruit);
            }
        }
    }

    public List<MonthFruitBean> getAll(){
        return monthFruitDao.getAll();
    }

}
