/**
 * E5Projects#org.reed/ManualNacosConfig.java
 */
package org.reed;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.reed.utils.HttpUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

/**
 * @author chenxiwen
 * @date 2021-01-28 15:27:12
 */
public class ManualNacosConfig {
    private static final String NACOS_ADDRESS = "http://11.11.54.102:10001/nacos";
    private static final String CONFIG_PATH = "/v1/cs/configs";


    public static void main(String[] args) {
        try {
            HttpURLConnection normalHttpConnection = HttpUtil.getNormalHttpConnection(NACOS_ADDRESS + CONFIG_PATH
                    + "?show=all&dataId=environment.yml&group=zhbg&tenant=dev&namespaceId=dev", 5000);
            byte[] bytes = HttpUtil.readHttpURLConnection(normalHttpConnection);
            String configStr = new String(bytes, StandardCharsets.UTF_8);
            JSONObject config = JSON.parseObject(configStr);
            String content = config.getString("content");
            Map configMap = new Yaml().load(content);
//            System.out.println(MapUtil.toString(configMap));
            configMap.forEach((k,v)->{System.out.println(k+"@@@"+v);});
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arr = new String[]{"aaa", "bbb", "ccc"};
        Random random = new Random(arr.length);
        for (int i = 0; i < 100; i++) {
            System.out.println(arr[random.nextInt(arr.length)]);
        }
    }

}
