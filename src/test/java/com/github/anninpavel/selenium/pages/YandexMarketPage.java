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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Pavel Annin (https://github.com/anninpavel).
 */
public final class YandexMarketPage extends BasePage {

    private final static String ENDPOINT = "https://market.yandex.ru/";

    private final By computerDepartmentLink = By.cssSelector("[data-department=\"Компьютеры\"]");
    private final By categoriesContainer = By.className("topmenu__subitem");
    private final By numberItemsPageButton = By.className("select__button");
    private final By numberItemsPageContainer = By.className("select__item");
    private final By itemsContainer = By.className("n-snippet-card2");
    private final By loaderLabel = By.className("preloadable__preloader");
    private final By sorterLabels = By.cssSelector(".n-filter-block_pos_left > .n-filter-sorter");
    private final By priceLabel = By.className("price");
    private final By itemCompareButton = By.className("image_name_compare");
    private final By compareButton = By.cssSelector(".popup-informer .button");
    private final By compareProductsLabel = By.cssSelector(".n-compare-content__line > .n-compare-cell");
    private final By removeCompareButton = By.className("n-compare-toolbar__action");

    public YandexMarketPage(WebDriver driver, WebDriverWait driverWait) {
        super(driver, driverWait);
    }

    public void openPage() {
        mDriver.get(ENDPOINT);
    }

    public void navigateToCategory(Category category) {
        final String name;
        switch (category) {
            case TABLET:
                name = "планшеты";
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown category: %s", category));
        }

        moveToElement(computerDepartmentLink);
        final var categoryElement = findElementsByLocator(categoriesContainer).stream()
                .filter(webElement -> webElement.getText().toLowerCase().equals(name))
                .findFirst()
                .orElseThrow();
        categoryElement.click();
    }

    public void changeNumberItemsPage(NumberItemsPage type) {
        scrollTo(numberItemsPageButton);
        click(numberItemsPageButton);
        findElementsByLocator(numberItemsPageContainer).stream()
                .filter(element -> element.getText().contains(Integer.toString(type.getNumberItems())))
                .findFirst()
                .orElseThrow().click();
        invisibilityElementByLocator(loaderLabel);
    }

    public void chageSortMode(Sortable sortable) {
        findElementsByLocator(sorterLabels).stream()
                .filter(element -> element.getAttribute("data-bem").contains(sortable.getKey()))
                .findFirst()
                .orElseThrow()
                .click();
        invisibilityElementByLocator(loaderLabel);
    }

    public int[] getPriceItems() {
        return findElementsByLocator(priceLabel).stream()
                .map(WebElement::getText)
                .map(text -> text.replaceAll("[\\D]", ""))
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public void addTwoItemsForCompare() {
        findElementsByLocator(itemCompareButton).stream()
                .limit(2)
                .forEach(element -> {
                    moveToElement(element);
                    element.click();
                });
        click(compareButton);
    }

    public void removeAllItemsForCompare() {
        click(removeCompareButton);
    }

    public int getCountCompareProduct() {
        return findElementsByLocator(compareProductsLabel).size();
    }

    public int getCountItems() {
        return findElementsByLocator(itemsContainer).size();
    }

    public enum Category {TABLET}

    public enum NumberItemsPage {
        ITEMS_12(12),
        ITEMS_48(48);

        private final int numberItems;

        NumberItemsPage(int number) {
            this.numberItems = number;
        }

        public int getNumberItems() {
            return numberItems;
        }
    }

    public enum Sortable {
        APRICE("aprice");

        private final String key;

        Sortable(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}