package com.bbytes.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bbytes.config.service.CloudConfigException;
import com.bbytes.config.service.IConfigReaderWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class PropertyToRedisStoreAndRetrieveTests {

	@Autowired
	IConfigReaderWriter configReaderWriter;

	@Test
	public void stringStoreAndLoad() throws CloudConfigException {
		configReaderWriter.saveProperty("testProp", "testVal", "testprojec",
				"prod");
		configReaderWriter.setActiveEnvironment("testprojec", "prod");

		String val = (String) configReaderWriter.getPropertyValue("testProp",
				"testprojec");
		Assert.assertEquals("testVal", val);
		
		Assert.assertTrue(configReaderWriter.getProjectEnvCCProperties("testprojec", "prod").size() > 0 );

	}

	@Test
	public void intStoreAndLoad() throws CloudConfigException {
		configReaderWriter.createProject("testprojec");
		configReaderWriter.addEnvironmentToProject("testprojec", "prod");
		
		configReaderWriter.saveProperty("testProp", new Integer(9), "testprojec", "prod");
		configReaderWriter.setActiveEnvironment("testprojec", "prod");
		Integer val = (Integer) configReaderWriter.getPropertyValue("testProp",
				"testprojec");
		Assert.assertEquals(9, val.intValue());

	}
	
	@Test
	public void deletetest() throws CloudConfigException {
		configReaderWriter.createProject("testprojec");
		configReaderWriter.addEnvironmentToProject("testprojec", "prod");
		
		configReaderWriter.saveProperty("testProp", new Integer(9), "testprojec", "prod");
		configReaderWriter.setActiveEnvironment("testprojec", "prod");
		Integer val = (Integer) configReaderWriter.getPropertyValue("testProp",
				"testprojec");
		Assert.assertEquals(9, val.intValue());
		
		configReaderWriter.deleteProperty("testProp", "testprojec", "prod");
		Object testProp = configReaderWriter.getPropertyValue("testProp",
				"testprojec");
		
		Assert.assertNull(testProp);
	}
	
	@Test
	public void intStoreAndLoadWithoutActiveEnvironmentSettings() throws CloudConfigException {
		configReaderWriter.saveProperty("testProp", new Integer(9), "testprojec", "prod");
		configReaderWriter.setActiveEnvironment("testprojec", null);
		Integer val = (Integer) configReaderWriter.getPropertyValue("testProp",
				"testprojec");
		Assert.assertNull(val);

	}
	
	@Test
	public void addProjects() throws CloudConfigException {
		configReaderWriter.createProject("Testproj1");
		configReaderWriter.createProject("Testproj2");
		configReaderWriter.createProject("Testproj3");
		System.out.println(configReaderWriter.listProjects().size());
		System.out.println(configReaderWriter.listProjects());
		Assert.assertTrue(configReaderWriter.listProjects().size() >= 3);

	}
	
	@Test
	public void addActiveProjectEnvironment() throws CloudConfigException {
		configReaderWriter.createProject("TESTPROJECT");
		configReaderWriter.addEnvironmentToProject("TESTPROJECT", "PRODPROD");
		Assert.assertTrue(configReaderWriter.getActiveEnvironment("TESTPROJECT").equals("PRODPROD"));

	}

}
