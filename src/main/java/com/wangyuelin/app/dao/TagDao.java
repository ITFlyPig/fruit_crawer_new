package com.wangyuelin.app.dao;

import com.wangyuelin.app.bean.Tag;
import com.wangyuelin.app.config.mybatis.baseMapper.BaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TagDao extends BaseMapper<Tag> {

    //查询特定水果对应的tag
    @Select("select tag as tag, child_tag as childSText from tag where fruit_id = #{fruitId} and tag = #{tagName}")
    Tag getTag(@Param("fruitId") int fruitId, @Param("tagName") String tagName);

    //插入tag
    @Insert("insert into tag(tag, child_tag, fruit_id) values(#{tag.tag}, #{tag.childSText}, #{fruitId})")
    void insertOne(@Param("tag") Tag tag, @Param("fruitId") int fruitId);

    //更新一个标签
    @Update("update tag set child_tag=#{tag.childSText} where tag=#{tag.tag} and fruit_id=#{fruitId}")
    void updateOne(@Param("tag") Tag tag, @Param("fruitId") int fruitId);



}
