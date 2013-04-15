package com.rest.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomateRestTest {	
	
	private final static Logger logger = LoggerFactory.getLogger(AutomateRestTest.class);
	
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(RestTest.class);
      for (Failure failure : result.getFailures()) {
    	  logger.info(failure.toString());
      }
      logger.info("Result :"+result.wasSuccessful());
   }	
}
