package com.shiliu.fapiinterface.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.shiliu.fapiinterface.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import static com.shiliu.fapiinterface.utils.SignUtil.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public class FApiClient {

    private String accessKey;

    private String secretKey;

    public FApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String helloByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get("http://127.0.0.1:8123/api/hello/", paramMap);
    }

    public String helloByPost(@RequestParam String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post("http://127.0.0.1:8123/api/hello/", paramMap);
    }

    public String helloByPost2(@RequestBody User user) {
        String json = JSONUtil.toJsonStr(user);
        return HttpRequest
                .post("http://127.0.0.1:8123/api/hello/user")
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
        headers.put("once", RandomUtil.randomNumbers(100));
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headers.put("sign", genSign(headers.toString(), secretKey));
        return headers;
    }
}
