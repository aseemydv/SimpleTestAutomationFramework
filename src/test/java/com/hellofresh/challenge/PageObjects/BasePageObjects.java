package com.hellofresh.challenge.PageObjects;

import com.hellofresh.challenge.Actions.Logout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class that defines and returns webelement for most of the
 * page objects required for signing in, logging in and checking out
 */
public class BasePageObjects {
    public static final By Login = By.className("login") ;
    public static final By userName = By.id("email");
    public static final By password = By.id("passwd");
    public static final By submitLogin = By.id("SubmitLogin");
    public static final By Logout = By.className("logout");

    /**
     * wait until login button is available and return webelement for  the object
     * @param driver webdriver object
     * @return webelement for login button
     */
    public static WebElement getLoginButton(WebDriver driver){
        return new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(Login));
    }

    /**
     * Returns webelement for username text box
     * @param driver webdriver object
     * @return username textfield weblement
     */
    public static WebElement getUserNameTextBox(WebDriver driver){
        return driver.findElement(userName);
    }

    /**
     * Returns webelement for password text box
     * @param driver webdriver object
     * @return password textfield weblement
     */
    public static WebElement getPasswordTextBox(WebDriver driver){
        return driver.findElement(password);
    }

    /**
     * Returns webelement for submit button
     * @param driver webdriver object
     * @return submit button weblement
     */
    public static WebElement getSubmitButton(WebDriver driver){
        return driver.findElement(submitLogin);
    }

    /**
     * Returns webelement for logout button
     * @param driver webdriver object
     * @return logout button weblement
     */
    public static WebElement getLogoutButton(WebDriver driver){
        return new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(Logout));
    }

    /**
     * Returns webelement for a locator requested
     * @param driver webdriver object
     * @param locator By class locator
     * @return requested object's webelement
     */
    public static WebElement navigateElement(WebDriver driver, By locator){
        return driver.findElement(locator);
    }

    /**
     * Returns webelement for a locator requested after waiting until its visible
     * on webpage
     * @param driver webdriver object
     * @param locator By class locator
     * @return requested object's webelement
     */
    public static WebElement waitForElement(WebDriver driver, By locator) {
        return new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
