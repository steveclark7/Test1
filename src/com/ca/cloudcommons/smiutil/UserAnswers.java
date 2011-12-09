package com.ca.cloudcommons.smiutil;

import java.util.ArrayList;
import java.util.List;

import com.ssx.xml.output.XmlOutput;

public class UserAnswers {
	private String user = "";
	private String service = "";
	
	private List<Indicator>list = new ArrayList<Indicator>();
	
	
	
	public UserAnswers(String user, String service) {
		super();
		this.user = user;
		this.service = service;
	}

//	public UserAnswers() {
//	}
	
	void add(Indicator indicator){
		list.add(indicator);
	}
	
	void loadXmlOut(XmlOutput out) {
		out.startElement("User-Answers");		
		out.attribute("user", user);
		out.attribute("service", service);
		
		getIndicators(out);
		
		out.endElement();
	}
	
	
	private void getIndicators(XmlOutput out){
		
		for (Indicator iterable : list) {
			iterable.loadXmlOut(out);
			
		}
	}
	
	
//	<User-Answers>
//		<user>User2@test.com</user>
//		<service>TestService1</service>
//		<indicator>
//			<name>Suitability_indOne</name>
//			<indicatorValue>4</indicatorValue>
//			<updateTime>2011-3-11-16-1-3</updateTime>
//		</indicator>	

}
