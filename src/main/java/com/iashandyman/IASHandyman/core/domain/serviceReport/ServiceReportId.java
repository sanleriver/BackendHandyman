package com.iashandyman.IASHandyman.core.domain.serviceReport;

import org.apache.commons.lang3.Validate;

public class ServiceReportId {

    private final String value;

    public ServiceReportId(String value) {
        Validate.notNull(value, "Id of service report can't be null");
        Validate.notBlank(value, "Id of service report can't be blank");
        Validate.isTrue(value.trim().length() == 36);
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
