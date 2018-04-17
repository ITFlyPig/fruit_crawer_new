package com.wangyuelin.app.bean;

import javax.persistence.Transient;
import java.util.List;

public class Fruit {
    private int id;
    private String icon;//图片
    private String desc;//简介
    private String name;//水果的名称
    private String func;//功效
    @Transient
    private List<Tag> tags;//历史、注意事项等
    @Transient
    private List<CookBookBean> cookBookList;//菜谱

    public String getIcon() {
        return icon;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<CookBookBean> getCookBookList() {
        return cookBookList;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setCookBookList(List<CookBookBean> cookBookList) {
        this.cookBookList = cookBookList;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("name: " ).append(getName()).append("  icon:").append(getIcon()).append("  desc:").append(getDesc())
                .append("  func:").append(getFunc());
        return stringBuffer.toString();
    }
}
