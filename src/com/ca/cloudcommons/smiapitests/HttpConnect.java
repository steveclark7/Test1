package com.ca.cloudcommons.smiapitests;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpConnect {

	public void setupHttp(UiProperties props) throws Exception;

	public void closeHttp();

	public URI getURI(String query, List<NameValuePair> nvps) throws URISyntaxException;

	public HttpResponse execute(HttpRequestBase http) throws ClientProtocolException, IOException;

	public String configurationInfo() throws IOException;

	public List<NameValuePair> getAuthenticationList();
	
	public List<NameValuePair> getAdminAuthentication();

}