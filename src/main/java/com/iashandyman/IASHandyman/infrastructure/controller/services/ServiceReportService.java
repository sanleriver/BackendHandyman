package com.iashandyman.IASHandyman.infrastructure.controller.services;

import com.iashandyman.IASHandyman.core.domain.service.ServiceId;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportEndDatetime;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportId;
import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReportStartDatetime;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;
import com.iashandyman.IASHandyman.core.gateways.ServiceReportRepository;
import com.iashandyman.IASHandyman.core.services.ServiceReportDomainService;
import com.iashandyman.IASHandyman.infrastructure.controller.model.ServiceReportDTO;
import com.iashandyman.IASHandyman.infrastructure.controller.model.ServiceReportInput;
import com.iashandyman.IASHandyman.shared.models.weekly.WeeklyHoursWorked;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceReportService {
    private final ServiceReportRepository serviceReportRepository;
    private final ServiceReportDomainService domainService;

    public ServiceReportService(ServiceReportRepository serviceReportRepository, ServiceReportDomainService domainService) {
        this.serviceReportRepository = serviceReportRepository;
        this.domainService = domainService;
    }

    public ServiceReportDTO saveServiceReport(ServiceReportInput serviceReportInput){
        String serviceReportId = UUID.randomUUID().toString();
        ServiceReport serviceReport = new ServiceReport(
                new ServiceReportId(serviceReportId),
                new TechnicianId(serviceReportInput.getTechnicianId()),
                new ServiceId(serviceReportInput.getServiceId()),
                new ServiceReportStartDatetime(serviceReportInput.getStartDatetime()),
                new ServiceReportEndDatetime(serviceReportInput.getEndDatetime())
        );
        serviceReportRepository.store(serviceReport);
        return ServiceReportDTO.fromDomain(serviceReport);
    }

    public WeeklyHoursWorked getHoursWorked(String techId, int week) {
        TechnicianId technicianId = new TechnicianId(techId);
        List<LocalDateTime> dateTimes = establishInitialAndFinalDateOfWeek(week);
        List<ServiceReport> serviceReportList = serviceReportRepository.getReportByTechnicianAndWeek(technicianId, dateTimes.get(0), dateTimes.get(1));
        WeeklyHoursWorked result = domainService.calculateHours(serviceReportList);
        return result;
    }

    private List<LocalDateTime> establishInitialAndFinalDateOfWeek(int week){
        LocalDate initialDate = LocalDate.now().with(WeekFields.ISO.weekOfWeekBasedYear(), week).with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
        LocalTime initialTime = LocalTime.of(00,00,00);
        LocalDate finalDate = LocalDate.now().with(WeekFields.ISO.weekOfWeekBasedYear(), week).with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
        LocalTime finalTime = LocalTime.of(23,59,59);
        List<LocalDateTime> finalList = new ArrayList<>();
        finalList.add(LocalDateTime.of(initialDate, initialTime));
        finalList.add(LocalDateTime.of(finalDate, finalTime));
        return finalList;
    }
}
