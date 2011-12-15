package com.ca.cloudcommons.smiutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	static Logger _log = Logger.getLogger(TestBase.class);
	

	public final int HTTP_OK = 200;
	public final int HTTP_UNAUTHORIZED = 401;

	@Rule
	public TestName name = new TestName();

	private static boolean showMe = true;

	private HttpConnector httpSmi;
	private UiProperties ourProps;

	// String getPasswordAuthentication() {
	// String username = UiProperties.getUser();
	// String password = UiProperties.getPassword();
	//
	// return username + ":" + password;
	// }

	public TestBase() {
		super();
	}

	public UiProperties getOurProps() {
		return ourProps;
	}

	public HttpConnector getHttpSmi() {
		return httpSmi;
	}

	public String getMethodName() {
		return name.getMethodName();
	}

	void loadProperties() throws Exception {
		// Properties props = new Properties();
		// props.load(new FileInputStream("c:\\smiapitests.properties"));
		ourProps = new UiProperties();
		httpSmi.setupHttp(ourProps);
	}

	private void startTest() {
		System.out.println("\nRunning: " + getMethodName());
	}

	public void succeedTest() {
		System.out.println("Succeeded: " + getMethodName());
	}

	void show(String info) {

		_log.debug(info);
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
	 * In general every test should call this method because it cleans up (consumes the) call.
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

	/**
	 * Called before all test cases that access a web site. Sets up all credentials and http globals.
	 * 
	 * @throws Exception
	 */
	void setupHttp() throws Exception {

		httpSmi = new HttpConnector();

		loadProperties();

		startTest();

	}

	void closeHttp() {

		show("Ending: " + getMethodName());
		httpSmi.closeHttp();
		show("");
	}

	void showTestInfo() throws IOException {

		// TODO update this for every release
		showAlways("SMI API tests version 1.0");

		showAlways(httpSmi.configurationInfo());
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

	// http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
	// category/aba45240-8291-11df-bca3-339c3162b513/
	// service/b141b6de-09aa-4a57-bf11-eb35e6458dda/naturalScore?
	// &key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef

	// http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
	// &key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef

	String listAllItems1(String details) throws Exception {
		// HttpGet http = new HttpGet("/Insight_API/xml/SMI/0.5/" + details);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", ourProps.getUser()));
		qparams.add(new BasicNameValuePair("secret", ourProps.getPassword()));

		String encodedUri = URLEncodedUtils.format(qparams, "UTF-8");

		URI uri = URIUtils.createURI("http", ourProps.getCloudSite(), -1, "/Insight_API/xml/SMI/0.5/" + details,
				encodedUri, null);

		HttpGet http = new HttpGet(uri);

		// http.setHeader(header)
		// show("executing request: " + http.getRequestLine());

		// List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		// nvps.add(new BasicNameValuePair("key", ourProps.getUser()));
		// nvps.add(new BasicNameValuePair("secret", ourProps.getPassword()));

		// http.addHeader("key", ourProps.getUser());
		// http.addHeader("secret", ourProps.getPassword());

		// http.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = execute(http);

		// showXMLResponse(response);

		showResponse(response);

		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));
		return "";// getFirstLine(response);
	}

	Document getXmlResponse(String details) throws Exception {
		// HttpGet http = new HttpGet("/Insight_API/xml/SMI/0.5/" + details);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", ourProps.getUser()));
		qparams.add(new BasicNameValuePair("secret", ourProps.getPassword()));

		String encodedUri = URLEncodedUtils.format(qparams, "UTF-8");

		URI uri = URIUtils.createURI("http", ourProps.getCloudSite(), -1, "/Insight_API/xml/SMI/0.5/" + details,
				encodedUri, null);

		HttpGet http = new HttpGet(uri);

		// http.setHeader(header)
		// show("executing request: " + http.getRequestLine());

		// List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		// nvps.add(new BasicNameValuePair("key", ourProps.getUser()));
		// nvps.add(new BasicNameValuePair("secret", ourProps.getPassword()));

		// http.addHeader("key", ourProps.getUser());
		// http.addHeader("secret", ourProps.getPassword());

		// http.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = execute(http);

		Document doc = getXmlFromResponse(response);

		// showResponse(response);

		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));
		return doc;// getFirstLine(response);
	}

	HttpResponse execute(HttpRequestBase httpRequest) throws IOException, ClientProtocolException {
		// String token = getToken();

		// http.setHeader("authToken", token);

		show("executing request: " + httpRequest.getRequestLine());
		return httpSmi.execute(httpRequest);
	}

	Document getXMLDocument(InputStream content) throws Exception {
		Document doc = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(content);
		doc.getDocumentElement().normalize();

		// showResultValues(doc);

		return doc;
	}

	private void showResultValues(Document doc) {
		show("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("result");
		show("-----------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				show("returnCodee : " + getTagValue("returnCode", eElement));
				show("errorMessage : " + getTagValue("errorMessage", eElement));
				show("errorReason : " + getTagValue("errorReason", eElement));
				show("resultCount : " + getTagValue("resultCount", eElement));
			}
		}
	}


	private String getTagValue(String sTag, Element eElement) {
		String retval = "";

		NodeList tagElemnts = eElement.getElementsByTagName(sTag);

		if (tagElemnts.getLength() > 0) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

			Node nValue = (Node) nlList.item(0);

			retval = nValue.getNodeValue();
		}

		return retval;
	}

	/**
	 * In general every test should call this method because it cleans up (consumes the) call.
	 * 
	 * @param response
	 * @throws Exception
	 */
	Document getXmlFromResponse(HttpResponse response) throws Exception {
		Document doc = null;
		HttpEntity entity = response.getEntity();

		show(response.getStatusLine().toString());
		if (entity != null) {
			// show("Response content length: " +
			// entity.getContentLength());
			InputStream content = entity.getContent();

			doc = getXMLDocument(content);

//			showResultValues(doc);

			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(content));
			// String line;
			// while ((line = in.readLine()) != null) {
			// show(line);
			// }
			//
			// in.close();
		}

		show("----------------------------------------");

		// If you don't call this after a http execution you get connection
		// errors.
		EntityUtils.consume(entity);

		return doc;
	}


}
