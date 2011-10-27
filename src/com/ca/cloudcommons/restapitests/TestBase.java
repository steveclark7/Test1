package com.ca.cloudcommons.restapitests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestName;

enum SleepTime {
	SHORT(1), MEDIUM(5), LONG(10);

	private int time = 0;

	private SleepTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}
}

public abstract class TestBase {

	public final int HTTP_OK = 200;
	public final int HTTP_UNAUTHORIZED = 401;

	@Rule
	public TestName name = new TestName();

	private DefaultHttpClient httpclient = new DefaultHttpClient();

	private HttpHost proxy;
	BasicHttpContext localcontext = new BasicHttpContext();
	UiProperties ourProps;
	HttpHost targetHost;
	private static boolean showMe = true;

	// String getPasswordAuthentication() {
	// String username = UiProperties.getUser();
	// String password = UiProperties.getPassword();
	//
	// return username + ":" + password;
	// }

	public String getMethodName() {
		return name.getMethodName();
	}

	private void startTest() {
		System.out.println("\nRunning: " + getMethodName());
	}

	public void succeedTest() {
		System.out.println("Succeeded: " + getMethodName());
	}

	void show(String info) {
		// TODO logging

		if (ourProps.getDebugLevel() > 0) {
			System.out.println(info);
		}
	}

	void showAlways(String info) {
		System.out.println(info);
	}

	static void showStatic(String info) {
		// TODO logging
		if (showMe) {
			System.out.println(info);
		}
	}

	/**
	 * 
	 * @param seconds
	 *            SleepTime enum
	 * @throws Exception
	 */
	static void sleep(SleepTime seconds) throws Exception {
		Thread.sleep(seconds.getTime() * 1000);
	}

	int checkStatusCode(HttpResponse response) {
		StatusLine sl = response.getStatusLine();
		return sl.getStatusCode();
	}

	/**
	 * In general every test should call this method because it cleans up
	 * (consumes the) call.
	 * 
	 * @param response
	 * @throws IOException
	 */
	void showResponse(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();

		show(response.getStatusLine().toString());
		if (entity != null) {
			// System.out.println("Response content length: " +
			// entity.getContentLength());
			InputStream content = entity.getContent();

			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = in.readLine()) != null) {
				show(line);
			}

			in.close();
		}

		show("----------------------------------------");

		// If you don't call this after a http execution you get connection
		// errors.
		EntityUtils.consume(entity);
	}

	void clearHttpUserPass() throws Exception {

		loadProperties();

		String url = ourProps.getCloudSite();

		httpclient.getCredentialsProvider().setCredentials(new AuthScope(url, 80),
				new UsernamePasswordCredentials("UserAnonGuest", "pppppp"));
	}

	/**
	 * Called before all test cases that access a web site. Sets up all
	 * credentials and http globals.
	 * 
	 * @throws Exception
	 */
	void setupHttp() throws Exception {

		loadProperties();
		startTest();

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

		if( proxyName.length() > 0){
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(proxyName, port),
					new NTCredentials(ourProps.getProxyUser(), ourProps.getProxyPassword(), "workstation", "tant-a01"));
	
			List<String> proxyAuthPrefs = new ArrayList<String>();
			proxyAuthPrefs.add(AuthPolicy.NTLM);
	
			httpclient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, proxyAuthPrefs);
	
			proxy = new HttpHost(proxyName, port);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	void closeHttp() {
		show("Ending: " + getMethodName());

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpclient.getConnectionManager().shutdown();
		show("");
	}

	/**
	 * Get the authentication token for use with subsequent calls.
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	String getToken(int code) throws IOException {
		HttpGet httpget = new HttpGet("/api/1.0/authToken");

		show("executing request: " + httpget.getRequestLine());
		show("via proxy: " + proxy);
		show("to target: " + targetHost);

		HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);
		String line = getFirstLine(response);

		Assert.assertTrue("getToken status is not OK!", (code == checkStatusCode(response)));

		return line;
	}

	void showTestInfo() throws IOException {
		loadProperties();

		String url = ourProps.getCloudSite();
		targetHost = new HttpHost(url);

		showAlways("");
		
		String proxy = "No Proxy Configured";
		
		if( ourProps.getProxy().length() > 0 ){
			proxy = ourProps.getProxy();
		}
		
		//TODO update this for every release
		showAlways("REST API tests version 1.0");
		
		showAlways("executing requests via proxy: " + proxy);
		showAlways("to target: " + targetHost);
		showAlways("");
	}

	String getFirstLine(HttpResponse response) throws IOException {
		String line = "";
		HttpEntity entity = response.getEntity();

		show(response.getStatusLine().toString());
		if (entity != null) {
			// System.out.println("Response content length: " +
			// entity.getContentLength());
			InputStream content = entity.getContent();

			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			while ((line = in.readLine()) != null) {
				show(line);
				break;
			}

			in.close();
		}

		show("----------------------------------------");

		EntityUtils.consume(entity);

		return line;
	}

	ArrayList<String> regxCsvArray(String csvCommand) {
		ArrayList<String> csvList = new ArrayList<String>();

		// regex matches csv string
		Pattern pat = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

		String[] str = pat.split(csvCommand);

		for (int i = 0; i < str.length; i++) {
			String tmp = str[i];
			String element = tmp;

			if (element.length() > 2) {
				if (element.startsWith("\"") && element.endsWith("\"")) {
					element = tmp.substring(1, element.length() - 1);
				}
			}

			csvList.add(element);
		}

		return csvList;
	}

	void loadProperties() throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream("c:\\apitests.properties"));
		ourProps = new UiProperties(props);
	}

	String listProjects(String details) throws IOException, ClientProtocolException {
		HttpGet httpget = new HttpGet("/api/1.0/project/" + details);
		// httpget.setHeader("authToken", token);
		show("executing request: " + httpget.getRequestLine());
		HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);

		int code = checkStatusCode(response);		
		Assert.assertTrue("listProjects returned http code: " + code, (200 == code));

		return getFirstLine(response);
	}

	HttpResponse execute(HttpRequestBase http) throws IOException, ClientProtocolException {
		// String token = getToken();

		// http.setHeader("authToken", token);

		show("executing request: " + http.getRequestLine());
		return (httpclient.execute(targetHost, http, localcontext));
	}

}
