package com.iashandyman.IASHandyman.core.domain.technician;

import org.apache.commons.lang3.Validate;

public class TechnicianFirstname {

    private final String value;

    public TechnicianFirstname(String value) {
        Validate.notNull(value, "Firstname of technician can't be null");
        Validate.notBlank(value, "Firstname of technician can't be blank");
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
