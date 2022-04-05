package com.iashandyman.IASHandyman.infrastructure.controller.model;

import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;

public class ServiceReportDTO {

    private String id;
    private String technicianId;
    private String serviceId;
    private String startDatetime;
    private String endDatetime;

    public ServiceReportDTO() {
    }

    public ServiceReportDTO(String id, String technicianId, String serviceId, String startDatetime, String endDatetime) {
        this.id = id;
        this.technicianId = technicianId;
        this.serviceId = serviceId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public static ServiceReportDTO fromDomain(ServiceReport serviceReport){
        return new ServiceReportDTO(
                serviceReport.getId().getValue(),
                serviceReport.getTechnician().getValue(),
                serviceReport.getService().getValue(),
                serviceReport.getStartDatetime().getValue().toString(),
                serviceReport.getEndDatetime().getValue().toString()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }
}
