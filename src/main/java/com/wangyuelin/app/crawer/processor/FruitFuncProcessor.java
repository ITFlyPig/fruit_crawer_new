package com.wangyuelin.app.crawer.processor;

import com.wangyuelin.app.bean.Fruit;
import com.wangyuelin.app.bean.Tag;
import com.wangyuelin.app.service.MonthFruitImpl;
import com.wangyuelin.app.service.TagService;
import com.wangyuelin.app.service.itf.IFruitInfo;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获取水果的功效
 */
@Component
public class FruitFuncProcessor implements PageProcessor {

    @Autowired
    private IFruitInfo fruitInfo;
    @Autowired
    private TagService tagService;
    @Autowired
    private MonthFruitImpl monthFruit;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    public static final String url = "https://www.baidu.com/s?wd=芒果的功效与作用";//抽取的网址


    private boolean isAdded;//是否添加了查询的url
    private HashMap<String, String> urlMap = new HashMap<String, String>();

    private static String[] NEED_TITLE = new String[]{"不宜", "文化", "历史", "食用"};//需要的标题

    public void process(Page page) {


        //添加所有水果的功效查询url
        if (!isAdded) {
            isAdded = true;
            int size = fruits.size();
            for (int i = 0; i < size; i++) {
                String fruit = fruits.get(i);
                String url = getQueryUrl(fruit);
                if (!TextUtils.isEmpty(url)) {
                    page.addTargetRequest(url);
                    urlMap.put(url, fruit);
                }
            }
        }

        String url = page.getUrl().get();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String fruit = urlMap.get(url);


        if (url.startsWith(FUNC_PREFIX) && !TextUtils.isEmpty(fruit)) {//功效
            String func = page.getHtml().xpath("//div[@class='op_exactqa_s_answer']/text()").get();
            String icon = page.getHtml().xpath("//div[@class='op_exactqa_main']//img/@src").get();
            String detail = getDetailUrl(fruit);
            page.addTargetRequest(detail);
            urlMap.put(detail, fruit);

            //将解析的结果保存到数据库
            Fruit oneFruit = new Fruit();
            oneFruit.setName(fruit);
            oneFruit.setIcon(icon);
            oneFruit.setFunc(func);
            fruitInfo.add(oneFruit);


        } else if (url.startsWith(DETAIL_PREFIX)) {//详情
            String desc = page.getHtml().xpath("//div[@class='lemma-summary']/div").get();//简介
            String descNoTag = parseDeatil(desc);
            //更新数据库的简介
            if (!TextUtils.isEmpty(descNoTag)){
                fruitInfo.updateDesc(fruit, descNoTag);//移除多余标签的简介
            }
            List<String> divs = page.getHtml().xpath("//div[@label-module]").all();
            List<Tag> list = pasreNeedtag(divs);
            tagService.addAll(list, fruit);
        }





    }

    public Site getSite() {
        return site;
    }


    private String getQueryStr(String fruit) {
        if (TextUtils.isEmpty(fruit)) {
            return null;
        }
        return fruit + "的功效与作用";

    }


    private static String FUNC_PREFIX = "https://www.baidu.com/s?wd=";
    private static String DETAIL_PREFIX = "https://baike.baidu.com/item/";

    /**
     * 获取查询的url
     *
     * @param fruit
     * @return
     */
    private String getQueryUrl(String fruit) {

        String queryStr = getQueryStr(fruit);
        if (TextUtils.isEmpty(fruit)) {
            return null;
        }
        return FUNC_PREFIX + queryStr;

    }

    private String getDetailUrl(String fruit) {
        return DETAIL_PREFIX + fruit;

    }



    private String parseDeatil(String deatilHtml) {
        String noTagsStr = removeAllTags(deatilHtml);
        if (TextUtils.isEmpty(noTagsStr)){
            return noTagsStr;
        }
        noTagsStr = noTagsStr.replaceAll(" ", "");//移除空格
        noTagsStr = noTagsStr.replaceAll("\n", "");//移除换行
        return noTagsStr;
    }


    /**
     * 解析得到感兴趣相关的标签
     *
     * @param
     * @return
     */
    private List<Tag> pasreNeedtag(List<String> divs) {

        if (divs == null) {
            return null;
        }

        int size = divs.size();
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        for (int i = 0; i < size; i++) {
            Document dc = Jsoup.parse(divs.get(i));
            Elements elements = dc.getAllElements();
            if (elements.size() == 0) {
                continue;
            }

            Element child = elements.first().getElementsByTag("div").first();

            if (isTitleTag(child) && isNeedTag(child)) {//开始获取标题下的内容
                Tag tag = new Tag();
                String tagStr = getTagContent(child);
                tag.setTag(tagStr);//标题

                int temp = i + 1;
                Element next = null;
                if (temp < size) {
                    next = Jsoup.parse(divs.get(temp)).getAllElements().first().getElementsByTag("div").first();

                }
                if (next == null) {
                    continue;
                }
                while (isItem(next)) {//是标题对应下的item，应该获取
                    String itemStr = next.text();
                    if (!TextUtils.isEmpty(itemStr)) {
                        String childStr = tag.getChildSText();
                        if (childStr == null) {
                            childStr = "";
                        }
                        tag.setChildSText(childStr + "\n" + itemStr);
                    }


                    //在接着取下一个
                    temp++;
                    if (temp < size) {
                        next = Jsoup.parse(divs.get(temp)).getAllElements().first().getElementsByTag("div").first();
                    } else {
                        temp--;//避免数组越界
                        break;
                    }
                }
                i = --temp;//和for循环中的++做抵消
                tagList.add(tag);

            }

        }

        return tagList;
    }


    /**
     * 移除所有的html标签
     *
     * @param str
     */
    private String removeAllTags(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String pattern = "<.+?>";
        return str.replaceAll(pattern, "");
    }

    /**
     * 检测是否是标题标签
     *
     * @param element
     * @return
     */
    private boolean isTitleTag(Element element) {
        if (element == null) {
            return false;
        }
        String lableStr = element.attr("label-module");
        if (!TextUtils.isEmpty(lableStr) && lableStr.equalsIgnoreCase("para-title")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否应该继续
     *
     * @param element
     * @return
     */
    private boolean isItem(Element element) {
        String tagStr = element.text();
        if (TextUtils.isEmpty(tagStr)) {
            return false;
        }
        String lableStr = element.attr("label-module");
        if (TextUtils.isEmpty(lableStr)) {
            return false;
        }
        if (lableStr.equalsIgnoreCase("para")) {
            return true;
        }
        return false;
    }

    /**
     * 是否是需要的标签
     *
     * @param element
     * @return
     */
    private boolean isNeedTag(Element element) {
        if (element == null) {
            return false;
        }
        String text = getElementTitle(element);
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        int size = NEED_TITLE.length;
        for (int i = 0; i < size; i++) {
            if (text.contains(NEED_TITLE[i])) {

                return true;
            }
        }


        return false;


    }

    /**
     * 递归获取当期那元素和子元素的文本内容
     *
     * @return
     */
    private String getElementHText(Element element) {
        String res = "";
        int childs = element.children().size();
        if (childs == 0) {
            if ("h1 h2 h3 h4 h5 h6".contains(element.tagName())) {
                res += element.text();
            }

        } else if (childs > 0) {
            if ("h1 h2 h3 h4 h5 h6".contains(element.tagName())) {
                res += element.text();
            }
            for (Element child : element.children()) {
                res += getElementHText(child);
            }

        }
        return res;
    }

    /**
     * 获取元素的标题
     *
     * @param element
     * @return
     */
    private String getElementTitle(Element element) {

        return getElementHText(element);

    }


    private void test(){

        String html = "<div class=\"para-title level-2\" label-module=\"para-title\"> \n" +
                " <h2 class=\"title-text\"><span class=\"title-prefix\">苹果</span>历史</h2> \n" +
                " <a class=\"edit-icon j-edit-link\" data-edit-dl=\"10\" href=\"javascript:;\"><em class=\"cmn-icon wiki-lemma-icons wiki-lemma-icons_edit-lemma\"></em>编辑</a> \n" +
                "</div>";

        Element element = Jsoup.parse(html).getAllElements().first().getElementsByTag("div").first();
        String res = getElementHText(element);
        System.out.println("res:" + res);
    }

    /**
     * 获取tag的文本
     * @param div
     * @return
     */
    private String getTagContent(Element div){
        Elements h2s = div.getElementsByTag("h2");
        if(h2s != null){
            Element h2 = h2s.first();
            if (h2 != null){
                return h2.ownText();
            }

        }

        Elements h3s = div.getElementsByTag("h3");
        if(h3s != null){
            Element h3 = h3s.first();
            return h3.ownText();
        }

        return null;

    }

    private List<String> fruits;

    /**
     * 开始爬取
     */
    public void start(){
        fruits = monthFruit.getFruits();
        Spider.create(this).addPipeline(new ConsolePipeline()).addUrl(getQueryUrl("芒果")).thread(1).run();

    }

}