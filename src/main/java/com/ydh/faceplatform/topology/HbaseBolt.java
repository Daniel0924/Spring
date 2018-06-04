package com.ydh.faceplatform.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ydh.faceplatform.utils.HbaseUtil;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;


public class HbaseBolt extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(HbaseBolt.class.getName());

    private Map<String, String> map;

    //连接hbase
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        try {
            map = HbaseUtil.getHbaseMap("TEST2_43", "info", "imageFea0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("HbaseBolt!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }


    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String rowKey = tuple.getStringByField("key");
        String value = tuple.getStringByField("value");

        JSONArray jsonArray = new JSONArray();
        JSONObject sourceJson = new JSONObject();

        sourceJson.put("pcId", "100");
        sourceJson.put("s_pbId", "TEST_43");
        sourceJson.put("s_rowkey", rowKey);
        sourceJson.put("s_imageId", "image0");
        jsonArray.add(sourceJson);

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String targetValue = entry.getValue();
            if (targetValue.equals(value)) {
                String targetKey = entry.getKey();
                JSONObject targetJson = new JSONObject();
                targetJson.put("pbid_result", "TEST2_43");
                targetJson.put("rowkey_result", targetKey);
                targetJson.put("imagekey", "image0");
                targetJson.put("similarity", "0.99");
                jsonArray.add(targetJson);
            }
        }
        collector.emit(new Values(jsonArray.toString()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("jsonArray"));
    }

}
