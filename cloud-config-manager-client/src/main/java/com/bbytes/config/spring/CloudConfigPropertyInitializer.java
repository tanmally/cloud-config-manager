package com.bbytes.config.spring;

import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bbytes.config.client.CloudConfigurationSource;
import com.netflix.config.ClasspathPropertiesConfiguration;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;

public class CloudConfigPropertyInitializer implements InitializingBean {

	final static Logger logger = LoggerFactory.getLogger(CloudConfigPropertyInitializer.class);
	
	private List<String> configList;

	private String host;

	private String port = "9000"; // default port

	private String project;

	private int pollDelay = 10; // 10 secs

	/**
	 * @return the configList
	 */
	public List<String> getConfigList() {
		return configList;
	}

	/**
	 * @param configList
	 *            the configList to set
	 */
	public void setConfigList(List<String> configList) {
		this.configList = configList;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the pollDelay
	 */
	public int getPollDelay() {
		return pollDelay;
	}

	/**
	 * @param pollDelay
	 *            the pollDelay to set
	 */
	public void setPollDelay(int pollDelay) {
		this.pollDelay = pollDelay;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Came in to Cloud Config Property Initializer");
		
		FixedDelayPollingScheduler scheduler = new FixedDelayPollingScheduler(0, getPollDelay() * 1000, true);

		CloudConfigurationSource cloudConfigurationSource = new CloudConfigurationSource(getHost(), getPort(),
				getProject());

		DynamicConfiguration dynamicConfiguration = null;
		try {
			dynamicConfiguration = new DynamicConfiguration(cloudConfigurationSource, scheduler);
		} catch (Exception exp) {
			logger.error("Not able to connect to cloud config server" + exp);
		}

		// init class path config files
		ClasspathPropertiesConfiguration.initialize();

		List<String> configFilePath = getConfigList();
		ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
		
		// add all config files
		if (configFilePath != null) {
			int index = 1;

			for (String filePath : configFilePath) {
				PropertiesConfiguration configFromPropertiesFile = new PropertiesConfiguration(filePath);
				finalConfig.addConfiguration(configFromPropertiesFile, "config-" + index);
				index++;
			}
		}

		if (dynamicConfiguration != null)
			finalConfig.addConfigurationAtFront(dynamicConfiguration, "dynamicConfig");
		
		// install so that finalConfig becomes the source of dynamic properties
		DynamicPropertyFactory.initWithConfigurationSource(finalConfig);

	}

	

}
