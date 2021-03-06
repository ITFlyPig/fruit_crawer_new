package com.wangyuelin.app.controller;

import com.wangyuelin.app.bean.User;
import com.wangyuelin.app.crawer.processor.FruitFuncProcessor;
import com.wangyuelin.app.crawer.processor.MonthFruitProcessor;
import com.wangyuelin.app.service.itf.ITest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ITest test;

    @Autowired
    private MonthFruitProcessor monthFruitProcessor;

    @Autowired
    private FruitFuncProcessor fruitFuncProcessor;


    @RequestMapping("/getOneUser")
    @ResponseBody
    public User getOneUser(){
        logger.info("getOneUser");
        return test.getUser();
    }


    @RequestMapping("/getAll")
    @ResponseBody
    public List<User> getAll(){
        logger.info("getOneUser");
        return test.getAll();
    }

    @RequestMapping("/start_crawler")
    public void startCrawler(){
        fruitFuncProcessor.start();

    }


}
