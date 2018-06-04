package com.ydh.faceplatform.topology;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import org.apache.log4j.Logger;


public class InsertPhotoBolt extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(InsertPhotoBolt.class.getName());

    public void execute(Tuple input, BasicOutputCollector collector) {
        // TODO Auto-generated method stub
        String json = input.getStringByField("jsonArray");

        System.out.println(json);
    }

    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
    }
}
