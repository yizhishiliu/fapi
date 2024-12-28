package com.shiliu.fapigateway;

import com.shiliu.fapi.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDubbo
public class FapiGatewayApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(FapiGatewayApplication.class, args);
//    }

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FapiGatewayApplication.class, args);
        FapiGatewayApplication applicationContextBean = applicationContext.getBean(FapiGatewayApplication.class);
        String result = applicationContextBean.sayHello("world");
        System.out.println(result);
    }

    public String sayHello(String name) {
        return demoService.sayHello(name);
    }
}
