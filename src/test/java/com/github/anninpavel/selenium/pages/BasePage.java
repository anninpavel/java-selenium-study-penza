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

package com.github.anninpavel.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public abstract class BasePage {

    protected final WebDriver mDriver;
    private final WebDriverWait driverWait;

    public BasePage(final WebDriver driver, final WebDriverWait driverWait) {
        this.mDriver = driver;
        this.driverWait = driverWait;
    }

    protected void replaceText(final By locator, final CharSequence text) {
        final var element = findElementByLocator(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected CharSequence readText(final By locator) {
        return findElementByLocator(locator).getText();
    }

    protected List<CharSequence> readTexts(final By locator) {
        return findElementsByLocator(locator).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    protected void click(final By locator) {
        findElementByLocator(locator).click();
    }

    protected void submit(final By locator) {
        findElementByLocator(locator).submit();
    }

    protected void moveToElement(final By locator) {
        final var element = findElementByLocator(locator);
        moveToElement(element);
    }

    protected void moveToElement(final WebElement element) {
        new Actions(mDriver).moveToElement(element)
                .build()
                .perform();
    }

    protected void scrollTo(final By locator) {
        final var element = findElementByLocator(locator);
        ((JavascriptExecutor) mDriver).executeScript("arguments[0].scrollIntoView();", element);
    }

    protected WebElement findElementByLocator(final By locator) {
        return driverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> findElementsByLocator(final By locator) {
        return driverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void invisibilityElementByLocator(final By locator) {
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}