package com.ydh.faceplatform.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.alibaba.fastjson.JSONArray;

import java.util.*;

public class FaceTopo {

    public static void main(String[] args) throws Exception {

        localModel();
        //submitModel();
    }


    public static String initString() {
        Map<String, List<String>> jsonString = new HashMap<>();
        jsonString.put("pcId", new ArrayList<>(Arrays.asList("12", "13")));
        jsonString.put("pcSourcePbId", new ArrayList<>(Arrays.asList("China")));
        jsonString.put("pcSourceCountry", new ArrayList<>(Arrays.asList("Han")));
        jsonString.put("pcSourceNation", new ArrayList<>(Arrays.asList("ID")));
        jsonString.put("pcSourceIdType", new ArrayList<>(Arrays.asList("ID")));
        jsonString.put("pcSourceAgeRange", new ArrayList<>(Arrays.asList("30")));
        jsonString.put("pcSourceGender", new ArrayList<>(Arrays.asList("Man")));
        jsonString.put("pcTargetPbId", new ArrayList<>(Arrays.asList("13")));
        jsonString.put("pcTargetCountry", new ArrayList<>(Arrays.asList("China")));
        jsonString.put("pcTargetNation", new ArrayList<>(Arrays.asList("Han")));
        jsonString.put("pcTargetIdType", new ArrayList<>(Arrays.asList("ID")));
        jsonString.put("pcTargetAgeRange", new ArrayList<>(Arrays.asList("40")));
        jsonString.put("pcTargetGender", new ArrayList<>(Arrays.asList("Man")));
        jsonString.put("pcSimilarity", new ArrayList<>(Arrays.asList("0.9")));
        return JSONArray.toJSONString(jsonString);

    }

    /**
     * 开启storm任务，并将builder返回给remoteSubmit，让其可以用http post请求发送到控制器去
     *
     * @param jsonString
     * @param sourceNum
     * @param targetNum
     * @param spoutTaskNum
     * @param boltTaskNum
     * @return
     */
    public static TopologyBuilder buildDuplicateCheckTopology(String jsonString,
                                                              int sourceNum, int targetNum, int spoutTaskNum,
                                                              int boltTaskNum) {

        TopologyBuilder builder = new TopologyBuilder();
        PictureSpout spout = new PictureSpout(jsonString, sourceNum, targetNum);
        HbaseBolt hbaseBolt = new HbaseBolt();
        InsertPhotoBolt photoBolt = new InsertPhotoBolt();

        builder.setSpout("PictureSpout", spout);
        builder.setBolt("HbaseBolt", hbaseBolt).fieldsGrouping("PictureSpout", new Fields("picture"));
        builder.setBolt("InsertPhotoBolt", photoBolt).fieldsGrouping("HbaseBolt", new Fields("code"));
        return builder;
    }

    /**
     * 集群运行
     *
     * @throws AlreadyAliveException
     * @throws InvalidTopologyException
     * @throws AuthorizationException
     */
    public static void submitModel() throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        TopologyBuilder builder = new TopologyBuilder();
        PictureSpout spout = new PictureSpout();
        HbaseBolt hbaseBolt = new HbaseBolt();
        InsertPhotoBolt photoBolt = new InsertPhotoBolt();

        builder.setSpout("PictureSpout", spout);
        builder.setBolt("HbaseBolt", hbaseBolt).fieldsGrouping("PictureSpout", new Fields("key", "value"));
        builder.setBolt("InsertPhotoBolt", photoBolt).fieldsGrouping("HbaseBolt", new Fields("jsonArray"));
        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);

        Map<String, Object> stormConf = Utils.readStormConfig();//读取本地的storm.yaml配置文件，否则使用storm-core-xxx.jar中默认的default.yaml
        //System.out.println(stormConf.toString());
        //System.out.println(stormConf.get("storm.local.dir").getClass().getName());
        stormConf.putAll(conf);
        String topologyName = "duplicateTopo";//设置拓扑名

        String inputJar = "D:\\netease\\Spring\\target\\spring.jar";//需要上传到集群的jar包路径
        System.setProperty("storm.jar", inputJar);//设置需要上传到集群的jar包路径
        StormSubmitter.submitTopology(topologyName, stormConf, builder.createTopology());
    }


    public static void localModel() {
        TopologyBuilder builder = new TopologyBuilder();
        PictureSpout spout = new PictureSpout();
        HbaseBolt hbaseBolt = new HbaseBolt();
        InsertPhotoBolt photoBolt = new InsertPhotoBolt();

        builder.setSpout("PictureSpout", spout);
        builder.setBolt("HbaseBolt", hbaseBolt).fieldsGrouping("PictureSpout", new Fields("key", "value"));
        builder.setBolt("InsertPhotoBolt", photoBolt).fieldsGrouping("HbaseBolt", new Fields("jsonArray"));

        Config config = new Config();
        config.setDebug(true);
        System.out.println("1");
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("faceCompare", config, builder.createTopology());

        Utils.sleep(100 * 1000);
        cluster.killTopology("faceCompare");
        cluster.shutdown();
    }


}
