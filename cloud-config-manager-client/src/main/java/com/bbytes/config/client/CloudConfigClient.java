package com.bbytes.config.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.bbytes.config.domain.CCProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;

public class CloudConfigClient {

	protected AsyncHttpClient asyncHttpClient;

	protected String host;

	protected String port;

	protected String baseURL;

	protected ObjectMapper objectMapper;

	public CloudConfigClient(String host, String port) {

		this.host = host;
		this.port = port;

		baseURL = "http://" + host + ":" + port + URLConstants.SERVER_CONTEXT;

		Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setCompressionEnabled(true).setAllowPoolingConnection(true).setConnectionTimeoutInMs(30000).build();

		asyncHttpClient = new AsyncHttpClient(builder.build());

		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);

	}

	public boolean pingSuccess() {
		try {
			Future<Response> f = asyncHttpClient.prepareGet(baseURL + "/ping").execute();
			Response r = f.get();
			if (!HttpStatusUtil.getReponseStatus(r).endsWith(HttpStatusUtil.SUCCESS))
				return false;

		} catch (InterruptedException | ExecutionException | IOException e) {
			return false;
		}

		return true;
	}

	public List<String> getProjectList() throws CloudConfigException {
		try {
			String url = baseURL + "/project/list";

			Future<Response> f = buildRequest("get", url).setHeader("Content-Type", "application/json").execute();

			Response r = f.get();

			checkResponse(r);

			List<String> projectList = objectMapper.readValue(r.getResponseBody(), objectMapper.getTypeFactory()
					.constructCollectionType(List.class, String.class));

			return projectList;

		} catch (Exception e) {
			throw new CloudConfigException(e);
		}
	}

	public List<CCProperty> getPropertiesForProject(String project) throws CloudConfigException {
		try {
			String url = baseURL + "/project/" + project + "/property";

			Future<Response> f = buildRequest("get", url).setHeader("Content-Type", "application/json").execute();

			Response r = f.get();

			checkResponse(r);

			List<CCProperty> properties = objectMapper.readValue(r.getResponseBody(), objectMapper.getTypeFactory()
					.constructCollectionType(List.class, CCProperty.class));

			return properties;

		} catch (Exception e) {
			throw new CloudConfigException(e);
		}
	}

	/**
	 * @param r
	 * @throws CloudConfigException
	 * @throws IOException
	 */
	private void checkResponse(Response r) throws CloudConfigException, IOException {
		if (!HttpStatusUtil.isSuccess(r)) {

			if (r.getStatusCode() == HttpStatusUtil.INTERNAL_SERVER_ERROR)
				throw new CloudConfigException(r.getResponseBody());
			else
				throw new CloudConfigException("Cloud config error : " + r.getResponseBody());
		}

	}

	protected BoundRequestBuilder buildRequest(String httpMethodType, String url) {
		return CloudConfigClientUtil.buildRequest(asyncHttpClient, httpMethodType, url, "", "");
	}

	public void close() {
		if (asyncHttpClient != null && !asyncHttpClient.isClosed())
			asyncHttpClient.close();
	}

	public boolean isClosed() {
		return asyncHttpClient.isClosed();
	}

}
