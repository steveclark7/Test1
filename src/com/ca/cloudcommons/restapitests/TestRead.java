package com.ca.cloudcommons.restapitests;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRead extends TestBase {

	@BeforeClass
	public static void init() throws Exception {
		showStatic("Starting: " + TestRead.class);
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
	public void getAuthTokenByGuest() throws Exception {
		clearHttpUserPass();

		// this should fail as not logged in
		getToken(HTTP_UNAUTHORIZED);
	}

	@Test
	public void getAuthToken() throws Exception {
		String token = getToken(HTTP_OK);
		Assert.assertTrue("Can't find token", token.length() > 0);
	}

	@Test
	public void listAllProjects() throws Exception {
		listProjects("");
	}

	@Test
	/**
	 * List the project defined in properties file
	 */
	public void listProject() throws Exception {
		listProjects(ourProps.getProject());
	}
	
	@Test
	public void listFirstProject() throws Exception {
		HttpGet httpget = new HttpGet("/api/1.0/project");

		HttpResponse response = execute(httpget);
		String projects = getFirstLine(response);

		if (projects.startsWith("[") && projects.endsWith("]")) {
			projects = projects.substring(1, projects.length() - 1);
		}

		ArrayList<String> al = regxCsvArray(projects);

		if (al.size() > 0) {
			String proj1 = al.get(0);
			httpget = new HttpGet("/api/1.0/project/" + proj1);
			response = execute(httpget);
			showResponse(response);

			httpget = new HttpGet("/api/1.0/project/" + proj1 + "/trunk");
			response = execute(httpget);
			showResponse(response);
		} else {
			Assert.fail("Can't read first project");
		}
	}


}
