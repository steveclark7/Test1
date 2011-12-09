package com.ca.cloudcommons.smiapitests;

import com.ssx.xml.output.XmlOutput;

public class Indicator {
	private String name = "";
	private String indicatorValue = "";	
	private String updateTime = "";	
	
		
//	public Indicator() {
//	}
		
	public Indicator(String name, String indicatorValue, String updateTime) {
		super();
		this.name = name;
		this.indicatorValue = indicatorValue;
		this.updateTime = updateTime;
	}



	void loadXmlOut(XmlOutput out) {
		out.startElement("indcator");		
		out.attribute("name", name);
		out.attribute("indicatorValue", indicatorValue);				
		out.attribute("updateTime", updateTime);				
		out.endElement();
	}
	
	
//		<indicator>
//			<name>Suitability_indOne</name>
//			<indicatorValue>4</indicatorValue>
//			<updateTime>2011-3-11-16-1-3</updateTime>
//		</indicator>	

}
