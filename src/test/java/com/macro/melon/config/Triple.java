package com.macro.melon.config;

import org.openqa.selenium.WebElement;

public class Triple {
    private float  y;
    private float  x;
    private WebElement rect;

    public Triple(float  y, float  x, WebElement rect) {
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
