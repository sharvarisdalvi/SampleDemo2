package com.test.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Utils.pack.SupportUtils;
import com.base.pack.BrowserEventManager;
import com.page.pack.loginpage;

public class test1 extends BaseClass {

	loginpage lp ;
	
	@Test
	public void HealthKartlogin() throws Exception
	{
		lp = new loginpage(event);
	    excel.GetSheet("Login");
		ArrayList<String> Data = excel.FetchData();
		System.out.println(Data);
		lp.LoginHealthkart(Data.get(0),Data.get(1));
		//lp.LoginHealthkart("test","test");
		
	}
}
