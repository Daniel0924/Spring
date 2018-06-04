package com.ydh.faceplatform.service.impl;

import com.ydh.faceplatform.bean.User;
import com.ydh.faceplatform.mapper.UserMapper;
import com.ydh.faceplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public List<HashMap> findByName() {

        return mapper.findOne();
    }
}
