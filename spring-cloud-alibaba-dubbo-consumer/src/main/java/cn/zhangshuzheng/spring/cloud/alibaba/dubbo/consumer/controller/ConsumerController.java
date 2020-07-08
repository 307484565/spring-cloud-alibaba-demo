package cn.zhangshuzheng.spring.cloud.alibaba.dubbo.consumer.controller;

import cn.zhangshuzheng.spring.cloud.alibaba.dubbo.api.DubboApi;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * consumer
 *
 * @author ZhangShuzheng
 * @date 2019/5/8
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private RestTemplate restTemplate = new RestTemplate();

    @Reference(check = false)
    private DubboApi dubboApi;

    @GetMapping("/demo")
    public Object demo() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("spring-cloud-alibaba-dubbo-provider");
        String url = String.format("http://%s:%s/provider/demo", serviceInstance.getHost(), serviceInstance.getPort());
        System.out.println("request url:" + url);
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/dubbo")
    @SentinelResource(value = "dubbo", blockHandler = "rollbackHandler", blockHandlerClass = ConsumerControllerHandler.class)
    public String dubbo() {
        return dubboApi.demo();
    }

    /**
     * exceptionsToIgnore 用于指定哪些异常被排除掉，不会计入异常统计中，也不会进入 fallback 逻辑中，而是会原样抛出。
     * @return
     */
    @GetMapping("/fallback")
    @SentinelResource (fallback = "fallbackHandler", fallbackClass = ConsumerControllerHandler.class, exceptionsToIgnore=Exception.class)
    public String fallbackTest() {
        throw new RuntimeException();
    }

    @GetMapping("/fallback/default")
    @SentinelResource (defaultFallback = "defaultFallback", fallbackClass = ConsumerControllerHandler.class)
    public String defaultFallbackTest() {
        throw new RuntimeException();
    }

    /**
     * Block 拒绝处理函数，参数最后多一个 BlockException，其余与原函数一致.
     * 指定blockHandlerClass后不再走本方法
     * @param ex
     * @return
     */
    public String rollbackHandler(BlockException ex) {
        ex.printStackTrace();
        return "Controller中定义拒绝返回";
    }

    /**
     * fallback 异常处理函数，参数最后多一个 Throwable, 异常可以是任意类型或不加异常参数，其余与原函数一致.
     * sentinel1.6版本之前只能针对抛出DegradeException异常处理，其余异常不走此方法
     * 指定fallbackClass后不再走本方法
     * @param e
     * @return
     */
    public String fallbackHandler(Throwable e) {
        e.printStackTrace();
        return "Controller中定义异常降级处理";
    }


}
