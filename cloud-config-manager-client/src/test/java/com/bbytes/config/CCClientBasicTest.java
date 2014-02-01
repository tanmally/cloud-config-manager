
package com.bbytes.config;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bbytes.config.client.CloudConfigClient;
import com.bbytes.config.client.CloudConfigException;
import com.bbytes.config.domain.CCProperty;

/**
 * 
 * 
 * @author Thanneer
 * 
 * @version
 */
public class CCClientBasicTest extends DaasClientBaseTest {
	

	
	@Before
	public void SetUp() throws CloudConfigException {
	}

	@Test
	public void CCClientTest() throws CloudConfigException  {
		CloudConfigClient ccClient = getCCClient();
		boolean success = ccClient.pingSuccess();
		Assert.assertTrue(success);
	}
	
	
	@Test
	public void getPropetyForProjectClientTest() throws CloudConfigException  {
		CloudConfigClient ccClient = getCCClient();
		List<String> projectList = ccClient.getProjectList();
		List<CCProperty> properties = ccClient.getPropertiesForProject(projectList.get(3));
		for (CCProperty ccProperty : properties) {
			System.out.println(ccProperty);
		}
		Assert.assertTrue(properties.size() > 0);
	}
	
	

//	@After
//	public void cleanUp() {
//		asyncHttpClient.close();
//	}
}
