package tests;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

    private static WebDriver driver;
    private Properties props = new Properties();

    @BeforeMethod
    public void setup() throws IOException {
       
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
    if (input == null) {
        System.out.println("Sorry, unable to find config.properties");
        return;
    }
    props.load(input);
} catch (IOException ex) {
    ex.printStackTrace();
}

        //WebDriver driver;
        String executionMode = props.getProperty("execution", "local");

        if ("remote".equalsIgnoreCase(executionMode)) {
            // Remote WebDriver
            URL gridUrl = new URL(props.getProperty("webdriver.remote.url", "http://localhost:4444/wd/hub"));
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("chrome");
            driver = new RemoteWebDriver(gridUrl, capabilities);
        } else {
            // Local WebDriver
           driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.get(props.getProperty("url"));
    }

    @Test
    public void testLogin() {
        if (driver == null) {
            throw new RuntimeException("WebDriver was not initialized.");
        }
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.sendKeys(props.getProperty("username"));
        passwordField.sendKeys(props.getProperty("password"));
        loginButton.click();

        WebElement dashboard = driver.findElement(By.id("menu_button_container"));
        Assert.assertTrue(dashboard.isDisplayed(), "Login failed!");
        System.out.println("succesful execution....");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
