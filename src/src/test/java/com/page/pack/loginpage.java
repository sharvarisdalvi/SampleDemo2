package com.page.pack;
import org.openqa.selenium.WebDriver;

import com.base.pack.BrowserEventManager;

public class loginpage {

	private BrowserEventManager event;
	private WebDriver driver;
	
	
	//page element xpath
	String EmailId = "xpath__//input[@name='email']";
	String Pwd ="xpath__//input[@name ='pass']";
	String LoginBtn = "xpath__//input[@value='Log In']";
	
	
	public loginpage(BrowserEventManager event)
	{
		this.event= event;
		this.driver = event.getWebDriver();
	}
	
	public void setEmail(String strEmailId) throws InterruptedException
	{
		event.inputText(EmailId, strEmailId);
	}
	
	public void setPwd(String strPwd) throws Exception
	{
		event.inputText(Pwd, strPwd);
	}
	
	public void clickLoginBtn() throws Exception
	{
		event.clickByJS(LoginBtn);
	}
	
	public void LoginHealthkart(String strEmailId,String strPwd) throws Exception
	{
		this.setEmail(strEmailId);
		this.setPwd(strPwd);
		Thread.sleep(1000);
		this.clickLoginBtn();
		
	}
	
}
