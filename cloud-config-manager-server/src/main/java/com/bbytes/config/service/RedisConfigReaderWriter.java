package com.bbytes.config.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import com.bbytes.config.domain.CCProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RedisConfigReaderWriter implements IConfigReaderWriter {

	@Resource(name = "redisTemplate")
	private ListOperations<String, String> propertyLevelOps;

	@Resource(name = "redisTemplate")
	private ListOperations<String, List<String>> projectLevelOps;

	private ObjectMapper jsonMapper;

	public RedisConfigReaderWriter() {
		jsonMapper = new ObjectMapper();
		jsonMapper.setSerializationInclusion(Include.NON_NULL);
	}

	@Override
	public void saveCCProperty(CCProperty property, String project, String environment) throws CloudConfigException {

		if (property == null)
			return;

		checkProjectAndEnvironmentExist(project, environment);

		String ccPropertyJson = null;
		try {
			ccPropertyJson = jsonMapper.writeValueAsString(property);
			propertyLevelOps.leftPush(project + ":" + environment + ":" + property.getPropertyName(), ccPropertyJson);

			// add it to proj:env combo
			List<CCProperty> ccProperties = getProjectEnvCCProperties(project, environment);
			ccProperties.add(property);
			projectLevelOps.leftPush(project + ":" + environment, convertProperty(ccProperties));

		} catch (JsonProcessingException e) {
			throw new CloudConfigException(e);
		}

	}

	@Override
	public void deleteCCProperty(CCProperty property, String project, String environment) throws CloudConfigException {

		if (property == null)
			return;

		checkProjectAndEnvironmentExist(project, environment);

		// remove from prop level ops
		propertyLevelOps.leftPop(project + ":" + environment + ":" + property.getPropertyName());

		// remove it from proj:env combo
		List<CCProperty> ccProperties = getProjectEnvCCProperties(project, environment);
		List<CCProperty> ccPropertiesToBeUpdated = new ArrayList<CCProperty>();
		for (Iterator<CCProperty> iterator = ccProperties.iterator(); iterator.hasNext();) {
			CCProperty ccProperty = (CCProperty) iterator.next();
			if (!ccProperty.getPropertyName().equals(property.getPropertyName())) {
				ccPropertiesToBeUpdated.add(ccProperty);
			}

		}
		// remove from project level ops
		// remove old collection
		projectLevelOps.leftPop(project + ":" + environment);
		// update new collection
		projectLevelOps.leftPush(project + ":" + environment, convertProperty(ccPropertiesToBeUpdated));
	}

	@Override
	public CCProperty getCCProperty(String propertyName, String project, String environment)
			throws CloudConfigException {
		List<String> result = propertyLevelOps.range(project + ":" + environment + ":" + propertyName, 0, 0);
		if (result == null || result.size() == 0 || result.get(0) == null)
			return null;

		String ccPropertyJson = result.get(0);

		CCProperty property = null;
		try {
			property = jsonMapper.readValue(ccPropertyJson, CCProperty.class);
		} catch (IOException e) {
			throw new CloudConfigException(e);
		}
		return property;
	}

	@Override
	public Object getPropertyValue(String propertyName, String project, String environment) throws CloudConfigException {
		CCProperty property = getCCProperty(propertyName, project, environment);
		if (property == null)
			return null;

		return property.getPropertyValue();
	}

	@Override
	public void saveProperty(String propertyName, Object propertyValue, String project, String environment)
			throws CloudConfigException {

		CCProperty property = getCCProperty(propertyName, project, environment);

		if (property == null) {
			property = new CCProperty();
			property.setPropertyName(propertyName);
			property.setCreationDate(new Date());
		}

		property.setUpdateDate(new Date());
		property.setPropertyValue(propertyValue);
		saveCCProperty(property, project, environment);
	}

	@Override
	public void deleteProperty(String propertyName, String project, String environment)
			throws CloudConfigException {
		CCProperty property = getCCProperty(propertyName, project, environment);
		if (property != null)
			deleteCCProperty(property, project, environment);

	}

	@Override
	public Object getPropertyValue(String propertyName, String project) throws CloudConfigException {
		String environment = getActiveEnvironment(project);
		return getPropertyValue(propertyName, project, environment);
	}

	@Override
	public void saveProperty(String propertyName, Object propertyValue, String project) throws CloudConfigException {
		String environment = getActiveEnvironment(project);
		saveProperty(propertyName, propertyValue, project, environment);
	}

	@Override
	public void deleteProperty(String propertyName, String project) throws CloudConfigException {
		String environment = getActiveEnvironment(project);
		deleteProperty(propertyName, project, environment);

	}

	@Override
	public void saveCCProperty(CCProperty property, String project) throws CloudConfigException {
		String environment = getActiveEnvironment(project);
		saveCCProperty(property, project, environment);
	}

	@Override
	public CCProperty getCCProperty(String propertyName, String project) throws CloudConfigException {
		String environment = getActiveEnvironment(project);
		return getCCProperty(propertyName, project, environment);
	}

	@Override
	public List<CCProperty> getProjectEnvCCProperties(String project, String environment) throws CloudConfigException {

		checkProjectAndEnvironmentExist(project, environment);

		List<List<String>> result = projectLevelOps.range(project + ":" + environment, 0, 0);

		if (result == null || result.size() == 0)
			return new ArrayList<CCProperty>();

		return convertString(result.get(0));
	}

	@Override
	public void setActiveEnvironment(String project, String environment) throws CloudConfigException {
		checkProjectExist(project);

		propertyLevelOps.leftPush("PROJECT:" + project, environment);
	}

	@Override
	public String getActiveEnvironment(String project) throws CloudConfigException {
		checkProjectExist(project);

		List<String> result = propertyLevelOps.range("PROJECT:" + project, 0, 0);
		if (result == null || result.size() == 0)
			return null;

		return result.get(0);

	}

	@Override
	public void createProject(String project) {
		List<String> projectList = listProjects();
		if (projectList == null) {
			projectList = new ArrayList<>();
		}

		if (!projectList.contains(project)) {
			projectList.add(project);
		}

		projectLevelOps.leftPush(IConfigReaderWriter.PROJECT_LIST, projectList);
	}

	@Override
	public boolean isProjectExist(String project) {
		List<String> projectList = listProjects();
		if (projectList == null) {
			return false;
		}

		return projectList.contains(project);

	}

	@Override
	public void addEnvironmentToProject(String project, String environment) throws CloudConfigException {

		checkProjectExist(project);

		List<String> environmentList = getProjectEnvironments(project);
		if (environmentList == null) {
			environmentList = new ArrayList<>();
			setActiveEnvironment(project, environment);
		}

		if (!environmentList.contains(environment)) {
			environmentList.add(environment);
		}

		projectLevelOps.leftPush(project, environmentList);
	}

	@Override
	public List<String> getProjectEnvironments(String project) throws CloudConfigException {

		checkProjectExist(project);

		List<List<String>> result = projectLevelOps.range(project, 0, 0);
		if (result == null || result.size() == 0)
			return new ArrayList<String>();;

		return result.get(0);
	}

	@Override
	public boolean isEnvironmentExist(String project, String environment) {
		List<String> envList;
		try {
			envList = getProjectEnvironments(project);
		} catch (CloudConfigException e) {
			return false;
		}

		if (envList == null) {
			return false;
		}

		return envList.contains(environment);
	}

	@Override
	public List<String> listProjects() {
		List<List<String>> result = projectLevelOps.range(IConfigReaderWriter.PROJECT_LIST, 0, 0);
		if (result == null || result.size() == 0)
			return new ArrayList<>();

		return result.get(0);

	}

	private void checkProjectExist(String project) throws CloudConfigException {
		if (!isProjectExist(project)) {
			throw new CloudConfigException("No Project with name " + project + " found");
		}
	}

	private void checkProjectAndEnvironmentExist(String project, String environment) throws CloudConfigException {
		if (!isProjectExist(project)) {
			throw new CloudConfigException("No Project with name " + project + " found");
		}

		if (!isEnvironmentExist(project, environment)) {
			throw new CloudConfigException("No Environment with name " + environment + " found for project " + project);
		}
	}

	private CCProperty convertString(String ccPropertyJson) throws CloudConfigException {
		try {
			return jsonMapper.readValue(ccPropertyJson, CCProperty.class);
		} catch (IOException e) {
			throw new CloudConfigException(e);
		}
	}

	private List<CCProperty> convertString(List<String> ccPropertyJsonList) throws CloudConfigException {

		if (ccPropertyJsonList == null)
			return new ArrayList<CCProperty>();

		List<CCProperty> ccProperties = new ArrayList<CCProperty>();
		for (String stringJson : ccPropertyJsonList) {
			ccProperties.add(convertString(stringJson));
		}

		return ccProperties;
	}

	private String convertProperty(CCProperty ccProperty) throws CloudConfigException {
		try {
			return jsonMapper.writeValueAsString(ccProperty);
		} catch (JsonProcessingException e) {
			throw new CloudConfigException(e);
		}

	}

	private List<String> convertProperty(List<CCProperty> ccProperties) throws CloudConfigException {

		if (ccProperties == null)
			return new ArrayList<String>();

		List<String> ccStringProperties = new ArrayList<String>();
		for (CCProperty ccProperty : ccProperties) {
			ccStringProperties.add(convertProperty(ccProperty));
		}

		return ccStringProperties;
	}

}
