package com.ydh.faceplatform.controller;


import com.alibaba.fastjson.JSONObject;
import com.ydh.faceplatform.service.TopoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TopoController {

    public Logger logger = Logger.getLogger(TopoController.class);
    @Autowired
    private TopoService topoService;

    @RequestMapping(value = "/api/duplicateResult1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    String getDuplicateResult1(String requestBody) {
        logger.info("=======begin to get duplicated result=============");
        System.out.println("post result is:" + requestBody);

        return requestBody;
    }


    @RequestMapping(value = "/api/duplicateResult")
    public String getDuplicateResult(@RequestBody String requestBody, HttpServletRequest request) {
        logger.info("=======begin to get duplicated result=============");
        System.out.println("post result is:" + requestBody);
        return requestBody;
    }


    @RequestMapping(value = "/api/duplicateProgress")
    public String getDuplicateProgress(@RequestBody String requestBody, HttpServletRequest request) {
        logger.info("=======begin to get duplicated progress=============");
        System.out.println("progress is:" + requestBody);
        return requestBody;
    }

}
