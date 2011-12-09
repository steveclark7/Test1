package com.ca.cloudcommons.smiapitests;

import java.util.Properties;

public class UiProperties {

	public static String PROPS = "tests.properties";
	
	private Properties ourProps;
	
	public UiProperties() {
		ourProps = Util.load(this, PROPS);
	}
	

//	UiProperties(Properties ourProps) {
//		this.ourProps = ourProps;
//	}

	public String getUser() {
		return ourProps.getProperty("User", "a.user@ca.com");
	}

	public String getPassword() {
		return ourProps.getProperty("Password", "password");
	}

	public String getProxy() {
		return ourProps.getProperty("Proxy", "");
	}

	public int getProxyPort() {
		String value = ourProps.getProperty("ProxyPort", "80");

		int retval = 0;
		try {
			retval = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			// defaults to 0
		}

		return retval;
	}

	public String getProxyUser() {
		return ourProps.getProperty("ProxyUser", "p.user@ca.com");
	}

	public String getProxyPassword() {
		return ourProps.getProperty("ProxyPassword", "password");
	}

	
	public String getAdminUser() {
		return ourProps.getProperty("AdminUser", "Admin.user@ca.com");
	}

	public String getAdminPassword() {
		return ourProps.getProperty("AdminPassword", "password");
	}
	
	public String getCloudSite() {
		return ourProps.getProperty("CloudSite", "50.57.192.171");
	}

	public String getProject() {
		return ourProps.getProperty("Project", "Project");
	}

	public int getDebugLevel() {
		String value = ourProps.getProperty("DebugLevel", "0");

		int retval = 0;
		try {
			retval = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			// defaults to 0
		}

		return retval;
	}

}
