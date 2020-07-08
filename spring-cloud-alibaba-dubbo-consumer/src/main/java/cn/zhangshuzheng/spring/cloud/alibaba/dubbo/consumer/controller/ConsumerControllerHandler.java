package cn.zhangshuzheng.spring.cloud.alibaba.dubbo.consumer.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * sentinel拒绝/降级处理类
 * 指定blockHandlerClass后就不会再走controller内的拒绝方法
 */
public class ConsumerControllerHandler {

    //类中定义必须为static
    public static String rollbackHandler(BlockException ex) {
        ex.printStackTrace();
        return "拒绝类中定义的拒绝返回";
    }

    //类中定义必须为static
    public static String fallbackHandler(Throwable ex) {
        ex.printStackTrace();
        return "拒绝类中定义的异常处理返回";
    }

    /**
     * 默认异常处理方法，没有参数或一个异常参数
     * @param ex
     * @return
     */
    public static String defaultFallback(Throwable ex) {
        ex.printStackTrace();
        return "拒绝类中定义的默认异常处理返回";
    }


}
