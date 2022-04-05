package com.iashandyman.IASHandyman.infrastructure.controller;

import com.iashandyman.IASHandyman.infrastructure.controller.model.ServiceReportInput;
import com.iashandyman.IASHandyman.infrastructure.controller.services.ServiceReportService;
import com.iashandyman.IASHandyman.shared.errors.ApplicationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ServiceReportController {
    private final ServiceReportService serviceReportService;

    public ServiceReportController(ServiceReportService serviceReportService) {
        this.serviceReportService = serviceReportService;
    }

    @RequestMapping(path = "/reports", method = RequestMethod.POST)
    public ResponseEntity<?> createReport(@RequestBody ServiceReportInput serviceReportInput){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(serviceReportService.saveServiceReport(serviceReportInput));
        }catch (IllegalArgumentException | NullPointerException e){
            ApplicationError error = new ApplicationError(
                    "InputDataValidationError",
                    "Bad input data",
                    Map.of(
                            "error", e.getMessage()
                    )
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e){
            ApplicationError error = new ApplicationError(
                    "SystemError",
                    e.getMessage(),
                    Map.of()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error);
        }
    }

    @RequestMapping(path = "/reports/{techId}/{week}", method = RequestMethod.GET)
    public ResponseEntity<Object> getHoursWorked(@PathVariable String techId, @PathVariable int week){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceReportService.getHoursWorked(techId, week));
        } catch (Exception e){
            ApplicationError error = new ApplicationError(
                    "SystemError",
                    e.getMessage(),
                    Map.of()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error);
        }
    }
}
