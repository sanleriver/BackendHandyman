package com.iashandyman.IASHandyman.infrastructure.controller.gateways.models;

import com.iashandyman.IASHandyman.core.domain.service.ServiceId;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportEndDatetime;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportId;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportStartDatetime;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ServiceReportDBO {

    private String id;
    private String technicianId;
    private String serviceId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    public ServiceReportDBO(String id, String technicianId, String serviceId, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        this.id = id;
        this.technicianId = technicianId;
        this.serviceId = serviceId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public ServiceReportDBO() {
    }

    public ServiceReport toDomain(){
        return new ServiceReport(
                new ServiceReportId(id),
                new TechnicianId(technicianId),
                new ServiceId(serviceId),
                new ServiceReportStartDatetime(startDatetime),
                new ServiceReportEndDatetime(endDatetime)
        );
    }

    public static ServiceReportDBO fromDomain(ServiceReport serviceReport){
        return new ServiceReportDBO(
                serviceReport.getId().toString(),
                serviceReport.getTechnician().getValue(),
                serviceReport.getService().getValue(),
                serviceReport.getStartDatetime().getValue(),
                serviceReport.getEndDatetime().getValue()
        );
    }

    public static ServiceReportDBO fromResultSet(ResultSet resultSet) throws SQLException{
        ServiceReportDBO serviceReportDBO = new ServiceReportDBO();
        serviceReportDBO.setId(resultSet.getString("service_report_id"));
        serviceReportDBO.setTechnicianId(resultSet.getString("technician_id"));
        serviceReportDBO.setServiceId(resultSet.getString("service_id"));
        serviceReportDBO.setStartDatetime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
        serviceReportDBO.setEndDatetime(resultSet.getTimestamp("end_datetime").toLocalDateTime());
        return serviceReportDBO;
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
