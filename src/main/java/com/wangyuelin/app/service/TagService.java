package com.wangyuelin.app.service;

import com.wangyuelin.app.bean.Fruit;
import com.wangyuelin.app.bean.Tag;
import com.wangyuelin.app.dao.FruitInfoDao;
import com.wangyuelin.app.dao.TagDao;
import com.wangyuelin.app.service.itf.ITag;
import com.wangyuelin.app.utils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService implements ITag {
    private final Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    private TagDao tagDao;
    @Autowired
    private FruitInfoDao fruitInfoDao;


    @Override
    public void addAll(List<Tag> tags, String fruitName) {
        logger.info("开始插入标签");
        if (tags == null || tags.size()== 0 || TextUtil.isEmpty(fruitName)){
            return;
        }

        Fruit fruit = fruitInfoDao.getFruitByName(fruitName);
        if (fruit == null){
            logger.info("水果不存在");
            return;
        }

        for (Tag tag : tags){
            Tag old = tagDao.getTag(fruit.getId(), tag.getTag());
            if (old == null){
                logger.info("开始插入");
                tagDao.insertOne(tag, fruit.getId());
            }else {
                logger.info("开始更新");
                tagDao.updateOne(tag, fruit.getId());

            }
            logger.info("tag的信息：" + tag.toString());
        }

    }
}
