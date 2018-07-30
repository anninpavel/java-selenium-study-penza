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
import com.github.anninpavel.selenium.pages.PageScope;
import com.github.anninpavel.selenium.pages.YandexGeoLocationPage;
import com.github.anninpavel.selenium.pages.YandexResultPage;
import com.github.anninpavel.selenium.pages.YandexSearchPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertTrue;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public final class YandexSearchTest extends BaseTest {

    private YandexSearchPage searchPage;

    @Parameters("driverType")
    @BeforeTest
    public void initialization(String driverType) {
        final var webDriverType = WebDriverType.requireDriver(driverType);
        final var webDriver = WebDriverFactory.getInstance().getDriver(webDriverType);
        setDriver(webDriver);
    }

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        final var pageScope = new PageScope(
                new YandexResultPage(getDriver(), getDriverWait()),
                new YandexGeoLocationPage(getDriver(), getDriverWait()));
        searchPage = new YandexSearchPage(getDriver(), getDriverWait(), pageScope);
    }

    @Test
    public void verifySearch() {
        searchPage.openLitePage();
        final var resultText = searchPage.search("погода пенза")
                .getTextOfFirstResult()
                .toString();

        assertTrue(resultText.toLowerCase().contains("погода"),
                "Поиск подстроки \"погода\" в первом результате поиска");
        assertTrue(resultText.toLowerCase().matches("(.*?)пенз(.*?)"),
                "Поиск подстроки \"пенза\" в первом результате поиска");
    }

    @Test
    public void verifyTabMoreWhenChangeCity() {
        searchPage.openFullPage();

        searchPage.navigateToGeoLocation().changeCity("Лондон");
        final var londonTitles = searchPage.getTitleMoreTabs();

        searchPage.navigateToGeoLocation().changeCity("Париж");
        final var parisTitles = searchPage.getTitleMoreTabs();

        assertTrue(Arrays.equals(londonTitles, parisTitles),
                "Проверка содержимого влкадки \"ещё\" при изменении города");
    }

    @Test
    public void verifyUrlWhenGoTabs() {
        Arrays.stream(YandexSearchPage.TabType.values()).forEach(tab -> {
            searchPage.openFullPage();
            searchPage.navigateTo(tab);
            assertTrue(getDriver().getCurrentUrl().startsWith(tab.getLink()),
                    String.format("Проверка валидности url при переходе на вкладку %s.", tab));
        });
    }
}