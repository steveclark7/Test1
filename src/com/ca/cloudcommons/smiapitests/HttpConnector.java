package com.ca.cloudcommons.smiapitests;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

public class HttpConnector implements HttpConnect {

	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private HttpHost proxy;
	private HttpHost targetHost;
	private BasicHttpContext localcontext = new BasicHttpContext();
	private UiProperties ourProps;

	public HttpConnector() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#setupHttp(com.ca.cloudcommons.smiapitests.UiProperties)
	 */
	@Override
	public void setupHttp(UiProperties props) throws Exception {
		ourProps = props;

		String url = ourProps.getCloudSite();
		targetHost = new HttpHost(url);

		initProxy();

		httpclient.getCredentialsProvider().setCredentials(new AuthScope(url, 80),
				new UsernamePasswordCredentials(ourProps.getUser(), ourProps.getPassword()));

		// MyAuthenticator ma = new MyAuthenticator();
		// String userPassword = ma.getPasswordAuthentication();
		//
		// // Encode String
		// String token = new
		// sun.misc.BASE64Encoder().encode(userPassword.getBytes());

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

		// HttpGet httpget = new HttpGet("/");

		// for (int i = 0; i < 3; i++) {
		// HttpResponse response = httpclient.execute(targetHost, httpget,
		// localcontext);
		// HttpEntity entity = response.getEntity();
		// EntityUtils.consume(entity);
		// }

		// httpclient.getParams().setParameter("authToken", token);
		// httpget.setHeader("authToken", token);

		// List authPrefs = new ArrayList();
		// authPrefs.add(AuthPolicy.BASIC);
		//
		// httpclient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF,
		// authPrefs);

	}

	private void initProxy() {
		String proxyName = ourProps.getProxy();
		int port = ourProps.getProxyPort();

		if (proxyName.length() > 0) {
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(proxyName, port),
					new NTCredentials(ourProps.getProxyUser(), ourProps.getProxyPassword(), "workstation", "tant-a01"));

			List<String> proxyAuthPrefs = new ArrayList<String>();
			proxyAuthPrefs.add(AuthPolicy.NTLM);

			httpclient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, proxyAuthPrefs);

			proxy = new HttpHost(proxyName, port);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#closeHttp()
	 */
	@Override
	public void closeHttp() {

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpclient.getConnectionManager().shutdown();
	}

	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#getURI(java.lang.String, java.util.List)
	 */
	@Override
	public URI getURI(String query, List<NameValuePair> nvps) throws URISyntaxException {

		String encodedUri = URLEncodedUtils.format(nvps, "UTF-8");

		URI uri = URIUtils.createURI("http", ourProps.getCloudSite(), -1, query, encodedUri, null);
		
		//TODO 8192 length check
		
		return uri;
	}

	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#execute(org.apache.http.client.methods.HttpRequestBase)
	 */
	@Override
	public HttpResponse execute(HttpRequestBase http) throws ClientProtocolException, IOException {
		return httpclient.execute(targetHost, http, localcontext);
	}
	
	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#configurationInfo()
	 */
	@Override
	public String configurationInfo() throws IOException {
		String retval = "";

		String url = ourProps.getCloudSite();
		targetHost = new HttpHost(url);

		String proxy = "No Proxy Configured";

		if (ourProps.getProxy().length() > 0) {
			proxy = ourProps.getProxy();
		}

//		// TODO update this for every release
//		showAlways("SMI API tests version 1.0");

		retval = "Executing requests via proxy: " + proxy + " to target: " + targetHost;
		
		return retval;
	}
	
	/* (non-Javadoc)
	 * @see com.ca.cloudcommons.smiapitests.HttpConnect#configurationInfo()
	 */
	@Override
	public List<NameValuePair> getAuthenticationList(){
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", ourProps.getUser()));
		qparams.add(new BasicNameValuePair("secret", ourProps.getPassword()));
		
		return qparams;
	}

	@Override
	public List<NameValuePair> getAdminAuthentication() {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", ourProps.getAdminUser()));
		qparams.add(new BasicNameValuePair("secret", ourProps.getAdminPassword()));
		
		return qparams;
	}
	
	

}
