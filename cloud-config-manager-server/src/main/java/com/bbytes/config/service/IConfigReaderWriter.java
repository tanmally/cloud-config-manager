package com.bbytes.config.service;

import java.util.List;

import com.bbytes.config.domain.CCProperty;

public interface IConfigReaderWriter {

	public static final String PROJECT_LIST = "CC_PROJECT_LIST";

	public Object getPropertyValue(String propertyName, String project,
			String environment) throws CloudConfigException;

	public void saveProperty(String propertyName, Object propertyValue,
			String project, String environment) throws CloudConfigException;

	public Object getPropertyValue(String propertyName, String project)
			throws CloudConfigException;

	public void saveCCProperty(CCProperty property, String project,
			String environment) throws CloudConfigException;

	public CCProperty getCCProperty(String propertyName, String project,
			String environment) throws CloudConfigException;

	public void saveProperty(String propertyName, Object propertyValue,
			String project) throws CloudConfigException;

	public void saveCCProperty(CCProperty property, String project)
			throws CloudConfigException;

	public CCProperty getCCProperty(String propertyName, String project)
			throws CloudConfigException;

	public List<CCProperty> getProjectEnvCCProperties(String project,String environment)
			throws CloudConfigException;

	public void setActiveEnvironment(String project, String environment)
			throws CloudConfigException;

	public String getActiveEnvironment(String project)
			throws CloudConfigException;

	public void createProject(String project);

	public boolean isProjectExist(String project);

	public boolean isEnvironmentExist(String project, String environment);

	public List<String> listProjects() throws CloudConfigException;

	public void addEnvironmentToProject(String project, String environment)
			throws CloudConfigException;

	public List<String> getProjectEnvironments(String project)
			throws CloudConfigException;
}
