package com.bbytes.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ConfigManagerSpringConfiguration {

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		 TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		    factory.setPort(9000);
		    factory.setSessionTimeout(10, TimeUnit.MINUTES);
		    return factory;
	}
}
