package com.bbytes.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication("classpath:/META-INF/app-context.xml",
				ConfigManagerSpringConfiguration.class);
		app.setShowBanner(false);
		app.setLogStartupInfo(false);
		app.run(args);
	}
}
