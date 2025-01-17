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

package com.github.anninpavel.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public class WebDriverFactory {

    private static volatile WebDriverFactory instance;
    private final Map<WebDriverType, WebDriver> drivers;

    public static WebDriverFactory getInstance() {
        WebDriverFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (WebDriverFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new WebDriverFactory();
                }
            }
        }
        return localInstance;
    }

    private WebDriverFactory() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver");
        drivers = Collections.synchronizedMap(new EnumMap<>(WebDriverType.class));
    }

    public WebDriver getDriver(WebDriverType type) {
        final var driver = drivers.get(type);
        if (driver != null) {
            return driver;
        } else {
            synchronized (this) {
                final WebDriver newDriver;
                switch (type) {
                    case CHROME:
                        newDriver = new ChromeDriver();
                        break;
                    case FIREFOX:
                        newDriver = new FirefoxDriver();
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unknown driver type: %s", type));
                }
                drivers.put(type, newDriver);
                return newDriver;
            }
        }
    }
}