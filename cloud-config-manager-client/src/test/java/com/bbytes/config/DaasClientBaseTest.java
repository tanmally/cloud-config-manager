package com.bbytes.config;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.bbytes.config.client.CloudConfigClient;
import com.bbytes.config.client.CloudConfigException;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

/**
 * 
 * 
 * @author Thanneer
 * 
 * @version
 */
public class DaasClientBaseTest {

	protected AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

	protected String host = "localhost";

	protected String port = "9000";

	@Test
	public void testHttpClient() throws IOException, InterruptedException,
			ExecutionException {
		Future<Response> f = asyncHttpClient.prepareGet("http://www.ning.com/")
				.execute();
		Response response = f.get();
		Assert.assertEquals(response.getStatusCode(), 200);
	}

	protected CloudConfigClient getCCClient() throws CloudConfigException {
		CloudConfigClient ccClient = new CloudConfigClient(host, port);
		return ccClient;

	}


}
