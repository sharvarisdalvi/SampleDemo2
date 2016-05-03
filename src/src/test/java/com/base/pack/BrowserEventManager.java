package com.base.pack;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.PhantomReference;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.Utils.pack.SupportUtils;



/**
 * @version 1.0
 * @since 10 Sep 2015
 * @author Atul.sharma
 * Create object of this class in order to interact with the browser. 
 * No constructor is required. 
 */
public class BrowserEventManager {
	//Defining constructor of webdriver. 
	private WebDriver driverObject = null; 
	private int explicitWait = 40;
	private Robot robotInstance = null;
	private Actions actions = null;
	private WebDriverWait ExplicitwaitObject; 
	private JavascriptExecutor ScriptExecuterObject = null; 
	private int implicitWait = 180;
	

	/**
	 * @author Atul.sharma
	 * User this method to set the implicit wait.
	 */
	public void setImplicitWait(int ImplicitWaitByUser){
		implicitWait = ImplicitWaitByUser;
	}
	public BrowserEventManager() throws Exception{
		this.driverObject = initializeAndReturnDriver();
		getScriptExecuterObject();
		getActionsInstance(this.driverObject);	
		getExplicitWaitInstance();
		
	}
	
	
	public WebDriver getWebDriver(){
		return driverObject;
	}

	private WebDriver initializeAndReturnDriver() throws Exception{
		
		System.out.println("Running on Remote Machine:"+SupportUtils.IsRemoteEnvironment());
		
		if (SupportUtils.IsRemoteEnvironment()) {
			Proxy proxy = new Proxy();
			//proxy.setHttpProxy("10.10.3.36:3128");
			DesiredCapabilities cp = DesiredCapabilities.firefox();
			cp.setCapability(CapabilityType.PROXY, proxy);
			System.out.println("Running in Remote Location");
			try{
			this.driverObject = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cp);
			}catch(Exception e){
			Reporter.log("---------Exception in initializing browser TRY ATTEMPT: 1 ------------"+e.getStackTrace());
			this.driverObject = new RemoteWebDriver(new URL("http://10.10.6.162:4444/wd/hub"),cp);	
			}
		} else {
			
			try{
				String name = SupportUtils.Browser();
				System.out.println("Running on local machine on "+name.toUpperCase()+" browser");
					if(name.contains("firefox")){
						ProfilesIni prof = new ProfilesIni();
						FirefoxProfile profile = prof.getProfile("Selenium");
						this.driverObject = new FirefoxDriver(profile);
						this.driverObject.manage().window().maximize();
						this.driverObject.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					}

					if(name.contains("phantom"))
					{
						DesiredCapabilities capability = new DesiredCapabilities(); 
						//capability.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\Software\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
						capability.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, SupportUtils.path());
						
						this.driverObject = new PhantomJSDriver(capability); 
						this.driverObject.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); 

					}
					if(name.contains("chrome"))
					{
						//System.setProperty("webdriver.chrome.driver","D:/Software/chromedriver_win32/chromedriver.exe");
						System.setProperty("webdriver.chrome.driver",SupportUtils.path());
						this.driverObject = new ChromeDriver();
						this.driverObject.manage().window().maximize();
						this.driverObject.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					}
					if (name.contains("ie"))
					{
						System.out.println("Executing on IE");
						System.setProperty("webdriver.ie.driver", SupportUtils.path());
						this.driverObject = new InternetExplorerDriver();
						this.driverObject.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
						this.driverObject.manage().window().maximize();
					}

			}   catch(Exception e){}
			}
		this.driverObject.manage().window().maximize();
		this.driverObject.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return this.driverObject;
		
		
	

	}
	private Actions getActionsInstance(WebDriver driver) {
		if(actions== null){
			actions = new Actions(this.driverObject);
		}
		return this.actions;
	}



	public Actions getActionsObject(){
		return this.actions;
	}

	private WebDriverWait getExplicitWaitInstance() {
		if(ExplicitwaitObject == null){
			ExplicitwaitObject = new WebDriverWait(this.driverObject, this.explicitWait);
		}
		return ExplicitwaitObject;
	}


	private JavascriptExecutor getScriptExecuterObject() {
		if(ScriptExecuterObject == null){
			ScriptExecuterObject = (JavascriptExecutor)this.driverObject;
		}
		return ScriptExecuterObject;
	}

	public JavascriptExecutor getJSExecuter(){
		return this.ScriptExecuterObject;
	}

	@SuppressWarnings("unused")
	private void highlightElement(WebElement element) { 
		try{
			for (int i = 0; i < 5; i++) { 
				WrapsDriver wrappedElement = (WrapsDriver) element; 
				JavascriptExecutor driver = (JavascriptExecutor) wrappedElement.getWrappedDriver(); 
				driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: green; border: 2px solid yellow;"); 
				driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, ""); 
				wrappedElement = null;
				driver = null;	
			}
		}
		catch (Exception e){
			System.out.println(e.getStackTrace());
		}


	} 

	/**
	 * @author Atul.sharma
	 * User this method to get the actual element identifier. 
	 * Example: On supplying path of particular element from OR, this method will return the id/xpath/css etc.
	 * xpath__//[@id='123'] will return //[@id='123']
	 * @param LogicalIdentifierOfElement -  <OR>.<PageName><ElementName>
	 * @return Actual Element identifier from the OR
	 */
	public String getElementIdentifier(String LogicalIdentifierOfElement){

		String[] ElementIdentifier = LogicalIdentifierOfElement.split("__");
		return ElementIdentifier[1];


	}


	public WebElement findElement(String ElementIdentifier, String operation) throws InterruptedException{
		//Waiting for page to load fully on the screen.
		//this.getScriptExecuterObject().executeScript("$(document).ready(function(){});");

		WebElement ElementToReturn = null; 
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>" + this.driverObject.getCurrentUrl() +  "");
		String OperationOnElement =  operation;

		// Checking if element identifer is null. 
		if(ElementIdentifier.isEmpty()){
			System.out.println("WARNING||"+ driverObject.getCurrentUrl()+"||" + "ELEMENT IDENTIFIER IS NOT DEFINED ON" + driverObject.getTitle());		
			return ElementToReturn;
		}
		// Following code will run when page is loads on the screen without any issue and element identifier is not null. 
		else{
			//Trimming Element Identifier.
			ElementIdentifier = ElementIdentifier.trim();

			//	System.out.println("FINDING ELMENT ON||"+ this.driverObject.getCurrentUrl()+"||" + "WITH PARAMETER||" + ElementIdentifier);



			String[] ElementIdentiferAfterSplit = ElementIdentifier.split("__");
			String FindElementWith = ElementIdentiferAfterSplit[0];
			String Identifier = ElementIdentiferAfterSplit[1];


			if(FindElementWith.equalsIgnoreCase("xpath")){


				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 

					try{
						Thread.sleep(500);
						this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.xpath(Identifier)));
						return this.driverObject.findElement(By.xpath(Identifier));
					}catch (Exception e){
						System.out.println(e.getMessage());
						return this.driverObject.findElement(By.xpath(Identifier));
					}



				}else{
					try{
						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Identifier)));
						return this.driverObject.findElement(By.xpath(Identifier));	
					}catch(Exception e){
						System.out.println(e.getMessage());
						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Identifier)));
						return this.driverObject.findElement(By.xpath(Identifier));		
					}
				}
			}

			if(FindElementWith.equalsIgnoreCase("id")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					try{ 
						this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.id(Identifier)));
						return this.driverObject.findElement(By.id(Identifier));
					}catch(Exception e){
						System.out.println(e.getMessage());
						this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.id(Identifier)));
						return this.driverObject.findElement(By.id(Identifier));
					}
				}else{
					try{
						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.id(Identifier)));
						return this.driverObject.findElement(By.id(Identifier));
					}catch(Exception e){
						System.out.println(e.getMessage());
						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.id(Identifier)));
						return this.driverObject.findElement(By.id(Identifier));
					}
				}
			}

			if(FindElementWith.equalsIgnoreCase("name")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 
					try{

						this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.name(Identifier)));
						return this.driverObject.findElement(By.name(Identifier));	
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.name(Identifier)));
						return this.driverObject.findElement(By.name(Identifier));
					}
				}else{
					try{

						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.name(Identifier)));
						return this.driverObject.findElement(By.name(Identifier));	
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.name(Identifier)));
						return this.driverObject.findElement(By.name(Identifier));		
					}
				}
			}
			if(FindElementWith.equalsIgnoreCase("css")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 
					this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.cssSelector(Identifier)));
					return this.driverObject.findElement(By.cssSelector(Identifier));
				}else{
					this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Identifier)));
					return this.driverObject.findElement(By.cssSelector(Identifier));	
				}
			}

			if(FindElementWith.equalsIgnoreCase("classname")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 
					this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.className(Identifier)));
					return this.driverObject.findElement(By.className(Identifier));
				}else{
					this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.className(Identifier)));
					return this.driverObject.findElement(By.className(Identifier));	
				}
			}
			if(FindElementWith.equalsIgnoreCase("linktext")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 
					this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.linkText(Identifier)));
					return this.driverObject.findElement(By.linkText(Identifier));
				}else{
					this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.linkText(Identifier)));
					return this.driverObject.findElement(By.linkText(Identifier));	
				}
			}

			if(FindElementWith.equalsIgnoreCase("partiallinktext")){
				if(OperationOnElement.equalsIgnoreCase("click")){
					// Special code for click operation checking that element should clickable before the scrpt clicks on it. 
					this.ExplicitwaitObject.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(Identifier)));
					return this.driverObject.findElement(By.partialLinkText(Identifier));
				}else{
					this.ExplicitwaitObject.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(Identifier)));
					return this.driverObject.findElement(By.partialLinkText(Identifier));	
				}
			}



		}
		return ElementToReturn;


	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)And finally click on it.  
	 * @param String - ElementIdentifier
	 */
	public void clickSimple(String ElementIdentifier) throws Exception{

		//highlightElement(findElement(ElementIdentifier,"click"));

		findElement(ElementIdentifier,"click").click();

	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)Use the Actions class of Webdriver to click on element.
	 * d)Move to focus to the element you want to click
	 * e)And finally click on it.\
	 * {Use this method if normal click does not work. }  
	 * @param String - ElementIdentifier
	 */
	public void clickByActionClass(String ElementIdentifier) throws InterruptedException{
		// highlightElement(findElement(ElementIdentifier,"click"));
		getActionsInstance(this.driverObject).moveToElement(findElement(ElementIdentifier,"click")).click().perform();
	}

	/**
	 * @author Atul.sharma
	 * @param ElementIdentifier
	 * @param keys
	 * Use this method to key press operations. You need to pass on the element identifier from OR and keys.<key you want to press>
	 */

	public void keyPress(String ElementIdentifier, Keys key) throws InterruptedException{
		this.findElement(ElementIdentifier, "").sendKeys(key);
	}


	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about to get text from.
	 * c) get the text of the element anre return it
	 *  {Use this method to get the text of an element.}  
	 * @param String - ElementIdentifier
	 */
	public String getTextOfWebElement(String ElementIdentifier) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,""));
		WebElement elementToGetTextFrom = this.findElement(ElementIdentifier, "");
		String elementText = elementToGetTextFrom.getText();
		return elementText;
	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about to get text from.
	 * c) get the text of the element anre return it
	 *  {Use this method to get the text of an element.}  
	 * @param String - ElementIdentifier
	 */
	public boolean checkElementPresentOrNot(String ElementIdentifier) throws InterruptedException{
		try{
			findElement(ElementIdentifier, "");
			return true; 
		}
		catch (Exception e){
			return false;
		}

	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)Use JavaScript to click on the element. 
	 * {This method will click on the web element using java script. Use this method if normal click/ action class click does not work.}  
	 * @param String - ElementIdentifier
	 */

	public void clickByJS(String ElementIdentifier) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,"click"));
		this.ScriptExecuterObject.executeScript("arguments[0].click();", findElement(ElementIdentifier,"click"));
	}
	public void inputByJS(String ElementIdentifier,String inputvalue) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,"click"));
		this.ScriptExecuterObject.executeScript("arguments[0].setAttribute('value', '" + inputvalue +"')", findElement(ElementIdentifier,"click"));
	}


	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)Find the Web elements present in the given element identifier. 
	 * {Supply Elementidentifier and tag name to this function}  
	 * @param String - ElementIdentifier
	 * @param String - Tag name
	 * @return List<WebElement>
	 */

	public List<WebElement> findElementsInWebElement(String ElementIdentifier, String tagname) throws InterruptedException{
		return this.findElement(ElementIdentifier, "click").findElements(By.tagName(tagname));
		//return this.driverObject.findElements(By.xpath(getElementIdentifier(ElementIdentifier)));

	}

	/**
	 * @author Atul.sharma
	 * This method will return the list of web elements. 
	 * @param String - xpath of element
	 * @return List<WebElement>
	 */

	public List<WebElement> findElements(String ElementIdentifier) throws InterruptedException{
		return this.driverObject.findElements(By.xpath(this.getElementIdentifier(ElementIdentifier)));


	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)Use the Actions class of Webdriver to double click on element.
	 * d)Move to focus to the element you want to click
	 * e)And finally double click on it.\
	 * {Use this method if want to double click on a webelement.}  
	 * @param String - ElementIdentifier
	 */
	public void clickDoupbleClick(String ElementIdentifier) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,"click"));
		getActionsInstance(this.driverObject).doubleClick(findElement(ElementIdentifier,"click")).perform();

	}

	/**
	 * @author Atul.sharma
	 * This method will,
	 * a)Wait for page to load fully on your screen. 
	 * b)Highlight the element it is about click.
	 * c)Find the element on UI and input the text you supply to this method. 
	 * {This method will input the text in a text box. }  
	 * @param String - ElementIdentifier
	 * @param String - You need to pass on the text you want to input.  
	 */

	public void inputText(String ElementIdentifier,String inputText) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,""));
		findElement(ElementIdentifier,"").clear();
		findElement(ElementIdentifier,"").sendKeys(inputText);

	}

	public void inputTextWithOutClearTextField(String ElementIdentifier,String inputText) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,""));
		findElement(ElementIdentifier,"").sendKeys(inputText);

	}

	/**
	 * @author Atul.sharma
	 * This method will check the text present in a web element/ui component. 
	 * @param String - ElementIdentifier
	 * @param String - You need to pass on the text you want to verify. This is case insensitive. 
	 * @return - No Error if title mathes else AssertionError. 
	 */
	public void inputFileForBrowse(String ElementIdentifier,String inputText) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,""));
		findElement(ElementIdentifier,"").sendKeys(inputText);
	}

	/**
	 * @author Atul.sharma
	 * This method will check the text present in a web element/ui component. 
	 * @param String - ElementIdentifier
	 * @param String - You need to pass on the text you want to verify. This is case insensitive. 
	 * @return - No Error if title mathes else AssertionError. 
	 */
	public void assertTextPresentInElement(String ElementIdentifier,String textToVerify) throws InterruptedException{
		//highlightElement(findElement(ElementIdentifier,""));
		String textRetrunedFromElement = findElement(ElementIdentifier,"").getText();
		textRetrunedFromElement = textRetrunedFromElement.toLowerCase();
		textToVerify = textToVerify.toLowerCase();
		Assert.assertEquals(textRetrunedFromElement,textToVerify);
	}

	/**
	 * @author Atul.sharma
	 * This method will check the title of the page. 
	 * @param String - You need to pass on the page title you want to verify. This is case insensitive. 
	 * @return - No Error if title mathes else AssertionError. 
	 */
	public void assertPageTitle(String expectedTitle) throws InterruptedException{
		System.out.println(this.driverObject.getTitle().toLowerCase());
		String actualTitle = this.driverObject.getTitle().toLowerCase();
		Assert.assertEquals(actualTitle,expectedTitle.toLowerCase());
	}

	/**
	 * @author Atul.sharma
	 * This method will focus on active element.  
	 */
	public void focusOnActiveElement() throws InterruptedException{
		this.driverObject.switchTo().activeElement();
	}

	/**
	 * @author Atul.sharma
	 * This method will press ESC button.   
	 */

	public void pressESC(String ElementIdentifier) throws InterruptedException{
		robotInstance.keyPress(KeyEvent.VK_ESCAPE);	
		this.findElement(ElementIdentifier, "").sendKeys(Keys.ESCAPE);
		this.actions.sendKeys(Keys.ESCAPE).build().perform();
	}
	/**
	 * @author Atul.sharma
	 * This method will press ESC button.   
	 */

	public void executeJS(String JavaScript) throws InterruptedException{
		this.ScriptExecuterObject.executeScript(JavaScript);
	}
	public void scrollbyJS(int x ,int y)
	{
		this.ScriptExecuterObject.executeScript("scroll("+ x+","+y+")");
	}

	/**
	 * @author Atul.sharma
	 * Use this method to set the explict wait.    
	 */
	public void setExplicitWait(int i){
		if(i>=60){
			System.out.println("WARNING: Avaoid setting explict wait. You can pass it while creating object of BrowserEventManager class.");
			System.out.println("You have set the explicity wait greater than 1 minute. Kindly reduce it for better performance.");
		}
		this.explicitWait = i;
	}

	/**
	 * @author Atul.sharma
	 * Use this method to get the current explicit wait setup in the browsereventmanager object.    
	 */
	public int getExplicitWait(){
		return this.explicitWait;
	}

	/**\
	 * @author Atul.sharma
	 * @param ElementIdentifier
	 * @param dropdownValue
	 * @throws InterruptedException
	 */
	public void selectdropdown(String ElementIdentifier,String dropdownVisisbleText) throws InterruptedException{
		//Get the Dropdown as a Select using its name attribute 
		Select selectObject = new Select(findElement(ElementIdentifier,"")); 
		Thread.sleep(1000);
		selectObject.selectByVisibleText(dropdownVisisbleText);
	}
	public void selectdropdownByValue(String ElementIdentifier,String dropdownValue) throws InterruptedException{
		//Get the Dropdown as a Select using its name attribute 
		Select selectObject = new Select(findElement(ElementIdentifier,"")); 
		selectObject.selectByValue(dropdownValue);
	}
	/**
	 * @author Atul.sharma
	 * @param ElementIdentifier
	 * @param dropdownValue
	 * @throws InterruptedException
	 */ 
	//    public void closeBrowser(){
	//    	if (CommonUtils.isRunningJar() || CommonUtils.isRemoteEnvironment()) 
	//    	this.driverObject.quit();
	//    }

	/**
	 * @deprecated
	 * @author Atul.sharma
	 * @param ElementIdentifier
	 * @throws InterruptedException
	 */ 
	public void mouseHover(String ElementIdentifier) throws InterruptedException{
		this.getActionsObject().moveToElement(this.findElement(ElementIdentifier, "")).perform();

	}

	/**
	 * @author Atul.sharma
	 * @param ElementIdentifier
	 * @return current url of the browser.
	 * @throws InterruptedException
	 */ 
	public String getCurrentURL() throws InterruptedException{
		return this.driverObject.getCurrentUrl();
	}


	public void jsForFileUpload(String ElementIdentifier) throws InterruptedException{
		this.ScriptExecuterObject.executeScript("arguments[0].setAttribute('class','')", findElement(ElementIdentifier,""));

	}


	public boolean waitForElementToBePresent(String ElementIdentifier,int second) throws InterruptedException{
		int user_set_seconds = second; 
		int explicitWaitSetInthisClass = this.getExplicitWait();
		this.setExplicitWait(user_set_seconds);
		try{
			this.findElement(ElementIdentifier, "");
			return true;

		}catch(Exception e){
			System.out.println("Unable to locate element with " + ElementIdentifier);
			return false;
		}
		finally{
			this.setExplicitWait(explicitWaitSetInthisClass);
		}


		//WebDriverWait wait= new WebDriverWait(this.driverObject, second);
		//wait.until(ExpectedConditions.presenceOfElementLocated((By)findElement(ElementIdentifier, "")));

	}
	public boolean waitForElementUntil(String ElementIdentifier,int second) throws InterruptedException{
		int user_set_seconds = second; 
		this.setExplicitWait(user_set_seconds);
		this.findElement(ElementIdentifier, "").isDisplayed();
		return true;
	}
	private String parentWindowId;
	public void setParentWindowID(){
		parentWindowId = this.driverObject.getWindowHandle();
	}
	public void getParentWindowID(){
		parentWindowId = this.driverObject.getWindowHandle();
	}

	public void switchToMainWindow(){
		this.driverObject.switchTo().window(parentWindowId);
	}
	public void screenshot(String screenshotname) throws IOException{
		File scrFile = ((TakesScreenshot)this.driverObject).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("../Framework/screenshot/"+screenshotname+".png"));
		//FileUtils.copyFile(scrFile, new File(BrowserEventManager.class.getClassLoader().getResourceAsStream("screenshot").toString()+screenshotname+".png"));
	}

	/**
	 * @author Atul.sharma
	 * @param NameOfWindow
	 * User this method to switch the child window opened by the webdriver. You need to use switchToMainWindow in order to switch back to the original window. 
	 */
	public void switchToWindow_By_Name(String NameOfWindow){

		try {
			this.driverObject.switchTo().window(NameOfWindow);
		} catch (NoSuchWindowException e) {
			e.printStackTrace();
			System.out.println("Unable to switch to window with name: " + NameOfWindow);
		}

	}

	/**
	 * @author Atul.sharma
	 * @param titleOfPage
	 */
	public void switchToWindow_By_Title(String titleOfPage){
		Set<String> allWindows = this.driverObject.getWindowHandles();
		if(!allWindows.isEmpty()) {

			for (String windowId : allWindows) {

				try { 
					if(this.driverObject.switchTo().window(windowId).getTitle().equals(titleOfPage)) {
						System.out.println("You are on child window with title: "+ titleOfPage );

						break;
					}
				}
				catch(NoSuchWindowException e) {
					System.out.println("Unable to move to child window with title: "+ titleOfPage );
					e.printStackTrace();
				}
			}
		}   	
	}
	/**
	 * @author Atul.sharma
	 * @param contentOfThePage
	 */
	public void switchToWindow_By_PageContent(String contentOfThePage){
		Set<String> allWindows = this.driverObject.getWindowHandles();
		if(!allWindows.isEmpty()) {
			for (String windowId : allWindows) {
				this.driverObject.switchTo().window(windowId);
				if(this.driverObject.getPageSource().contains(contentOfThePage)) {
					try {
						System.out.println("Moved to child window");
						break;
					} catch(NoSuchWindowException e) {
						e.printStackTrace();
						System.out.println("Unable to move to child window. ");
					}
				}
			}
		}

	}



	public void closeAllBrowserInstancesOnSeleniumGrid()
			throws InterruptedException {
		FirefoxDriver driver = new FirefoxDriver();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://10.10.6.162:4444/grid/console");

		List<WebElement> totalGridSessions = driver
				.findElementsByXPath("//p[@class='proxyid']");
		System.out.println("TOTAL NODE INSTANCE COUNT: "
				+ totalGridSessions.size());

		Iterator<WebElement> itr = totalGridSessions.iterator();
		while (itr.hasNext()) {
			WebElement nodeName = itr.next();
			String url = nodeName.getText().substring(5, 28) + "/wd/hub";
			System.out.println("CLOSING ALL THE BROWSER INSTANCES ON: "+url);

			FirefoxDriver driver2 = new FirefoxDriver();
			driver2.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
			driver2.manage().window().maximize();
			driver2.get(url);
			List<WebElement> totalInstancesOfFirefox = driver2
					.findElementsByXPath("//div[contains(text(),'Firefox')]");
			if ((totalInstancesOfFirefox.size()) != 0) {
				Iterator<WebElement> itr2 = totalInstancesOfFirefox.iterator();
				while (itr2.hasNext()) {
					WebElement browserInstance = itr2.next();
					Thread.sleep(2000);
					browserInstance.click();
					Thread.sleep(2000);
					driver2.findElement(
							By.xpath("//button[contains(text(),'Delete Session')]"))
							.click();
					driver2.findElementByXPath(
							"//button[(contains(text(),'OK'))]").click();
				}

			}
			driver2.quit();
		}

		driver.quit();

	}


}




