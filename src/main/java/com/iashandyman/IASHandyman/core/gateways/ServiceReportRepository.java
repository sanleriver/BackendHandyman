package com.iashandyman.IASHandyman.core.gateways;

import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceReportRepository {

    void store(ServiceReport serviceReport);

    List<ServiceReport> getReportByTechnicianAndWeek(TechnicianId technicianId, LocalDateTime initialDate, LocalDateTime finalDate);
}
