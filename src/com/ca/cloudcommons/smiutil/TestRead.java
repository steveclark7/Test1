package com.ca.cloudcommons.smiutil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import com.ca.cloudcommons.smiutil.ProductFileParser.NameIp;
import com.ca.cloudcommons.smiutil.SmiXml.ProviderInfo;

public class TestRead extends TestBase {

	static Logger _log = Logger.getLogger(TestRead.class);
	
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
	@Ignore
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
	public void listProvider() throws Exception {
		// https://smi.cloudcommons.com:8443/Insight_API/json/SMI/0.5/service/create?
		// providerUUID=50fc741c-5447-11df-a06a-c7daeae07f53&name=TestService&desc=testservice

		HttpConnector smi = getHttpSmi();
		List<NameValuePair> qparams = smi.getAdminAuthentication();

		String uuid = "ae89bb71-b8d2-0c0b-e040-13ac0d6e2518";

		// show(uuid);

		URI uri = smi.getURI("/Insight_API/xml/SMI/0.5/provider/" + uuid, qparams);
		HttpGet http = new HttpGet(uri);

		show("executing request: " + http.getRequestLine());
		HttpResponse response = smi.execute(http);

		showResponse(response);
	}

	@Test
	@Ignore
	public void listProviderServices() throws Exception {
		// https://smi.cloudcommons.com:8443/Insight_API/json/SMI/0.5/service/create?
		// providerUUID=50fc741c-5447-11df-a06a-c7daeae07f53&name=TestService&desc=testservice

		// CA
		String uuid = "ae89bb71-b8d2-0c0b-e040-13ac0d6e2518";

		getServices(uuid);
	}

	private void getServices(String uuid) throws URISyntaxException, ClientProtocolException, IOException {
		HttpConnector smi = getHttpSmi();
		List<NameValuePair> qparams = smi.getAdminAuthentication();

		// show(uuid);

		URI uri = smi.getURI("/Insight_API/xml/SMI/0.5/provider/" + uuid + "/service", qparams);
		HttpGet http = new HttpGet(uri);

		show("executing request: " + http.getRequestLine());
		HttpResponse response = smi.execute(http);

		showResponse(response);
	}

	@Test
	@Ignore
	public void parseInputFile() throws Exception {
		ProductFileParser pp = new ProductFileParser();

		// pp.parseFile("export_all_products-filtered-test1.csv");
		ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-NoDesc.csv");
		// ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-empty-desc.csv");

		for (NameIp nameIp : magentoList) {
			_log.info("Status: " + nameIp.getStatus() + " Name: " + nameIp.getProduct_name());
		}
	}

	@Test
	@Ignore
	public void listSMIItems() throws Exception {

		// http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
		// category/aba45240-8291-11df-bca3-339c3162b513/
		// service/b141b6de-09aa-4a57-bf11-eb35e6458dda/naturalScore?
		// &key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef

		// http://50.57.192.171/Insight_API/xml/SMI/0.5/categories?
		// &key=ae5b26097e666ceadd6f851c8a5abbcd&secret=8af0b36d77aece511f661facfecf89ef

		// listAllItems1("categories");
//		 listAllItems1("providers");

		listAllItems1("services");

		// listAllItems1("surveys");
	}

	@Test
	public void listProviders() throws Exception {
		//listAllItems1("providers");
		SmiXml smiXml = new SmiXml();
		Document doc = getXmlResponse("providers");
		smiXml.loadProviderList(doc);

		ProductFileParser pp = new ProductFileParser();

		// pp.parseFile("export_all_products-filtered-test1.csv");
		// ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-NoDesc.csv");
		ArrayList<NameIp> magentoList = pp.parseFile("wilma-export_all_products-filtered-empty-desc.csv");

		for (NameIp nameIp : magentoList) {
			_log.info("Status: " + nameIp.getStatus() + " provider: " + nameIp.getManufacturer()
					+ " product: " + nameIp.getProduct_name());
			ProviderInfo pi = smiXml.getProvider(nameIp.getManufacturer());

			if (pi != null) {
				getServices(pi.getUuid());
			}
		}

	}

}
