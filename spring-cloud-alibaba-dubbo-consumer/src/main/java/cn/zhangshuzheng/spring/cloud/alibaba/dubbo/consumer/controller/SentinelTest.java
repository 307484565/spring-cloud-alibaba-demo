package cn.zhangshuzheng.spring.cloud.alibaba.dubbo.consumer.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

public class SentinelTest {

    /**
     * 1 使用Sentinel API 定义HelloWorld资源
     * @param args
     */
    public static void main(String[] args) {
        // 配置规则.
        initFlowRules();

        while (true) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性，自动 exit entry
            try (Entry entry = SphU.entry("HelloWorld", EntryType.IN, 1)) {
                // 被保护的逻辑
                System.out.println("hello world");
            } catch (BlockException e) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
            }
        }
    }

    /**
     * 使用Sentinel注解定义HelloWorld资源
     */
    @SentinelResource("HelloWorld")
    public void helloWorld() {
        // 资源中的逻辑
        System.out.println("hello world");
    }

    public static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("HelloWorld");
        // set limit qps
        rule.setCount(5);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
