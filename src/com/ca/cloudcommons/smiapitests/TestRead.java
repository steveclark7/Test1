package com.ca.cloudcommons.smiapitests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
	public void listSMIItems() throws Exception {

//		http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
//		category/aba45240-8291-11df-bca3-339c3162b513/
//		service/b141b6de-09aa-4a57-bf11-eb35e6458dda/naturalScore?
//		&key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef		
		
//		http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
//		&key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef
		
//		listAllItems1("categories");
		listAllItems1("providers");				
		
//		listAllItems1("services");		
		
//		listAllItems1("surveys");		
	}

	@Test
	@Ignore
	public void listProviders() throws Exception {
		listAllItems1("providers");				
		
	}
	
	
	
}
