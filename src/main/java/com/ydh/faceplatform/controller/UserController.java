package com.ydh.faceplatform.controller;

import com.ydh.faceplatform.bean.User;
import com.ydh.faceplatform.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 调用service的接口，实现相应功能
 */

@RestController
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserById")
    @ResponseBody
    public Object add() {
        return userService.findByName();
    }
}