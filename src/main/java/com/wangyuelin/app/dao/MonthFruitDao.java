package com.wangyuelin.app.dao;

import com.wangyuelin.app.bean.MonthFruitBean;
import com.wangyuelin.app.config.mybatis.baseMapper.BaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MonthFruitDao extends BaseMapper<MonthFruitBean> {

    @Select("select * from month_fruit where monthNum = #{month}")
    MonthFruitBean getByMonth(@Param("month") int month);

    @Update("update month_fruit set fruitStr=#{fruitStr} where monthNum = #{month} ")
    void updateByMonth(@Param("month") int month,  @Param("fruitStr") String fruitStr);

    @Insert("insert into month_fruit(fruitStr, monthNum, month) values(#{monthFruit.fruitStr}, #{monthFruit.monthNum}, #{monthFruit.month})")
    void add(@Param("monthFruit") MonthFruitBean monthFruitBean);



}
