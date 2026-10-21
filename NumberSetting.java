package com.lucifer.client.setting;

public final class NumberSetting extends Setting<Double> {
    private final double min, max, step;
    public NumberSetting(String name, String description, double initial, double min, double max, double step) {
        super(name, description, initial, value -> value != null && Double.isFinite(value)
                && value >= min && value <= max);
        if (min >= max || step <= 0) throw new IllegalArgumentException("Invalid numeric range");
        this.min = min; this.max = max; this.step = step;
    }
    public double min() { return min; }
    public double max() { return max; }
    public double step() { return step; }
    public void nudge(double direction) {
        double next = Math.max(min, Math.min(max, get() + step * direction));
        set(Math.round(next / step) * step);
    }
}
