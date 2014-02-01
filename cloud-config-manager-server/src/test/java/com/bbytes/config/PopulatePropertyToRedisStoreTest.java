package com.bbytes.config;

import java.util.Date;

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
public class PopulatePropertyToRedisStoreTest {

	@Autowired
	IConfigReaderWriter configReaderWriter;



	@Test
	public void storeAndLoad() throws CloudConfigException {
		configReaderWriter.createProject("test");
		configReaderWriter.addEnvironmentToProject("test", "dev");
		
		configReaderWriter.saveProperty("prop1", new Integer(9), "test", "dev");
		configReaderWriter.saveProperty("prop2", new Double(77d), "test", "dev");
		configReaderWriter.saveProperty("prop3", new String("string"), "test", "dev");
		configReaderWriter.saveProperty("prop4", new Date(), "test", "dev");
		configReaderWriter.saveProperty("prop5", new Long(222), "test", "dev");
		configReaderWriter.saveProperty("prop6", new String("last"), "test", "dev");
		configReaderWriter.setActiveEnvironment("test", "dev");
		Integer val = (Integer) configReaderWriter.getPropertyValue("prop1",
				"test");
		Assert.assertEquals(9, val.intValue());

	}
	
	

}
