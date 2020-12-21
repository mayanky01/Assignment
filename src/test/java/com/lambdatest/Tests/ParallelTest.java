package com.lambdatest.Tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParallelTest {

	// Lambdatest Credentails can be found here at https://www.lambdatest.com/capabilities-generator
	String username = System.getenv("LT_USERNAME") == null ? "mayanky01" : System.getenv("LT_USERNAME"); 
	String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "e8fwQ8TfE00g1msyQ0QbfraNGqaXCDtAffGdJ435Mq0QLoGRb5" : System.getenv("LT_ACCESS_KEY");

    public static WebDriver driver;
    public static String currentBrowser;

	@BeforeTest(alwaysRun = true)
	@Parameters(value = { "browser", "version", "platform" })
	protected void setUp(String browser, String version, String platform) throws Exception {
        currentBrowser = browser;
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// set desired capabilities to launch appropriate browser on Lambdatest
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, version);
		capabilities.setCapability(CapabilityType.PLATFORM, platform);
		capabilities.setCapability("build", "TestNG Parallel");
		capabilities.setCapability("name", "TestNG Parallel");
		capabilities.setCapability("network", true);
		capabilities.setCapability("video", true);
		capabilities.setCapability("console", true);
        capabilities.setCapability("visual", true);
       

		// Launch remote browser and set it as the current thread
		String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";
		try {
            driver = new RemoteWebDriver(new URL(gridURL), capabilities);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		} catch (Exception e) {
			System.out.println("driver error");
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void test() throws Exception {

		try {
			// Launch the app
			driver.get("https://www.lambdatest.com/automation-demos/");

			// Login to the application
			driver.findElement(By.id("username")).sendKeys("lambda");
			driver.findElement(By.name("password")).sendKeys("lambda123");
            driver.findElement(By.className("applynow")).sendKeys(Keys.ENTER);

            //Populate Email
            driver.findElement(By.name("email")).sendKeys("mayanky01@gmail.com");
            driver.findElement(By.id("populate")).click();
              
            //Accept Alert
            if (currentBrowser.equalsIgnoreCase("Chrome")){
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        }
            //Choose Frequency of purchase
            driver.findElement(By.id("3months")).sendKeys(Keys.ENTER);

            //Choose decisive factor
            driver.findElement(By.id("discounts")).click(); 

            //select mode of payment
            WebElement mode = driver.findElement(By.id("preferred-payment"));
            Select sel = new Select(mode);
            sel.selectByIndex(2);

            //Accept the terms
            driver.findElement(By.xpath("//input[@type ='checkbox' and @name='tried-ecom']")).click();

            //Move the Slider to 9
            WebElement slider = driver.findElement(By.xpath("//div[@id='slider']/span"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', 'left: 88.8889%;')", slider);
            String expectedValue = "left: 88.8889%;";
            String actualValue =  slider.getAttribute("style");
            Assert.assertEquals(actualValue, expectedValue, "Slider is not at desidered location");

            //Submit the Form
            driver.findElement(By.id("submit-button")).sendKeys(Keys.ENTER);;
            String expectedMessage = "You have successfully submitted the form.";
            String actualMessage = driver.findElement(By.xpath("//div[@id='message']/p")).getText();
            Assert.assertEquals(actualMessage, expectedMessage, "Form is not successfully submitted");
            
            
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	}

	@AfterTest(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
	}

}
