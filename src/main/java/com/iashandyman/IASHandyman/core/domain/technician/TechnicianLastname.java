package com.iashandyman.IASHandyman.core.domain.technician;

import org.apache.commons.lang3.Validate;

public class TechnicianLastname {

    private final String value;

    public TechnicianLastname(String value) {
        Validate.notNull(value, "Lastname of technician can't be null");
        Validate.notBlank(value, "Lastname of technician can't be blank");
        Validate.isTrue(value.trim().length() < 50);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
