package com.shiliu.fapigateway.filter;

import com.shiliu.fapiclientsdk.utils.SignUtil;
import com.shiliu.fapicommon.model.entity.InterfaceInfo;
import com.shiliu.fapicommon.model.entity.User;
import com.shiliu.fapicommon.service.InnerInterfaceInfoService;
import com.shiliu.fapicommon.service.InnerUserInterfaceInfoService;
import com.shiliu.fapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    // ip白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://127.0.0.1:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        HttpHeaders headers = request.getHeaders();
        String sourceAddress = request.getRemoteAddress().getAddress().getHostAddress();
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求路径：{}", path);
        log.info("请求方法：{}", method);
        log.info("请求参数：{}", request.getQueryParams());
        log.info("请求头：{}", headers);
        log.info("请求来源地址：{}", sourceAddress);

        // 2. 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }

        // 3. 用户鉴权（判断ak，sk是否合法）
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        // 验证nonce是否已使用
        // 其他方案：redis存储使用过的nonce，验证是否已使用(这里简单校验一下小于4位数）
        assert nonce != null;
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 验证timestamp是否过期（不超过当前时间5分钟）
        assert timestamp != null;
        if (Long.parseLong(timestamp) > System.currentTimeMillis() + 5 * 60 * 1000) {
            return handleNoAuth(response);
        }

        // 验证accessKey是否已分配
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        // 从数据库中查询accessKey对应的secretKey
        String secretKey = invokeUser.getSecretKey();
        if (secretKey == null) {
            return handleNoAuth(response);
        }
        // 验证sign是否正确
        String genSign = SignUtil.genSign(body, secretKey);
        if (sign == null || !sign.equals(genSign)) {
            return handleNoAuth(response);
        }

        // 4. 请求的模拟接口是否存在
        // 查询数据库，验证请求的接口是否存在
        InterfaceInfo serviceInterfaceInfo = null;
        try {
            serviceInterfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (serviceInterfaceInfo == null) {
            return handleNoAuth(response);
        }

        // 6. 响应日志
        return handleResponse(exchange, chain, serviceInterfaceInfo.getId(), invokeUser.getId());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long invokeUserId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法，就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {

                                        // 5. 请求转发，调用模拟接口，调用成功，接口调用次数+1
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, invokeUserId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }

                                        // 读取响应体的内容并转换为字节数组
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        //释放掉内存
                                        DataBufferUtils.release(dataBuffer);
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8);
                                        sb2.append(data);
                                        // 记录日志
                                        log.info("响应结果：{}", sb2);
                                        // 将处理后的内容重新包装成DataBuffer并返回
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("响应code异常：{}", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("网关处理响应异常：" + e);
            return chain.filter(exchange);
        }
    }


    /**
     * 非法请求处理
     *
     * @param response
     * @return
     */
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        log.info("非法请求");
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 调用失败处理
     *
     * @param response
     * @return
     */
    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        log.info("调用失败");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}