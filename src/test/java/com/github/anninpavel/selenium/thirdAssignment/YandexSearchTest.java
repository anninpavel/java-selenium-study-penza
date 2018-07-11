/*
 *  MIT License
 *
 *  Copyright (c) 2018 Pavel Annin
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.github.anninpavel.selenium.thirdAssignment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public class YandexSearchTest {

    private final static String BASE_URL = "https://ya.ru/";

    private WebDriver driver;

    @BeforeSuite
    public void initialization() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver");
    }

    @Parameters("driverType")
    @BeforeTest
    public void initDriver(final String driverType) {
        switch (driverType) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException(String.format("%s: %s", "Unknown driver type:", driver));
        }

        // Setup
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5_000, TimeUnit.MILLISECONDS);
        driver.get(BASE_URL);
    }

    @AfterTest
    public void releaseDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    public void verifyFirstSearchSuggestions() {
        final var queryInput = driver.findElement(By.name("text"));
        queryInput.clear();
        queryInput.sendKeys("погода пенза");

        final var suggestionText = driver.findElements(By.className("suggest2-item__text")).get(0);
        assertTrue(suggestionText.getText().toLowerCase().contains("пенза"),
                "Поиск подстроки в первом результате предложения");
    }
}