package com.ca.cloudcommons.smiapitests;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class RestApi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JUnitCore runner = new JUnitCore();
		runner.addListener(new TextListener(System.out));
		runner.run(AllTests.class);
	}

}
