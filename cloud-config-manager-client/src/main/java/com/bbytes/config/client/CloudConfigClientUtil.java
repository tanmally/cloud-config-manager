
package com.bbytes.config.client;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

/**
 * 
 * 
 * @author Thanneer
 * 
 * @version
 */
public class CloudConfigClientUtil {

	

	public static BoundRequestBuilder buildRequest(AsyncHttpClient asyncHttpClient, String httpMethodType, String url,
			String userName , String password) {

		switch (httpMethodType.toLowerCase()) {
		case "post":
			return asyncHttpClient.preparePost(url).addHeader("Authorization", " Bearer "  );
		case "get":
			return asyncHttpClient.prepareGet(url).addHeader("Authorization", " Bearer "  );
		case "put":
			return asyncHttpClient.preparePut(url).addHeader("Authorization", " Bearer "  );
		case "delete":
			return asyncHttpClient.prepareDelete(url).addHeader("Authorization", " Bearer ");
		default:
			return asyncHttpClient.prepareGet(url).addHeader("Authorization", " Bearer " );
		}
	}


	
}