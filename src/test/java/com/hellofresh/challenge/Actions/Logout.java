package com.hellofresh.challenge.Actions;

import com.hellofresh.challenge.PageObjects.BasePageObjects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class that handles logging out of the site
 */
public class Logout {
    private WebDriver driver;
    public Logout(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Action that performs logout when already logged in
     */
    public void perform(){
        BasePageObjects.getLogoutButton(driver).click();
    }
}
