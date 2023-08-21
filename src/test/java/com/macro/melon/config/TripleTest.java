package com.macro.melon.config;

import org.openqa.selenium.WebElement;

public class TripleTest {
    private float  y;
    private float  x;
    private WebElement rect;

    public TripleTest(float  y, float  x, WebElement rect) {
        this.y = y;
        this.x = x;
        this.rect = rect;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public WebElement getRect() {
        return rect;
    }
}
