package com.shiliu.fapiclientsdk;

import com.shiliu.fapiclientsdk.client.FApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Configuration
@ConfigurationProperties(prefix = "fapi.client")
@Data
@ComponentScan
public class FApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public FApiClient fApiClient() {
        // 通过accessKey和secretKey创建FApiClient实例
        return new FApiClient(accessKey, secretKey);
    }
}
