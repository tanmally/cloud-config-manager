package com.bbytes.config.spring;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.netflix.config.DynamicPropertyFactory;

public class CloudConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
		return DynamicPropertyFactory.getInstance().getStringProperty(placeholder, "null").get();
	}

}
