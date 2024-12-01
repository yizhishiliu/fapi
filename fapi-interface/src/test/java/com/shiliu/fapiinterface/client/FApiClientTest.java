package com.shiliu.fapiinterface.client;

import com.shiliu.fapiinterface.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@SpringBootTest
class FApiClientTest {

    @Test
    void helloByPost2() {
        FApiClient client = new FApiClient("shiliu", "aaAA1234");
        User user = new User();
        user.setUserName("shiliu");
        String result = client.helloByPost2(user);
        System.out.println(result);
    }
}