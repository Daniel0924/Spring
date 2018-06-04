package com.ydh.faceplatform.service;

import com.ydh.faceplatform.bean.User;

import java.util.HashMap;
import java.util.List;

/**
 *  封装业务接口
 */
public interface UserService {

    List<HashMap> findByName();
}
