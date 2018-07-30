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
import com.github.anninpavel.selenium.pages.YandexMarketPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public final class YandexMarketTest extends BaseTest {

    private YandexMarketPage marketPage;

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
        marketPage = new YandexMarketPage(getDriver(), getDriverWait());
    }

    @Test
    public void verifyNumberItemsPage() {
        marketPage.openPage();
        marketPage.navigateToCategory(YandexMarketPage.Category.TABLET);

        Arrays.stream(YandexMarketPage.NumberItemsPage.values()).forEach(numberItemsPage -> {
            marketPage.changeNumberItemsPage(numberItemsPage);
            assertEquals(marketPage.getCountItems(), numberItemsPage.getNumberItems(),
                    String.format("Проверка количества элементов в выдачи (%d шт)", numberItemsPage.getNumberItems()));
        });
    }

    @Test
    public void verifySortingByPrice() {
        marketPage.openPage();
        marketPage.navigateToCategory(YandexMarketPage.Category.TABLET);

        marketPage.chageSortMode(YandexMarketPage.Sortable.APRICE);
        final var prices = marketPage.getPriceItems();
        final var sortedPrices = marketPage.getPriceItems();
        assertEquals(prices, sortedPrices, "Проверка правильности сортировки по цене");
    }

    @Test
    public void verifyCompare() {
        marketPage.openPage();
        marketPage.navigateToCategory(YandexMarketPage.Category.TABLET);

        marketPage.addTwoItemsForCompare();
        assertEquals(marketPage.getCountCompareProduct(), 2,
                "Проверка добавления двух товаров к сравнению");

        marketPage.removeAllItemsForCompare();
        assertEquals(marketPage.getCountCompareProduct(), 0,
                "Проверка удаления товаров из сравнения");
    }
}