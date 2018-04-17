package com.wangyuelin.app.dao;

import com.wangyuelin.app.bean.Fruit;
import com.wangyuelin.app.config.mybatis.baseMapper.BaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FruitInfoDao extends BaseMapper<Fruit> {

    //插入一条水果相关的信息
    @Insert("insert into fruit_info(fruit_info.`name`, fruit_info.icon, fruit_info.`desc`, fruit_info.func) values(#{fruit.name}, #{fruit.icon}, #{fruit.desc}, #{fruit.func})")
    void insertOneFruit(@Param("fruit") Fruit fruit);

    //查询对应的水果是否存在相应的信息
    @Select("select * from fruit_info where name = #{name}")
    Fruit getFruitByName(@Param("name") String name);

    //更新水果对应的信息
    @Update("update fruit_info set  fruit_info.icon=#{fruit.icon}, fruit_info.desc=#{fruit.desc}, fruit_info.func=#{fruit.func} where fruit_info.name=#{fruit.name}")
    void updateFruitInfo(@Param("fruit") Fruit fruit);

    //更新简介
    @Update("update fruit_info set fruit_info.desc=#{desc} where fruit_info.name=#{name}")
    void updateDescByName(@Param("name") String name, @Param("desc") String desc);


}