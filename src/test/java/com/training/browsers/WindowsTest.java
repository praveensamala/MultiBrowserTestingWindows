package com.training.browsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WindowsTest {
	
	ChromeOptions options = new ChromeOptions();
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    DesiredCapabilities capabilities=DesiredCapabilities.firefox();
    
	@BeforeClass
	public void beforeSuite() {
		System.out.println("\n*****user working directory : "+System.getProperty("user.dir"));
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");
		//Below code is disable notifications from chrome browser during the testing
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);  
		options.setExperimentalOption("prefs", prefs);
		
		System.setProperty("webdriver.gecko.driver",  "geckodriver.exe");
		FirefoxProfile profile = new FirefoxProfile();
	    profile.setPreference("permissions.default.desktop-notification", 1);
	    capabilities.setCapability(FirefoxDriver.PROFILE, profile);
	}
	
	@Test (enabled = true)
	public void loginTest1() {		
		WebDriver driver = new ChromeDriver(options);
		facebookLoginTest(driver);
		driver.close();
	}
	
	@Test (enabled = true)
	public void loginTest2() {
	    @SuppressWarnings("deprecation")
		WebDriver driver = new FirefoxDriver(capabilities);
	    facebookLoginTest(driver);
		driver.close();
	}
	
	public static void facebookLoginTest(WebDriver driver) {
		driver.get("https://www.facebook.com");
		driver.manage().window().maximize();
		
		driver.findElement(By.id("email")).sendKeys("gamecheck280@gmail.com");
		driver.findElement(By.id("pass")).sendKeys("system123");
		driver.findElement(By.name("login")).click();
		
		waitForPageLoading(driver);
		
		List <WebElement> list = new ArrayList<WebElement>();
		list = driver.findElements(By.tagName("span"));
		boolean userfound = false;
		for(WebElement e: list) {
			//System.out.println("\n****gettext : "+e.getText());
			if (e.getText().contains("Gamecheck")) {
				userfound = true;
				break;
			}
		}
		
		driver.findElement(By.xpath("//div[@aria-label='Account']")).click();
		driver.findElement(By.xpath("//span[contains(text(), 'Log Out')]")).click();
		waitForPageLoading(driver);
		
		AssertJUnit.assertTrue(userfound);
	}
	
	@AfterClass
	public void afterTest() {
		
	}
	
	public static void waitForPageLoading(WebDriver driver) {
		int networkCallsLengthBefore = 0;
		int networkCallsLengthAfter = 0;
		System.out.println("***starting waitForPageRendering()");
		for (int i = 0; i < 45; i++) {
			try {
				String scriptToExecute = "var network = performance.getEntries(); return network;";
				String networkCalls = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
				networkCallsLengthBefore = networkCallsLengthAfter;
				networkCallsLengthAfter = networkCalls.length();				
				System.out.println(networkCallsLengthBefore+" : "+networkCallsLengthAfter);
				if (networkCallsLengthBefore == networkCallsLengthAfter && networkCalls.contains("entryType=paint,")) break;
				System.out.println("praveen kumar samala - my own code: waiting for the page to be rendered...");
			}
			catch (Exception e) {
				System.out.println("inspect error");
			}
			finally {
				sleep(1);
			}
		}
		System.out.println("***ending waitForPageLoading()"+"\n");
	}
	
	public static void sleep(int n) {
		try {
			Thread.sleep(n*1000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
