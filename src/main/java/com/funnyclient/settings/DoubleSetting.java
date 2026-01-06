package com.funnyclient.settings;

public class DoubleSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double increment;
    
    public DoubleSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.increment = increment;
    }
    
    @Override
    public void setValue(Double value) {
        super.setValue(Math.max(min, Math.min(max, value)));
    }
    
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }
}