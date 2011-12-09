package com.ca.cloudcommons.smiutil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestInfo extends TestBase {

	@BeforeClass
	public static void init() throws Exception {
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
	public void showInfo() throws Exception {
		showTestInfo();
	}
		

}
