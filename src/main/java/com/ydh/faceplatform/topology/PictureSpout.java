package com.ydh.faceplatform.topology;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PictureSpout extends BaseRichSpout {
    public SpoutOutputCollector collector;
    private String jsonString;
    private int pcTotal = 0;
    private int targetNum = 0;

    private List keyList;
    private List valueList;

    public PictureSpout(String jsonString, int sourceNum, int targetNum) {
        this.jsonString = jsonString;
        this.pcTotal = sourceNum;
        this.targetNum = targetNum;
    }

    public PictureSpout() {
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("key", "value"));
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        Configuration config = HBaseConfiguration.create();

        keyList = new ArrayList<>();
        valueList = new ArrayList<>();
        try {
            HTable table = new HTable(config, "TEST_43");
            Scan s = new Scan();
            s.addColumn(Bytes.toBytes("info"), Bytes.toBytes("imageFea0"));
            ResultScanner scanner = table.getScanner(s);

            for (Result r : scanner) {
                for (KeyValue kv : r.raw()) {
                    keyList.add(new String(kv.getRow()));
                    valueList.add(new String(kv.getValue()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.collector = collector;
    }

    @Override
    public void nextTuple() {

        if (keyList != null && keyList.size() != 0) {
            collector.emit(new Values(keyList.remove(0), valueList.remove(0)));
        }

        //Utils.sleep(100);
    }
}
