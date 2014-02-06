package com.bbytes.config.spring;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.bbytes.config.client.CloudConfigurationSource;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;

public class CloudConfigPropertyInitializer implements InitializingBean, ResourceLoaderAware {

	final static Logger logger = LoggerFactory.getLogger(CloudConfigPropertyInitializer.class);

	private List<String> configList;

	private String host;

	private String port = "9000"; // default port

	private String project;

	private int pollDelay = 10; // 10 secs

	private ResourceLoader resourceLoader;

	@Override
	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

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

	public URL[] convertToURLs() throws IOException {
		URL[] urls = new URL[configList.size()];
		for (int i = 0; i < configList.size(); i++) {
			Resource resource = resourceLoader.getResource(configList.get(i));
			urls[i] = resource.getURL();
		}
		return urls;
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

		ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();

		// add all config files
		int index = 1;
		URL[] configList = convertToURLs();
		for (URL url : configList) {
			PropertiesConfiguration configFromPropertiesFile = new PropertiesConfiguration(url);
			finalConfig.addConfiguration(configFromPropertiesFile, "config-" + index);
			index++;
		}

		if (dynamicConfiguration != null)
			finalConfig.addConfigurationAtFront(dynamicConfiguration, "dynamicConfig");

		// install so that finalConfig becomes the source of dynamic properties
		DynamicPropertyFactory.initWithConfigurationSource(finalConfig);

	}

}
