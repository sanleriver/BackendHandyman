package com.iashandyman.IASHandyman.shared.models.weekly;

import org.apache.commons.lang3.Validate;

public class NightHoursAmount {
    private final Double value;

    public NightHoursAmount(Double value) {
        Validate.notNull(value, "Amount can not be null.");
        Validate.isTrue(value >= 0, "Amount can not be negative");
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value + "";
    }
}
