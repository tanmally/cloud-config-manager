package com.bbytes.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bbytes.config.domain.CCProperty;
import com.bbytes.config.service.CloudConfigException;
import com.bbytes.config.service.IConfigReaderWriter;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	IConfigReaderWriter configReaderWriter;

	/**
	 * List all projects
	 * @return
	 * @throws CloudConfigException
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<String> listAllProjects() throws CloudConfigException {
		return configReaderWriter.listProjects();
	}
	
	/**
	 * List all environments for given project  
	 * @param project
	 * @return
	 * @throws CloudConfigException
	 */
	@RequestMapping("/{project}/environment")
	@ResponseBody
	public List<String> listProjectEnvironment(@PathVariable String project) throws CloudConfigException {
		return configReaderWriter.getProjectEnvironments(project);
	}
	
	/**
	 * Returns the project property list based on given environment 
	 * @param project
	 * @param environment
	 * @return
	 * @throws CloudConfigException
	 */
	@RequestMapping("/{project}/{environment}/property")
	@ResponseBody
	public List<CCProperty> listProjectEnvironmentProperties(@PathVariable String project,@PathVariable String environment) throws CloudConfigException {
		return configReaderWriter.getProjectEnvCCProperties(project, environment);
	}
	
	/**
	 * Returns the project property list based on active environment 
	 * @param project
	 * @return
	 * @throws CloudConfigException
	 */
	@RequestMapping("/{project}/property")
	@ResponseBody
	public List<CCProperty> listProjectProperties(@PathVariable String project) throws CloudConfigException {
		String environment = configReaderWriter.getActiveEnvironment(project);
		return configReaderWriter.getProjectEnvCCProperties(project, environment);
	}
	
	
	@ExceptionHandler(CloudConfigException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e) {
	    return e.getMessage();
	}

}