package org.openmrs.module.quarterlyreporting.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;

@Ignore
@ContextConfiguration("file:src/main/resources/moduleApplicationContext.xml")//TODO just added for troubleshooting purposes, gets loaded automatically by openmrs
public class QuarterlyReportingServiceTest extends BaseModuleContextSensitiveTest {

	@Before
	public void initialise() {
		System.out.println(applicationContext);
	}
	
	@Test
	public void currentServiceShouldBeWellInitialised() {
		Assert.assertNotNull(Context.getService(QuarterlyReportingService.class));
	}
}
