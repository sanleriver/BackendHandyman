package com.iashandyman.IASHandyman.infrastructure.controller.model;

import com.iashandyman.IASHandyman.core.domain.service.ServiceId;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;

import java.time.LocalDateTime;

public class ServiceReportInput {

    private String technicianId;
    private String serviceId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    public ServiceReportInput(String technicianId, String serviceId, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        this.technicianId = technicianId;
        this.serviceId = serviceId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public ServiceReportInput() {
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }
}
