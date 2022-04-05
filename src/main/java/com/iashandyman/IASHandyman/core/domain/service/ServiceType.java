package com.iashandyman.IASHandyman.core.domain.service;

import org.apache.commons.lang3.Validate;

public class ServiceType {

    private final String value;

    public ServiceType(String value) {
        Validate.notNull(value, "Type of service can't be null");
        Validate.notBlank(value, "Type of service can't be blank");
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
