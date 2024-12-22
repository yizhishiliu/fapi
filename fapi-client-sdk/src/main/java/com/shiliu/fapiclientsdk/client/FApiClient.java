package com.shiliu.fapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.shiliu.fapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.shiliu.fapiclientsdk.utils.SignUtil.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public class FApiClient {

    private static final String GATEWAY_URL = "http://127.0.0.1:8090";

    private String accessKey;

    private String secretKey;

    public FApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String helloByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(GATEWAY_URL + "/api/hello/", paramMap);
    }

    public String helloByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(GATEWAY_URL + "/api/hello/", paramMap);
    }

    public String helloByPost2(User user) {
        String json = JSONUtil.toJsonStr(user);
        return HttpRequest
                .post(GATEWAY_URL + "/api/hello/user")
                .addHeaders(getHeadersMap(json))
                .body(json)
                .execute()
                .body();
    }

    private Map<String, String> getHeadersMap(String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accessKey", accessKey);
        // 一定不能直接发送密钥
//        headers.put("secretKey", secretKey);
        headers.put("body", body);
        // 随机数once，防止重放攻击
        headers.put("nonce", RandomUtil.randomNumbers(4));
        // 当前时间戳，用于防止请求被篡改
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 签名
        headers.put("sign", genSign(body, secretKey));
        return headers;
    }
}
