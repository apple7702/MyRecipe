package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuexiao on 5/13/16.
 */
public class Material {
    private float quantity;
    private String unit;

    Material(float quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public void setQuantity(float n) {
        this.quantity = this.quantity + n;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
