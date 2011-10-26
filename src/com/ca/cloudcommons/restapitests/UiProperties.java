package com.ca.cloudcommons.restapitests;

import java.util.Properties;

public class UiProperties {
	
	private Properties ourProps;
	
	UiProperties( Properties ourProps ){
		this.ourProps = ourProps;		
	}

	public  String getUser() {
		return ourProps.getProperty("User", "a.user@ca.com");
	}

	public  String getPassword() {
		return ourProps.getProperty("Password", "password");
	}

    
	public  String getProxyUser() {
		return ourProps.getProperty("ProxyUser", "a.user@ca.com");
	}

	
	public  String getProxyPassword() {
		return ourProps.getProperty("ProxyPassword", "password");
	}
	
	public  String getCloudSite() {
		return ourProps.getProperty("CloudSite", "CloudSite");
	}
	
		
	public  String getProject() {
		return ourProps.getProperty("Project", "Project");
	}
	
}
