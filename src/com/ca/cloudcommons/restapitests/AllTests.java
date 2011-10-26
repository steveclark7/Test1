package com.ca.cloudcommons.restapitests;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs tests defined for this test suite.
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { TestInfo.class, TestRead.class, TestUpload.class })
public class AllTests {

    /**
     * Executed when this test suite is finished
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

}
