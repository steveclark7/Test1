package com.ca.cloudcommons.smiutil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import com.ca.cloudcommons.smiutil.ProductFileParser.NameIp;

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
//	@Ignore	
	public void listService() throws Exception {
		// https://smi.cloudcommons.com:8443/Insight_API/json/SMI/0.5/service/create?
		// providerUUID=50fc741c-5447-11df-a06a-c7daeae07f53&name=TestService&desc=testservice

		HttpConnector smi = getHttpSmi();
		List<NameValuePair> qparams = smi.getAdminAuthentication();

		String uuid = "a1fdd99c-046b-8a8d-e040-007f0100275c";

		// show(uuid);

		URI uri = smi.getURI("/Insight_API/xml/SMI/0.5/service/" + uuid, qparams);
		HttpGet http = new HttpGet(uri);

		show("executing request: " + http.getRequestLine());
		HttpResponse response = smi.execute(http);

		showResponse(response);
	}

	@Test
	@Ignore
	public void parseInputFile() throws Exception {
		ProductFileParser pp = new ProductFileParser();
		
//		pp.parseFile("export_all_products-filtered-test1.csv");
		ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-NoDesc.csv");
//		ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-empty-desc.csv");
		
		
		for (NameIp nameIp : magentoList) {
			System.out.println("Status: " + nameIp.getStatus() + " Name: " + nameIp.getProduct_name());			
		}						
	}
	

	@Test
	@Ignore
	public void listSMIItems() throws Exception {

//		http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
//		category/aba45240-8291-11df-bca3-339c3162b513/
//		service/b141b6de-09aa-4a57-bf11-eb35e6458dda/naturalScore?
//		&key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef		
		
//		http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
//		&key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef
		
//		listAllItems1("categories");
//		listAllItems1("providers");				
		
		listAllItems1("services");		
		
//		listAllItems1("surveys");		
	}

	@Test
	public void listProviders() throws Exception {
//		listAllItems1("providers");
		
		listAllItems1("providers");	
		
		Document doc = getXmlResponse("providers");
		
		createProviderList(doc);
		
		
		
	}
	
	
	
	
}
