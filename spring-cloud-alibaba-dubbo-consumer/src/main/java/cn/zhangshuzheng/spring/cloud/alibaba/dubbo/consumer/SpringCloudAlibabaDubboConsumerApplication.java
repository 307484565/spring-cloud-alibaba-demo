package cn.zhangshuzheng.spring.cloud.alibaba.dubbo.consumer;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringCloudAlibabaDubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaDubboConsumerApplication.class, args);
        initFlowQpsRule();
    }

    /**
     * 硬编码方式设置 流量控制规则
     */
    private static void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("dubbo");
        // set limit qps to 1
        rule.setCount(1);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
