package com.iashandyman.IASHandyman.core.domain.service;

import org.apache.commons.lang3.Validate;

public class ServiceId {

    private final String value;

    public ServiceId(String value) {
        Validate.notNull(value, "Id of service can't be null");
        Validate.notBlank(value, "Id of service can't be blank");
        Validate.isTrue(value.trim().length() < 10);
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
