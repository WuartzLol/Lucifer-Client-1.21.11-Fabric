package com.lucifer.client.module;

public enum Category {
    COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), HUD("HUD"),
    UTILITY("Utility"), PERFORMANCE("Performance");

    private final String title;
    Category(String title) { this.title = title; }
    public String title() { return title; }
}
