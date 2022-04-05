package com.iashandyman.IASHandyman.core.services;

import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.shared.models.totalWeekly.HoursWorkedTotal;
import com.iashandyman.IASHandyman.shared.models.totalWeekly.TotalWeeklyRegisters;
import com.iashandyman.IASHandyman.shared.models.weekly.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ServiceReportDomainService {
    public WeeklyHoursWorked calculateHours(List<ServiceReport> serviceReportList) {
        HoursWorkedTotal hoursWorkedTotal = new HoursWorkedTotal();
        WeeklyHoursWorked weeklyHoursWorked = new WeeklyHoursWorked();
        for (int i = 0; i < serviceReportList.size(); i++){
            //VALIDATE IF BOTH DATES ARE A SAME DAY
            if (ChronoUnit.DAYS.between(serviceReportList.get(i).getStartDatetime().getValue(), serviceReportList.get(i).getEndDatetime().getValue()) < 1){
                //VALIDATE IF START DATE IS SUNDAY
                if (serviceReportList.get(i).getStartDatetime().getValue().getDayOfWeek() == DayOfWeek.SUNDAY){
                    double sundayHoursAmount = calculateHoursAmount(serviceReportList.get(i).getStartDatetime().getValue(), serviceReportList.get(i).getEndDatetime().getValue());
                    weeklyHoursWorked = saveSundayHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), sundayHoursAmount);
                    hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + sundayHoursAmount);
                    //ELSE IF START DATE IS NOT SUNDAY
                } else {
                    //VALIDATE IF START DATETIME IS BEFORE TO SEVEN AM
                    if (serviceReportList.get(i).getStartDatetime().getValue().isBefore(establishSevenAm(serviceReportList.get(i).getStartDatetime().getValue()))){
                        //VALIDATE IF END DATETIME IS BEFORE TO SEVEN AM
                        if (serviceReportList.get(i).getEndDatetime().getValue().isBefore(establishSevenAm(serviceReportList.get(i).getStartDatetime().getValue()))){
                            double nightHoursAmount = calculateHoursAmount(serviceReportList.get(i).getStartDatetime().getValue(), serviceReportList.get(i).getEndDatetime().getValue());
                            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
                            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
                            //ELSE IF END DATETIME IS AFTER TO SEVEN AM
                        } else {
                            double nightHoursAmount = calculateHoursAmount(serviceReportList.get(i).getStartDatetime().getValue(), establishSevenAm(serviceReportList.get(i).getStartDatetime().getValue()));
                            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
                            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
                            //VALIDATE IF END DATETIME IS BEFORE TO EIGHT PM
                            if (serviceReportList.get(i).getEndDatetime().getValue().isBefore(establishEightPm(serviceReportList.get(i).getStartDatetime().getValue()))){
                                double regularHoursAmount = calculateHoursAmount(establishSevenAm(serviceReportList.get(i).getStartDatetime().getValue()), serviceReportList.get(i).getEndDatetime().getValue());
                                weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
                                hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
                                //ELSE IF END DATETIME IS AFTER TO EIGHT PM
                            } else {
                                double regularHoursAmount = calculateHoursAmount(establishSevenAm(serviceReportList.get(i).getStartDatetime().getValue()), establishEightPm(serviceReportList.get(i).getEndDatetime().getValue()));
                                weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
                                hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
                                nightHoursAmount = calculateHoursAmount(establishEightPm(serviceReportList.get(i).getStartDatetime().getValue()), serviceReportList.get(i).getEndDatetime().getValue());
                                weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
                                hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
                            }
                        }
                        //ELSE IF START DATETIME IS AFTER TO SEVEN AM
                    } else {
                        //VALIDATE IF START DATETIME IS BEFORE TO EIGHT PM
                        if (serviceReportList.get(i).getStartDatetime().getValue().isBefore(establishEightPm(serviceReportList.get(i).getStartDatetime().getValue()))){
                            //VALIDATE IF END DATETIME IS BEFORE TO EIGHT PM
                            TotalWeeklyRegisters registers = calculateWhenEndDatetimeIsAfterEightPm(serviceReportList.get(i).getStartDatetime().getValue(),
                                    serviceReportList.get(i).getEndDatetime().getValue(),
                                    weeklyHoursWorked,
                                    hoursWorkedTotal);
                            weeklyHoursWorked = registers.getWeeklyHoursWorked();
                            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
                            //ELSE IF START DATETIME IS AFTER TO EIGHT PM
                        } else {
                            double nightHoursAmount = calculateHoursAmount(serviceReportList.get(i).getStartDatetime().getValue(), serviceReportList.get(i).getEndDatetime().getValue());
                            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
                            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
                        }
                    }
                }
                //ELSE IF START DATE IS NOT SUNDAY
            }
        }
        return weeklyHoursWorked;
    }

    private TotalWeeklyRegisters calculateWhenEndDatetimeIsAfterEightPm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                        WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){

        if (endDatetime.isBefore(establishEightPm(startDatetime))){
            double regularHoursAmount = calculateHoursAmount(startDatetime, endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            //ELSE IF END DATETIME IS AFTER TO EIGHT PM
        } else {
            double regularHoursAmount = calculateHoursAmount(startDatetime, establishEightPm(endDatetime));
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            double nightHoursAmount = calculateHoursAmount(establishEightPm(startDatetime), endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        }
        TotalWeeklyRegisters t = new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private double calculateHoursAmount(LocalDateTime startDateTime, LocalDateTime endDateTime){
        double hoursAmount = (double)ChronoUnit.SECONDS.between(startDateTime, endDateTime)/(double)3600;
        return hoursAmount;
    }

    private WeeklyHoursWorked saveSundayHoursAmount(WeeklyHoursWorked weeklyHoursWorked,
                                                                      double hoursWorkedTotal,
                                                                      double sundayHoursAmount){
        List<Double> extraAndNoExtraHours = defineTypeHours(hoursWorkedTotal, sundayHoursAmount);
        weeklyHoursWorked.setSundayHoursAmount(new SundayHoursAmount(Math.round((weeklyHoursWorked.getSundayHoursAmount().getValue() + extraAndNoExtraHours.get(1)) * 100) / 100d));
        weeklyHoursWorked.setExtraSundayHoursAmount(new ExtraSundayHoursAmount(Math.round((weeklyHoursWorked.getExtraSundayHoursAmount().getValue() + extraAndNoExtraHours.get(0)) * 100) / 100d));
        return weeklyHoursWorked;
    }

    private WeeklyHoursWorked saveNightHoursAmount(WeeklyHoursWorked weeklyHoursWorked,
                                                    double hoursWorkedTotal,
                                                    double nightHoursAmount){
        List<Double> extraAndNoExtraHours = defineTypeHours(hoursWorkedTotal, nightHoursAmount);
        weeklyHoursWorked.setNightHoursAmount(new NightHoursAmount(Math.round((weeklyHoursWorked.getNightHoursAmount().getValue() + extraAndNoExtraHours.get(1)) * 100) / 100d));
        weeklyHoursWorked.setExtraNightHoursAmount(new ExtraNightHoursAmount(Math.round((weeklyHoursWorked.getExtraNightHoursAmount().getValue() + extraAndNoExtraHours.get(0)) * 100) / 100d));
        return weeklyHoursWorked;
    }

    private WeeklyHoursWorked saveRegularHoursAmount(WeeklyHoursWorked weeklyHoursWorked,
                                                   double hoursWorkedTotal,
                                                   double regularHoursAmount){
        List<Double> extraAndNoExtraHours = defineTypeHours(hoursWorkedTotal, regularHoursAmount);
        weeklyHoursWorked.setRegularHoursAmount(new RegularHoursAmount(Math.round((weeklyHoursWorked.getRegularHoursAmount().getValue() + extraAndNoExtraHours.get(1)) * 100) / 100d));
        weeklyHoursWorked.setExtraRegularHoursAmount(new ExtraRegularHoursAmount(Math.round((weeklyHoursWorked.getExtraRegularHoursAmount().getValue() + extraAndNoExtraHours.get(0)) * 100) / 100d));
        return weeklyHoursWorked;
    }

    private List<Double> defineTypeHours(double hoursWorkedTotal, double currentHoursAmount) {
        List<Double> hours = new ArrayList<>();
        double extraHours = 0.00;
        double noExtraHours = 0.00;
        if (hoursWorkedTotal > 48){
            extraHours = currentHoursAmount;
        }else if (hoursWorkedTotal + currentHoursAmount > 48){
            double difference = 48 - hoursWorkedTotal;
            double rest = currentHoursAmount - difference;
            noExtraHours = difference;
            extraHours = rest;
        } else {
            noExtraHours = currentHoursAmount;
        }
        hours.add(extraHours);
        hours.add(noExtraHours);
        return hours;
    }

    private LocalDateTime establishMidnight(LocalDateTime dateTime) {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(date, midnight);
        return localDateTime;
    }

    private LocalDateTime establishSevenAm(LocalDateTime dateTime){
        LocalTime sevenAm = LocalTime.of(07,00,00);
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(date, sevenAm);
        return localDateTime;
    }

    private LocalDateTime establishEightPm(LocalDateTime dateTime){
        LocalTime eightPm = LocalTime.of(20,00,00);
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(date, eightPm);
        return localDateTime;
    }
}
