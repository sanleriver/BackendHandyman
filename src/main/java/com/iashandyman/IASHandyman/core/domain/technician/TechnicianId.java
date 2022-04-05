package com.iashandyman.IASHandyman.core.domain.technician;

import org.apache.commons.lang3.Validate;

public class TechnicianId {

    private final String value;

    public TechnicianId(String value) {
        Validate.notNull(value, "Id of technician can't be null");
        Validate.notBlank(value, "Id of technician can't be blank");
        Validate.isTrue(value.trim().length() < 10 && value.trim().length() > 6);
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
