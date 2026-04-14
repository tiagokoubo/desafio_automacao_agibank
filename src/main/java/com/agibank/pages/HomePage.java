package com.agibank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By btnSearchIcon = By.cssSelector(".ast-search-icon");
    private By searchField = By.cssSelector(".search-field");
    private By btnSearchSubmit = By.id("search_submit");
    private By articleResultLinks = By.cssSelector("#main .ast-row a");
    private By noResultsMessage = By.cssSelector(".no-results p");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSearchIcon)).click();
    }

    public String getNoResultsMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).getText();
    }

    public void searchFor(String term) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchField)).sendKeys(term);
        driver.findElement(btnSearchSubmit).click();
    }

    public List<String> getArticleTitles() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(articleResultLinks));
        
        return driver.findElements(articleResultLinks)
                     .stream()
                     .map(WebElement::getText)
                     .filter(text -> !text.isEmpty())
                     .collect(Collectors.toList());
    }
}