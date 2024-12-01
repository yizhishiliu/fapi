package com.shiliu.fapiinterface;

import com.shiliu.fapiclientsdk.client.FApiClient;
import com.shiliu.fapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FapiInterfaceApplicationTests {

    @Resource
    private FApiClient client;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUserName("shiliu");
        String result = client.helloByPost2(user);
        System.out.println(result);
    }

}
