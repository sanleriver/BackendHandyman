package com.iashandyman.IASHandyman.core.domain.serviceReport;

import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

public class ServiceReportEndDatetime {

    private final LocalDateTime value;

    public ServiceReportEndDatetime(LocalDateTime value) {
        Validate.notNull(value, "End datetime of service report can't be null");
        Validate.isTrue(value.isBefore(LocalDateTime.now()));
        this.value = value;
    }

    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value + "";
    }
}
