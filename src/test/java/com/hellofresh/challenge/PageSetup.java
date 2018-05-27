package com.hellofresh.challenge;

import com.hellofresh.challenge.Actions.Login;
import com.hellofresh.challenge.Actions.Logout;
import com.hellofresh.challenge.InputGenerator.RandomInput;
import com.hellofresh.challenge.PageObjects.BasePageObjects;
import static com.hellofresh.challenge.PageObjects.BasePageObjects.navigateElement;
import static com.hellofresh.challenge.PageObjects.BasePageObjects.waitForElement;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.plaf.FileChooserUI;
import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.*;

public class PageSetup implements ITestListener {
    private WebDriver driver;
    Capabilities cap;
    private WebDriverWait wait;
    public static Logger logger = Logger.getLogger("hfLogger");
    private static int rowCount;
    String fullName;
    public static final int iteration = 10;
    private String navigate;
    AtomicInteger testCount, testCount2, testCount3, testCount4;
    File file;
    FileInputStream fis;
    FileOutputStream fos;
    Workbook xfWorkbook;
    Sheet sheet;
    RandomInput rin = new RandomInput();

    /**
     * Returns webdriver object
     * @return
     */
    public WebDriver getDriver(){
        return driver;
    }

    /**
     * Takes snapshot, invoked when a test fails
     * @param methodName Name of the failed test
     * @param driver webdriver object
     */
    private void takeSnapShot(String methodName, String browser, WebDriver driver) {
        logger.info("--- Taking snapshot now ");
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenShot = ts.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(screenShot, new File("./Screenshots/"+methodName+"-"+browser+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called for setting up browser for testing
     * @param browserName name of the browser for invoking its webdriver object
     */
    private void setBrowser(String browserName){
        switch (browserName){
            case "chrome":
                driver = initChrome();
                file = new File("Input/credentials-chrome.xlsx");
                break;
            case "firefox":
                driver = initFirefox();
                file = new File("Input/credentials-ff.xlsx");
                break;
            case "edge":
                driver = initEdge();
                file = new File("Input/credentials-edge.xlsx");
                break;
            default:
                System.out.println("Browser name "+browserName+" is invalid.\nBrowser chosen: Firefox");
                driver = initFirefox();
                break;
        }
    }

    /**
     * Initializes Google Chrome webdriver
     * @return chromedriver object
     */
    private static WebDriver initChrome(){
        logger.info("Launching Chrome...");
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Initializes Mozilla Firefox webdriver
     * @return firefox webdriver object
     */
    private static WebDriver initFirefox(){
        logger.info("Launching Firefox...");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Initializes Microsoft Edge webdriver
     * @return edge webdriver object
     */
    private static WebDriver initEdge(){
        logger.info("Launching Microsoft Edge...");
        System.setProperty("webdriver.edge.driver", "resources/MicrosoftWebDriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Gets URL to navigate to before beginning the test
     * @param url website URL
     * @throws IOException
     */
    @Parameters({"url"})
    @BeforeTest
    public void initSetup(String url) throws IOException {
        navigate = url;
    }

    /**
     * Populates an excel sheet with random input,
     * providing email ID, password and username.
     * @param browser specifies the browser for running test
     * @throws IOException
     */
    @Parameters({"browser"})
    @BeforeClass
    public void setUp(String browser) throws IOException {
        setBrowser(browser);
        String fname,lname;
        fis = new FileInputStream(file);
        xfWorkbook = new XSSFWorkbook(fis);
        sheet = (Sheet) xfWorkbook.getSheet("HelloFresh");
        Cell emailCell, passwdCell, userCell;
        rowCount=0;
        while (rowCount<=iteration){
            Row newRow = sheet.createRow(rowCount+1);
            emailCell = newRow.createCell(0);
            emailCell.setCellValue(rin.getRandomEmailId());
            passwdCell = newRow.createCell(1);
            passwdCell.setCellValue(rin.getRandomPassword());
            userCell = newRow.createCell(2);
            userCell.setCellValue(String.join(" ", rin.getRandomFirstName(), rin.getRandomLastName()));
            rowCount++;
        }
        fis.close();
        fos = new FileOutputStream(file);
        xfWorkbook.write(fos);
        fos.close();

        driver.get(navigate);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10, 50);
        cap = ((RemoteWebDriver) driver).getCapabilities();
        testCount = new AtomicInteger(1);
        testCount2 = new AtomicInteger(1);
        testCount3 = new AtomicInteger(1);
        testCount4 = new AtomicInteger(1);
    }

    /**
     * Test for registering a new user on the website
     */
    @Test(priority = 1, invocationCount = 1)
    public void signInTest() {

        int count = testCount.getAndAdd(1);
        Select select;
        BasePageObjects.getLoginButton(driver).click();

        Row row = (Row) sheet.getRow(count);
        String email = row.getCell(0).getStringCellValue();
        String password = row.getCell(1).getStringCellValue();

        String name = row.getCell(2).getStringCellValue().split(" ")[0];
        String surname = row.getCell(2).getStringCellValue().split(" ")[1];

        logger.info("Register with ID: "+email);
        navigateElement(driver, id("email_create")).sendKeys(email);
        navigateElement(driver, id("SubmitCreate")).click();
        if(cap.getBrowserName().toLowerCase().trim().equals("firefox")){
            navigateElement(driver, id("SubmitCreate")).sendKeys("toMakeItWork");
            navigateElement(driver, id("SubmitCreate")).click();
        }
        waitForElement(driver, id("id_gender2")).click();
        navigateElement(driver, id("customer_firstname")).sendKeys(name);
        navigateElement(driver, id("customer_lastname")).sendKeys(surname);
        BasePageObjects.getPasswordTextBox(driver).sendKeys(password);
        logger.info("--- Registered Email: "+email);
        logger.info("--- Password Used: "+password);

        String[] selectValues = {"days", "months", "years", "id_state"};
        for(String values:selectValues){
            select = new Select(navigateElement(driver, id(values)));
            select.selectByIndex(rin.getRandomSelectValues(select.getOptions().size()-1));
        }
        navigateElement(driver, id("company")).sendKeys(rin.getRandomName());
        navigateElement(driver, id("address1")).sendKeys(rin.getRandomAddress());
        navigateElement(driver, id("address2")).sendKeys(rin.getRandomAddress());
        navigateElement(driver, id("city")).sendKeys(rin.getRandomName());
        navigateElement(driver, id("postcode")).sendKeys(rin.getPostalCode());
        navigateElement(driver, id("other")).sendKeys(rin.getRandomName());
        navigateElement(driver, id("phone")).sendKeys(rin.getPhoneNumber());
        navigateElement(driver, id("phone_mobile")).sendKeys(rin.getPhoneNumber());
        navigateElement(driver, id("alias")).sendKeys(rin.getRandomName());
        navigateElement(driver, id("submitAccount")).click();

        WebElement heading =  waitForElement(driver, cssSelector("h1"));

        assertEquals(heading.getText(), "MY ACCOUNT");
        assertEquals(navigateElement(driver, className("account")).getText(), name + " " + surname);
        assertTrue(navigateElement(driver, className("info-account")).getText().contains("Welcome to your account."));
        assertTrue(navigateElement(driver, className("logout")).isDisplayed());
        assertTrue(driver.getCurrentUrl().contains("controller=my-account"));
        new Logout(driver).perform();
    }

    /**
     * Test to check if user is able to login after registration is successful
     */
    @Test(priority = 2, invocationCount = 1)
    public void logInTest() {
        int count = testCount2.getAndAdd(1);
        Row row = (Row) sheet.getRow(count);
        String email = row.getCell(0).getStringCellValue();
        String password = row.getCell(1).getStringCellValue();
        logger.debug("--- Logging-In Using: "+email);
        logger.debug("--- Password: "+password);
        fullName = row.getCell(2).getStringCellValue();
        BasePageObjects.getLoginButton(driver).click();
        new Login(driver).perform(email, password);

        WebElement heading = waitForElement(driver, cssSelector("h1"));

        assertEquals("MY ACCOUNT", heading.getText());
        assertEquals(fullName, driver.findElement(className("account")).getText());
        assertTrue(navigateElement(driver, className("info-account")).getText().contains("Welcome to your account."));
        assertTrue(navigateElement(driver, className("logout")).isDisplayed());
        assertTrue(driver.getCurrentUrl().contains("controller=my-account"));
        new Logout(driver).perform();
    }

    /**
     * Test for checking out process for an already existing user.
     */
    @Test(priority = 3, invocationCount = 1)
    public void checkoutTest() {
        int count = testCount3.getAndAdd(1);

        Row row = (Row) sheet.getRow(count);
        String email = row.getCell(0).getStringCellValue();
        String password = row.getCell(1).getStringCellValue();

        waitForElement(driver, className("login")).click();
        new Login(driver).perform(email, password);

        waitForElement(driver, linkText("Women")).click();
        navigateElement(driver, xpath("//a[@title='Faded Short Sleeve T-shirts']/ancestor::li")).click();

        if(cap.getBrowserName().toLowerCase().trim().equals("firefox")) {
            waitForElement(driver, name("Submit")).click();
        }
        else {
            navigateElement(driver, xpath("//a[@title='Faded Short Sleeve T-shirts']/ancestor::li")).click();
            waitForElement(driver, name("Submit")).click();
        }
        waitForElement(driver, xpath("//*[@id='layer_cart']//a[@class and @title='Proceed to checkout']")).click();
        waitForElement(driver, xpath("//*[contains(@class,'cart_navigation')]/a[@title='Proceed to checkout']")).click();
        waitForElement(driver, name("processAddress")).click();
        waitForElement(driver, id("uniform-cgv")).click();
        navigateElement(driver, name("processCarrier")).click();
        waitForElement(driver, className("bankwire")).click();
        waitForElement(driver, xpath("//*[@id='cart_navigation']/button")).click();
        WebElement heading = waitForElement(driver, cssSelector("h1"));

        assertEquals("ORDER CONFIRMATION", heading.getText());

        assertTrue(navigateElement(driver, xpath("//li[@class='step_done step_done_last four']")).isDisplayed());
        assertTrue(navigateElement(driver, xpath("//li[@id='step_end' and @class='step_current last']")).isDisplayed());
        assertTrue(navigateElement(driver, xpath("//*[@class='cheque-indent']/strong")).getText().contains("Your order on My Store is complete."));
        assertTrue(driver.getCurrentUrl().contains("controller=order-confirmation"));
        new Logout(driver).perform();
    }

    /**
     * Test to check if the user's credentials are requsted for signing in
     * after checking out as a guest user.
     */
    @Test(priority = 4, invocationCount = 1)
    public void checkoutAndLoginTest() {
        int count = testCount4.getAndAdd(1);

        Row row = (Row) sheet.getRow(count);
        String email = row.getCell(0).getStringCellValue();
        String password = row.getCell(1).getStringCellValue();

        waitForElement(driver, linkText("Women")).click();
        navigateElement(driver, xpath("//a[@title='Faded Short Sleeve T-shirts']/ancestor::li")).click();
        if(cap.getBrowserName().toLowerCase().trim().equals("firefox")) {
            waitForElement(driver, name("Submit")).click();
        }
        else {
            navigateElement(driver, xpath("//a[@title='Faded Short Sleeve T-shirts']/ancestor::li")).click();
            waitForElement(driver, name("Submit")).click();
        }

        waitForElement(driver, xpath("//*[@id='layer_cart']//a[@class and @title='Proceed to checkout']")).click();
        waitForElement(driver, xpath("//*[contains(@class,'cart_navigation')]/a[@title='Proceed to checkout']")).click();
        new Login(driver).perform(email, password);
        waitForElement(driver, name("processAddress")).click();
        waitForElement(driver, id("uniform-cgv")).click();
        navigateElement(driver, name("processCarrier")).click();
        waitForElement(driver, className("bankwire")).click();
        waitForElement(driver, xpath("//*[@id='cart_navigation']/button")).click();
        WebElement heading = waitForElement(driver, cssSelector("h1"));

        assertEquals("ORDER CONFIRMATION", heading.getText());

        assertTrue(navigateElement(driver, xpath("//li[@class='step_done step_done_last four']")).isDisplayed());
        assertTrue(navigateElement(driver, xpath("//li[@id='step_end' and @class='step_current last']")).isDisplayed());
        assertTrue(navigateElement(driver, xpath("//*[@class='cheque-indent']/strong")).getText().contains("Your order on My Store is complete."));
        assertTrue(driver.getCurrentUrl().contains("controller=order-confirmation"));
        new Logout(driver).perform();
    }

    /**
     * executes after all tests are covered
     * for closing the opened webdriver object
     */
    @AfterTest
    public void tearDown() {
        driver.close();
    }

    /**
     * Overrides the functionality of what happens when a test fails,
     * captures a snapshot when a test is found failed.
     * @param result ITestResult object
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.warn("***** ERROR: "+result.getName()+" Test failed");
        Object currentClass = result.getInstance();
        WebDriver driver = ((PageSetup) currentClass).getDriver();
        String methodName = result.getName().toString().trim();
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browser = cap.getBrowserName().toLowerCase().trim();
        takeSnapShot(methodName, browser, driver);
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {}

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}