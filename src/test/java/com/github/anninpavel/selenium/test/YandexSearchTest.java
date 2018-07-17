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

import com.github.anninpavel.selenium.WebDriverFactory;
import com.github.anninpavel.selenium.WebDriverType;
import com.github.anninpavel.selenium.pages.YandexResultPage;
import com.github.anninpavel.selenium.pages.YandexSearchPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public class YandexSearchTest extends BaseTest {

    private YandexSearchPage searchPage;
    private YandexResultPage resultPage;

    @Parameters("driverType")
    @BeforeTest
    public void initialization(String driverType) {
        setDriver(WebDriverFactory.getInstance().getDriver(WebDriverType.requrieDriver(driverType)));
    }

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        searchPage = new YandexSearchPage(getDriver(), getDriverWait());
        resultPage = new YandexResultPage(getDriver(), getDriverWait());
        searchPage.openPage();
    }

    @Test
    public void verifySearch() {
        searchPage.search("погода пенза");
        final var resultText = resultPage.getTextOfFirstResult().toString();

        assertTrue(resultText.toLowerCase().contains("погода"),
                "Поиск подстроки \"погода\" в первом результате поиска");
        assertTrue(resultText.toLowerCase().matches("(.*?)пенз(.*?)"),
                "Поиск подстроки \"пенза\" в первом результате поиска");
    }
}