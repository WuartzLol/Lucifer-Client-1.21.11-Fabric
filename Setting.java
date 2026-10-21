package com.lucifer.client.setting;

import java.util.function.Predicate;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private T value;
    private final Predicate<T> validator;

    protected Setting(String name, String description, T initial, Predicate<T> validator) {
        this.name = name;
        this.description = description;
        this.value = initial;
        this.validator = validator;
    }
    public String name() { return name; }
    public String description() { return description; }
    public T get() { return value; }
    public void set(T next) { if (validator.test(next)) value = next; }
}
