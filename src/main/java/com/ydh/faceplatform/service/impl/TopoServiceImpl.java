package com.ydh.faceplatform.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ydh.faceplatform.service.TopoService;
import com.ydh.faceplatform.topology.FaceTopo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopoServiceImpl implements TopoService {

    @Override
    public JSONObject getDuplicateResult() {
        FaceTopo.localModel();


        return new JSONObject();
    }


}
