package com.iashandyman.IASHandyman.core.domain.service;

import org.apache.commons.lang3.Validate;

public class ServiceName {

    private final String value;

    public ServiceName(String value) {
        Validate.notNull(value, "Name of service can't be null");
        Validate.notBlank(value, "Name of service can't be blank");
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
