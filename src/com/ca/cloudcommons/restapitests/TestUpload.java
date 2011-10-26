package com.ca.cloudcommons.restapitests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUpload extends TestBase {	
	private final String TEXTFILE = "123.txt";
	private final String PNGFILE = "agree.png";
	

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
	public void addDeleteProject() throws Exception {
		String newProject = "steve-p1";
		
		listProjects("");

		HttpPost http = new HttpPost("/api/1.0/project" );
		
	    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	    nvps.add(new BasicNameValuePair("name", newProject));
		http.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));		
		HttpResponse response = execute( http );		
		
		showResponse(response);
		
		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));		

		listProjects("");		
		
		deleteProject();			
	}
			

	@Test			
	public void uploadSameFile() throws Exception {		
		listProjects(ourProps.getProject());

		addFile(TEXTFILE);
		
		// TODO should this overwrite?
		addFile(TEXTFILE);
		
		deleteFile(TEXTFILE);		
	}
	
	@Test		
	public void uploadPngFile() throws Exception {
		listProjects(ourProps.getProject());

		addFile("agree.png");
		String files = listProjects( ourProps.getProject()+ "/trunk");
		Assert.assertTrue("File is not added!", (files.contains(PNGFILE)));
		
//		deleteFile("agree.png");		
	}
	
	
	@Test		
	public void uploadTxtFile() throws Exception {
		listProjects( ourProps.getProject());

		addFile(TEXTFILE);
		String files = listProjects( ourProps.getProject()+ "/trunk");
		Assert.assertTrue("File is not added!", (files.contains(TEXTFILE)));				

		deleteFile(TEXTFILE);		
		files = listProjects( ourProps.getProject()+ "/trunk");		
		Assert.assertTrue("File is not deleted!", (!files.contains(TEXTFILE)));				
	}

	private void addFile(String fileName) throws IOException, ClientProtocolException {
		String projectNameAndPath = ourProps.getProject()+ "/trunk";
		
		HttpPost method = new HttpPost("/api/1.0/project/" + projectNameAndPath);
		MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		// For File parameters
		entity.addPart("file", new FileBody((new File(fileName)), "binary/octet-stream"));
		// For usual String parameters
		// entity.addPart( paramName, new StringBody( paramValue.toString(), "text/plain", Charset.forName( "UTF-8" )));
		method.setEntity( entity );
		HttpResponse response = execute( method);
		
		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));
		
		showResponse(response);		
	}
	
			
	private void deleteFile(String fileName) throws Exception {		
		String projectNameAndPath = ourProps.getProject()+ "/trunk/";
				
		HttpDelete httpget = new HttpDelete("/api/1.0/project/" + projectNameAndPath + fileName);
		HttpResponse response = execute(httpget);
		showResponse(response);		

		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));
	}
		
	private void deleteProject() throws Exception {
		String newProject = "steve-p1";
		
		HttpDelete http = new HttpDelete("/api/1.0/project/" + newProject );		
		HttpResponse response = execute(http);
		
		showResponse(response);		
		Assert.assertTrue("Returned status is not OK!", (200 == checkStatusCode(response)));		

		listProjects("");		
	}

}
