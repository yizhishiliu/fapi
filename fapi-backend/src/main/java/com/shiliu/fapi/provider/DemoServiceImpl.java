package com.shiliu.fapi.provider;


import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
