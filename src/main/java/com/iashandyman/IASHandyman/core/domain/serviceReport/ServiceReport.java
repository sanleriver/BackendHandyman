package com.iashandyman.IASHandyman.core.domain.serviceReport;

import com.iashandyman.IASHandyman.core.domain.service.ServiceId;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;
import org.apache.commons.lang3.Validate;

public class ServiceReport {

    private final ServiceReportId id;
    private final TechnicianId technician;
    private final ServiceId service;
    private final ServiceReportStartDatetime startDatetime;
    private final ServiceReportEndDatetime endDatetime;

    public ServiceReport(ServiceReportId id, TechnicianId technician, ServiceId service, ServiceReportStartDatetime startDatetime, ServiceReportEndDatetime endDatetime) {
        Validate.isTrue(endDatetime.getValue().isAfter(startDatetime.getValue()));
        Validate.isTrue(endDatetime.getValue().isBefore(startDatetime.getValue().plusDays(1)), "End datetime should be max 1 day after of start datetime");
        this.id = id;
        this.technician = technician;
        this.service = service;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public ServiceReportId getId() {
        return id;
    }

    public TechnicianId getTechnician() {
        return technician;
    }

    public ServiceId getService() {
        return service;
    }

    public ServiceReportStartDatetime getStartDatetime() {
        return startDatetime;
    }

    public ServiceReportEndDatetime getEndDatetime() {
        return endDatetime;
    }
}
