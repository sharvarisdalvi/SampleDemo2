package com.test.pack;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Utils.pack.ExcelOperation;
import com.Utils.pack.SupportUtils;
import com.base.pack.BrowserEventManager;
import com.page.pack.loginpage;

public class BaseClass {

	public  BrowserEventManager event;
	public  WebDriver driver;
	public  Properties prop;

	public  ExcelOperation excel ;

	@BeforeClass
	public void setup() throws Exception
	{
		event = new BrowserEventManager();
		driver= event.getWebDriver();
		excel = new ExcelOperation();
		prop = SupportUtils.getproperty();
		//driver.get("https:www.facebook.com");
		driver.get(prop.getProperty("URL"));
		excel.ReadWorkbook("../Framework/src/test/resources/HealthKart.xlsx"); 
		
	}
	
	@AfterMethod
	public void Testresult(ITestResult result) throws IOException
	{
		if(result.equals(ITestResult.FAILURE))
		{
			event.screenshot(result.getTestName());
		}
		
	}
}
