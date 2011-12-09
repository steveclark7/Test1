package com.ca.cloudcommons.smiapitests;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUpload extends TestBase {

	@BeforeClass
	public static void init() throws Exception {
		showStatic("Starting: " + TestUpload.class);
	}

	@AfterClass
	public static void cleanup() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		setupHttp();
	}

	@After
	public void tearDown() throws Exception {
		closeHttp();
	}

	@Test
	public void showSurvey() throws Exception {
		ResourceSurvey xgen = new ResourceSurvey();

		UserAnswers ua = new UserAnswers("MyUser1", "MyService1");
		ua.add(new Indicator("name1", "5", "2011-3-11-16-1-3"));
		ua.add(new Indicator("name2", "6", "2011-3-11-16-1-4"));
		ua.add(new Indicator("name3", "7", "2011-3-11-16-1-5"));
		xgen.add(ua);

		ua = new UserAnswers("MyUser2", "MyService1");
		ua.add(new Indicator("nameA", "1", "2011-3-11-16-1-3"));
		ua.add(new Indicator("nameB", "3", "2011-3-11-16-1-4"));
		ua.add(new Indicator("nameC", "9", "2011-3-11-16-1-5"));
		xgen.add(ua);

		HttpConnector smi = getHttpSmi();
		
		List<NameValuePair> qparams = smi.getAuthenticationList();
		String payload = xgen.getXml();
		qparams.add(new BasicNameValuePair("xmlData", payload));
		show(payload);

		URI uri = smi.getURI("/Insight_API/xml/SMI/0.5/setIndicator", qparams);
		HttpPost http = new HttpPost(uri);

		show("executing request: " + http.getRequestLine());
		HttpResponse response = smi.execute(http);

		showResponse(response);
		// showXMLResponse(response);
	}

}
