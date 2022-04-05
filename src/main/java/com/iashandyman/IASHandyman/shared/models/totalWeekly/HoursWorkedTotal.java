package com.iashandyman.IASHandyman.shared.models.totalWeekly;

import org.apache.commons.lang3.Validate;

public class HoursWorkedTotal {
    private Double value;

    public HoursWorkedTotal(Double value) {
        Validate.notNull(value, "Amount can not be null.");
        Validate.isTrue(value >= 0, "Amount can not be negative");
        this.value = value;
    }

    public HoursWorkedTotal() {
        this.value = 0.0;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value + "";
    }
}
