package com.iashandyman.IASHandyman.infrastructure.controller.gateways;

import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.core.domain.technician.TechnicianId;
import com.iashandyman.IASHandyman.core.gateways.ServiceReportRepository;
import com.iashandyman.IASHandyman.infrastructure.controller.gateways.models.ServiceReportDBO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceReportRepositoryImpl implements ServiceReportRepository {
    private final DataSource dataSource;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ServiceReportRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void store(ServiceReport serviceReport) {
        String sql = "INSERT INTO service_report (service_report_id, technician_id, service_id, start_datetime, end_datetime) VALUES (?,?,?,?,?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ServiceReportDBO serviceReportDBO = ServiceReportDBO.fromDomain(serviceReport);
            preparedStatement.setString(1, serviceReportDBO.getId());
            preparedStatement.setString(2, serviceReportDBO.getTechnicianId());
            preparedStatement.setString(3, serviceReportDBO.getServiceId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(serviceReportDBO.getStartDatetime().format(formatter)));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(serviceReportDBO.getEndDatetime().format(formatter)));
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Error querying database", exception);
        }
    }

    @Override
    public List<ServiceReport> getReportByTechnicianAndWeek(TechnicianId technicianId, LocalDateTime initialDate, LocalDateTime finalDate) {
        String sql = "SELECT * FROM service_report WHERE technician_id = ? " +
                "AND start_datetime BETWEEN ? AND ? " +
                "OR end_datetime BETWEEN ? AND ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, technicianId.getValue());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(initialDate.format(formatter)));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(finalDate.format(formatter)));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(initialDate.format(formatter)));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(finalDate.format(formatter)));

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ServiceReport> reports = new ArrayList<>();

            while (resultSet.next()){
                ServiceReportDBO serviceReportDBO = ServiceReportDBO.fromResultSet(resultSet);
                ServiceReport serviceReport = serviceReportDBO.toDomain();
                reports.add(serviceReport);
            }

            resultSet.close();

            return reports;
        } catch (SQLException exception) {
            throw new RuntimeException("Error querying database", exception);
        }
    }
}
