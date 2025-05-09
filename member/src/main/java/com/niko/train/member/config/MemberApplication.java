package com.niko.train.member.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.niko")
@MapperScan("com.niko.train.member.mapper")
public class MemberApplication {
	private static final Logger Log = LogManager.getLogger(MemberApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MemberApplication.class);
		Environment env = app.run(args).getEnvironment();
		Log.info("启动成功");
		Log.info("地址: /http://127.0.0.1:{}{}/hello", env.getProperty("server.port"),
				env.getProperty("server.servlet.context-path"));
	}

}
