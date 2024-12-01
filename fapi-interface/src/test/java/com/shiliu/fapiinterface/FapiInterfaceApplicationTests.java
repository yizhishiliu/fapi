package com.shiliu.fapiinterface;

import com.shiliu.fapiclientsdk.client.FApiClient;
import com.shiliu.fapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FapiInterfaceApplicationTests {

    @Test
    void contextLoads() {
        FApiClient client = new FApiClient("shiliu", "aaAA1234");
        User user = new User();
        user.setUserName("shiliu");
        String result = client.helloByPost2(user);
        System.out.println(result);
    }

}
