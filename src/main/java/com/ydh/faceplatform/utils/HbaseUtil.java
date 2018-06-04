package com.ydh.faceplatform.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HbaseUtil {

    /**
     * 根据表名，一级列，二级列，返回hbase数据列表
     *
     * @param tableName
     * @param family
     * @param qualifier
     * @return
     * @throws Exception
     */
    public static List<String> getHbase(String tableName, String family, String qualifier) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        byte[] cf = Bytes.toBytes(family);
        byte[] qf = Bytes.toBytes(qualifier);
        List res = new ArrayList();
        //从Hbase中获得数据
        HTable table = new HTable(conf, tableName);
        Scan s = new Scan();
        ResultScanner scanner = table.getScanner(s);
        for (Result result : scanner) {
            byte[] val = null;
            if (result.containsColumn(cf, qf)) {
                val = result.getValue(cf, qf);
                res.add(Bytes.toString(val));
            }
        }
        return res;
    }

    public static Map<String, String> getHbaseMap(String tableName, String family, String qualifier) {
        Configuration config = HBaseConfiguration.create();

        Map<String, String> map = new HashMap<>();

        try {
            HTable table = new HTable(config, tableName);
            Scan s = new Scan();
            s.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));

            ResultScanner scanner = table.getScanner(s);
            for (Result r : scanner) {
                for (KeyValue kv : r.raw()) {
                    map.put(new String(kv.getRow()), new String(kv.getValue()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * public static List<String> getMultiHbase() throws Exception {
     * Configuration conf = HBaseConfiguration.create();
     * HTable table = new HTable(conf, "TEST2_43");
     * byte[] row = Bytes.toBytes("TEST2_43");
     * byte[] family = Bytes.toBytes("info");
     * byte[][] qualifiers = {Bytes.toBytes("image0"),//存照片
     * Bytes.toBytes("imageFea0"),//存照片特征值
     * Bytes.toBytes("name0"),//姓名
     * Bytes.toBytes("gender0"),//性别
     * Bytes.toBytes("country0"),//国籍
     * Bytes.toBytes("idType0"),//证件类型
     * Bytes.toBytes("nation0"),//民族
     * Bytes.toBytes("birthday0")};//生日
     * List<Get> gets = new ArrayList<Get>();
     * for (byte[] simple : qualifiers) {
     * Get get = new Get(row);
     * get.addColumn(family, simple);
     * gets.add(get);
     * }
     * <p>
     * }
     **/

    public static void addRecord(String tableName, String rowKey,
                                 String family, String qualifier, String value) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        try {
            HTable table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
                    Bytes.toBytes(value));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {


        List<String> list = null;
        try {
            list = getHbase("pictureTest", "information", "pictureCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String s : list) {
            System.out.println(s);
        }

        Map map = new HashMap();
        map.put("123", 123);
        map.put("124", 124);


    }
}
