package com.shiliu.fapiinterface.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.shiliu.fapiinterface.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * 调用第三方接口的客户端
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public class FApiClient {

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
        return HttpRequest.post("http://127.0.0.1:8123/api/hello/user").body(json).execute().body();
    }
}
