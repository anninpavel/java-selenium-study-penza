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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public final class YandexSearchPage extends BasePage {

    private static final String ENDPOINT_LIGHT = "https://ya.ru/";
    private static final String ENDPOINT_FULL = "https://yandex.ru/";

    private final PageScope scope;

    private final By queryInput = By.name("text");
    private final By findButton = By.className("suggest2-form__button");
    private final By geoLocationField = By.className("geolink");
    private final By moreLink = By.cssSelector("[data-statlog=\"tabs.more\"]");
    private final By moreTabsField = By.cssSelector("[role=\"menuitem\"]");

    public YandexSearchPage(final WebDriver driver, final WebDriverWait driverWait, final PageScope pageScope) {
        super(driver, driverWait);
        scope = pageScope;
    }

    public void openLitePage() {
        mDriver.get(ENDPOINT_LIGHT);
    }

    public void openFullPage() {
        mDriver.get(ENDPOINT_FULL);
    }

    public YandexResultPage search(final CharSequence text) {
        replaceText(queryInput, text);
        click(findButton);
        return scope.getPage(YandexResultPage.class);
    }

    public YandexGeoLocationPage navigateToGeoLocation() {
        click(geoLocationField);
        return scope.getPage(YandexGeoLocationPage.class);
    }

    public CharSequence[] getTitleMoreTabs() {
        click(moreLink);
        final var titleTabs = readTexts(moreTabsField);
        return titleTabs.toArray(new CharSequence[titleTabs.size()]);
    }
}