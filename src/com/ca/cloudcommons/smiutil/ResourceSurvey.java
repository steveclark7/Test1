package com.ca.cloudcommons.smiutil;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssx.xml.output.XmlOutput;
import com.ssx.xml.output.impl.XmlValidator;
import com.ssx.xml.output.sinks.TextSink;

public class ResourceSurvey {

	private String organization = "CA";
	private String survey = "Survey1";
		
	private List<UserAnswers>list = new ArrayList<UserAnswers>();

	
	public ResourceSurvey() {
	}
	
	
	void add(UserAnswers value){
		list.add(value);
	}


	/** Surround attribute values with single quotes */
	protected static final String USE_SINGLE_QUOTE = "useSingleQuote";
	/**
	 * Add line breaks and indenting to the XML output where applicable (ie. TextSink)
	 */
	protected static final String USE_FORMATTING = "useFormatting";
	/** The line break sequence to use when formatting */
	protected static final String END_OF_LINE_MARKER = "endOfLineMarker";
	/** The characters used in a single indentation when formating */
	protected static final String INDENT_SPACER = "indentSpacer";
	/**
	 * A testing switch - show namespace names rather than prefixes on output where applicable (ie. TextSink)
	 */
	protected static final String EXPAND_QNAMES = "expandQNames";

	/*		
	<Resource-Survey> 
		<organization>TestOrg</organization>       
		<survey>TestSurvey</survey>                    
		<User-Answers> 
			<user>User1@test.com</user>   
			<service>TestService1</service> 
			<indicator> 
				<name>Suitability_indOne</name> 
				<indicatorValue>9</indicatorValue> 
				<updateTime>2011-3-11-16-1-3</updateTime> 
				</indicator> 
	            …
	*/		

	String getXml() {
		String retval = "";
		XmlOutput out = newXmlOutput(null);
		
		out.startElement("Resource-Survey");
		
		out.startElement("organization");
		out.text(organization);
		out.endElement();
		
		out.startElement("survey");
		out.text(survey);
		out.endElement();	
		
			getUserAnswers(out);
		out.endElement();
		out.close();
		
        retval = getXmlText(out);		

		return retval;
	}
	
	void getUserAnswers(XmlOutput out){
		for (UserAnswers iterable : list) {
			iterable.loadXmlOut(out);			
		}		
	}
	

	protected XmlOutput newXmlOutput(HashMap<?, ?> properties) {
		boolean useSingleQuote = true;
		boolean useFormatting = true;
		String lineBreak = "\n";
		String indent = "    ";

		if (null != properties) {
			if (properties.containsKey(USE_SINGLE_QUOTE)) {
				useSingleQuote = ((Boolean) properties.get(USE_SINGLE_QUOTE)).booleanValue();
			}
			if (properties.containsKey(USE_FORMATTING)) {
				useFormatting = ((Boolean) properties.get(USE_FORMATTING)).booleanValue();
			}
			if (properties.containsKey(END_OF_LINE_MARKER)) {
				lineBreak = (String) properties.get(END_OF_LINE_MARKER);
			}
			if (properties.containsKey(INDENT_SPACER)) {
				indent = (String) properties.get(INDENT_SPACER);
			}
		}

		TextSink sink = new TextSink(new StringWriter(), "utf-8", useSingleQuote, useFormatting, lineBreak, indent);
		return new XmlValidator(sink);
	}

	String getXmlText(XmlOutput output) {
		XmlValidator validator = (XmlValidator) output;
		if (validator.getSink() instanceof TextSink) {
			String xmlText = ((TextSink) validator.getSink()).getOutput().toString();
			return xmlText.substring("<?xml version='1.0' encoding='utf-8'?>".length());
		}

		throw new IllegalArgumentException("XmlTextOutput instance required.");
	}

}
