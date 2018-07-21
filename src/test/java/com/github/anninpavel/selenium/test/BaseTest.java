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

package com.github.anninpavel.selenium.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public abstract class BaseTest {

    private long implicitlyWaitSecond = 10;
    private long explicitWaitSecond = 5;
    private long pageLoadSecond = 10;
    private WebDriver driver;
    private WebDriverWait driverWait;

    @BeforeClass
    public void setup() {
        if (driver == null) {
            throw new IllegalArgumentException("Call method setDriver(WebDriver)");
        }
        driverWait = new WebDriverWait(driver, explicitWaitSecond);
        driver.manage().window().maximize();
        driver.manage().timeouts()
                .implicitlyWait(implicitlyWaitSecond, TimeUnit.SECONDS)
                .pageLoadTimeout(pageLoadSecond, TimeUnit.SECONDS);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public BaseTest setDriver(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    public WebDriverWait getDriverWait() {
        return driverWait;
    }

    public long getImplicitlyWaitSecond() {
        return implicitlyWaitSecond;
    }

    public BaseTest setImplicitlyWaitSecond(long implicitlyWaitSecond) {
        this.implicitlyWaitSecond = implicitlyWaitSecond;
        return this;
    }

    public long getExplicitWaitSecond() {
        return explicitWaitSecond;
    }

    public BaseTest setExplicitWaitSecond(long explicitWaitSecond) {
        this.explicitWaitSecond = explicitWaitSecond;
        return this;
    }

    public long getPageLoadSecond() {
        return pageLoadSecond;
    }

    public BaseTest setPageLoadSecond(long pageLoadSecond) {
        this.pageLoadSecond = pageLoadSecond;
        return this;
    }
}