package com.wangyuelin.app.service;

import com.wangyuelin.app.bean.MonthFruitBean;
import com.wangyuelin.app.dao.MonthFruitDao;
import com.wangyuelin.app.service.itf.IMonthFruit;
import com.wangyuelin.app.utils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
