package com.hellofresh.challenge.Actions;

import com.hellofresh.challenge.PageObjects.BasePageObjects;
import org.openqa.selenium.WebDriver;

/**
 * Class that handles logging in for an already registered
 * user
 */
public class Login {
    private WebDriver driver;
    public Login(WebDriver driver){
        this.driver = driver;
    }

    /**
     * Actions that perform login bby submitting the form field
     * with username and password.
     * @param username registered username
     * @param password password of the user
     */
    public void perform(String username,String password){
        BasePageObjects.getUserNameTextBox(driver).sendKeys(username);
        BasePageObjects.getPasswordTextBox(driver).sendKeys(password);
        BasePageObjects.getSubmitButton(driver).click();
    }
}