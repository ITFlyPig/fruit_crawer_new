package com.wangyuelin.app.bean;

import java.io.Serializable;

public class Tag implements Serializable {
    private String tag;//标签
    private String childSText;//子标签的文字

    public String getTag() {
        return tag;
    }


    public String getChildSText() {
        return childSText;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public void setChildSText(String childSText) {
        this.childSText = childSText;
    }

    @Override
    public String toString() {
        return " tag:" + tag + "  childSText:" + childSText;
    }
}
