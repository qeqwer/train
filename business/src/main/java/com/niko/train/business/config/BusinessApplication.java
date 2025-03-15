package com.niko.train.business.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.niko")
@MapperScan("com.niko.train.*.mapper")
@EnableFeignClients("com.niko.train.business.feign")
public class BusinessApplication {
	private static final Logger Log = LogManager.getLogger(BusinessApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BusinessApplication.class);
		Environment env = app.run(args).getEnvironment();
		Log.info("启动成功");
		Log.info("地址: /http://127.0.0.1:{}{}/hello", env.getProperty("server.port"),
				env.getProperty("server.servlet.context-path"));

//		initFlowRules();
//		Log.info("已定义限流规则");
	}

	private static void initFlowRules() {
		List<FlowRule> rules =  new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource("doConfirm");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//set limit QPS to 20 一秒内只能有20个请求进来
		rule.setCount(20);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}

}
