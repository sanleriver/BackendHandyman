package com.iashandyman.IASHandyman.core.services;

import com.iashandyman.IASHandyman.core.domain.serviceReport.ServiceReport;
import com.iashandyman.IASHandyman.shared.models.totalWeekly.HoursWorkedTotal;
import com.iashandyman.IASHandyman.shared.models.totalWeekly.TotalWeeklyRegisters;
import com.iashandyman.IASHandyman.shared.models.weekly.*;
import org.apache.tomcat.jni.Local;
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
                TotalWeeklyRegisters registers = calculateWhenBothDatesAreSameDay(serviceReportList.get(i).getStartDatetime().getValue(),
                        serviceReportList.get(i).getEndDatetime().getValue(), weeklyHoursWorked, hoursWorkedTotal);
                weeklyHoursWorked = registers.getWeeklyHoursWorked();
                hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
                //ELSE IF START DATE IS NOT SUNDAY
            } else {
                TotalWeeklyRegisters registers = calculateWhenDatesAreDifferentDays(serviceReportList.get(i).getStartDatetime().getValue(),
                        serviceReportList.get(i).getEndDatetime().getValue(), weeklyHoursWorked, hoursWorkedTotal);
                weeklyHoursWorked = registers.getWeeklyHoursWorked();
                hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
            }
        }
        return weeklyHoursWorked;
    }

    private TotalWeeklyRegisters calculateWhenDatesAreDifferentDays(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                    WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        if (startDatetime.getDayOfWeek() == DayOfWeek.SUNDAY || endDatetime.getDayOfWeek() == DayOfWeek.SUNDAY){
            TotalWeeklyRegisters registers = calculateWhenFirstDateIsSunday(startDatetime, endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        } else {
            TotalWeeklyRegisters registers = calculateWhenDatesAreDiferrentAndIsNotSundays(startDatetime, endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenDatesAreDiferrentAndIsNotSundays(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                               WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal) {
        if (startDatetime.isBefore(establishSevenAm(startDatetime))){
            TotalWeeklyRegisters registers = calculateHoursOfCompletelyInitialDay(startDatetime, endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        } else {
            if (startDatetime.isBefore(establishEightPm(startDatetime))){
                TotalWeeklyRegisters registers = calculateHoursOfCompletelyInitialDayWhenStartDatetimeIsAfterSevenAm(startDatetime, endDatetime,
                        weeklyHoursWorked, hoursWorkedTotal);
                weeklyHoursWorked = registers.getWeeklyHoursWorked();
                hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
            } else {
                TotalWeeklyRegisters registers = calculateHoursOfCompletelyInitialDayWhenStartDatetimeIsAfterEightPm(startDatetime, endDatetime,
                        weeklyHoursWorked, hoursWorkedTotal);
                weeklyHoursWorked = registers.getWeeklyHoursWorked();
                hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
            }
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateHoursOfCompletelyInitialDayWhenStartDatetimeIsAfterEightPm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                                                     WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        double nightHoursAmount = calculateHoursAmount(startDatetime, establishMidnight(endDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        TotalWeeklyRegisters registers = calculateHoursOfCompletelyFinalDay(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
        weeklyHoursWorked = registers.getWeeklyHoursWorked();
        hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateHoursOfCompletelyInitialDayWhenStartDatetimeIsAfterSevenAm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                                                     WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        double regularHoursAmount = calculateHoursAmount(startDatetime, establishEightPm(startDatetime));
        weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
        double nightHoursAmount2 = calculateHoursAmount(establishEightPm(startDatetime), establishMidnight(endDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount2);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount2);
        TotalWeeklyRegisters registers = calculateHoursOfCompletelyFinalDay(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
        weeklyHoursWorked = registers.getWeeklyHoursWorked();
        hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateHoursOfCompletelyInitialDay(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                              WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal) {
        double nightHoursAmount = calculateHoursAmount(startDatetime, establishSevenAm(startDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        double regularHoursAmount = calculateHoursAmount(establishSevenAm(startDatetime), establishEightPm(startDatetime));
        weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
        double nightHoursAmount2 = calculateHoursAmount(establishEightPm(startDatetime), establishMidnight(endDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount2);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount2);
        TotalWeeklyRegisters registers = calculateHoursOfCompletelyFinalDay(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
        weeklyHoursWorked = registers.getWeeklyHoursWorked();
        hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateHoursOfCompletelyFinalDay(LocalDateTime endDatetime, WeeklyHoursWorked weeklyHoursWorked,
                                                                    HoursWorkedTotal hoursWorkedTotal) {
        if (endDatetime.isBefore(establishSevenAm(endDatetime))){
            double nightHoursAmount = calculateHoursAmount(establishMidnight(endDatetime), endDatetime);
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        } else {
            TotalWeeklyRegisters registers = calculateHoursOfCompletelyFinalDayWhenEndDatetimeIsAfterSevenAm(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateHoursOfCompletelyFinalDayWhenEndDatetimeIsAfterSevenAm(LocalDateTime endDatetime, WeeklyHoursWorked weeklyHoursWorked,
                                                                                                 HoursWorkedTotal hoursWorkedTotal) {
        double nightHoursAmount = calculateHoursAmount(establishMidnight(endDatetime), establishSevenAm(endDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        if (endDatetime.isBefore(establishEightPm(endDatetime))){
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(endDatetime), endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
        } else {
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(endDatetime), establishEightPm(endDatetime));
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            double nightHoursAmount2 = calculateHoursAmount(establishEightPm(endDatetime), endDatetime);
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount2);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount2);
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenFirstDateIsSunday(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                               WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        if (startDatetime.getDayOfWeek() == DayOfWeek.SUNDAY){
            double sundayHoursAmount = calculateHoursAmount(startDatetime, establishMidnight(endDatetime));
            weeklyHoursWorked = saveSundayHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), sundayHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + sundayHoursAmount);
        } else{
            TotalWeeklyRegisters registers = calculateWhenLastDateIsSunday(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenLastDateIsSunday(LocalDateTime endDatetime,
                                                               WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal) {
        if (endDatetime.isBefore(establishSevenAm(endDatetime))){
            double nightHoursAmount = calculateHoursAmount(establishMidnight(endDatetime), endDatetime);
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        } else {
            double nightHoursAmount = calculateHoursAmount(establishMidnight(endDatetime), establishSevenAm(endDatetime));
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
            TotalWeeklyRegisters registers = calculateWhenLastDateIsSundayAndEndTimeIsAfterSevenAm(endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenLastDateIsSundayAndEndTimeIsAfterSevenAm(LocalDateTime endDatetime,
                                                                                        WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal) {
        if (endDatetime.isBefore(establishEightPm(endDatetime))){
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(endDatetime), endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
        } else {
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(endDatetime), establishEightPm(endDatetime));
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            double nightHoursAmount = calculateHoursAmount(establishEightPm(endDatetime), endDatetime);
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenBothDatesAreSameDay(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                  WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        if (startDatetime.getDayOfWeek() == DayOfWeek.SUNDAY){
            double sundayHoursAmount = calculateHoursAmount(startDatetime, endDatetime);
            weeklyHoursWorked = saveSundayHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), sundayHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + sundayHoursAmount);
            //ELSE IF START DATE IS NOT SUNDAY
        } else {
            TotalWeeklyRegisters registers = calculateWhenStartDayIsNotSunday(startDatetime, endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenStartDayIsNotSunday(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                  WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        //VALIDATE IF START DATETIME IS BEFORE TO SEVEN AM
        if (startDatetime.isBefore(establishSevenAm(startDatetime))){
            TotalWeeklyRegisters registers = calculateWhenStartDatetimeIsBeforeSevenAm(startDatetime,
                    endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
            //ELSE IF START DATETIME IS AFTER TO SEVEN AM
        } else {
            //VALIDATE IF START DATETIME IS BEFORE TO EIGHT PM
            TotalWeeklyRegisters registers = calculateWhenStartDatetimeIsBeforeEightPm(startDatetime,
                    endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenStartDatetimeIsBeforeSevenAm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                           WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        //VALIDATE IF END DATETIME IS BEFORE TO SEVEN AM
        if (endDatetime.isBefore(establishSevenAm(startDatetime))){
            double nightHoursAmount = calculateHoursAmount(startDatetime, endDatetime);
            weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
            //ELSE IF END DATETIME IS AFTER TO SEVEN AM
        } else {
            TotalWeeklyRegisters registers = calculateWhenEndDatetimeIsAfterSevenAm(startDatetime, endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenEndDatetimeIsAfterSevenAm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                        WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        double nightHoursAmount = calculateHoursAmount(startDatetime, establishSevenAm(startDatetime));
        weeklyHoursWorked = saveNightHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
        hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        //VALIDATE IF END DATETIME IS BEFORE TO EIGHT PM
        if (endDatetime.isBefore(establishEightPm(startDatetime))){
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(startDatetime), endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            //ELSE IF END DATETIME IS AFTER TO EIGHT PM
        } else {
            double regularHoursAmount = calculateHoursAmount(establishSevenAm(startDatetime), establishEightPm(endDatetime));
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), regularHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + regularHoursAmount);
            nightHoursAmount = calculateHoursAmount(establishEightPm(startDatetime), endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
    }

    private TotalWeeklyRegisters calculateWhenStartDatetimeIsBeforeEightPm(LocalDateTime startDatetime, LocalDateTime endDatetime,
                                                                           WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal){
        if (startDatetime.isBefore(establishEightPm(startDatetime))){
            //VALIDATE IF END DATETIME IS BEFORE TO EIGHT PM
            TotalWeeklyRegisters registers = calculateWhenEndDatetimeIsAfterEightPm(startDatetime,
                    endDatetime, weeklyHoursWorked, hoursWorkedTotal);
            weeklyHoursWorked = registers.getWeeklyHoursWorked();
            hoursWorkedTotal.setValue(registers.getHoursWorkedTotal().getValue());
            //ELSE IF START DATETIME IS AFTER TO EIGHT PM
        } else {
            double nightHoursAmount = calculateHoursAmount(startDatetime, endDatetime);
            weeklyHoursWorked = saveRegularHoursAmount(weeklyHoursWorked, hoursWorkedTotal.getValue(), nightHoursAmount);
            hoursWorkedTotal.setValue(hoursWorkedTotal.getValue() + nightHoursAmount);
        }
        return new TotalWeeklyRegisters(weeklyHoursWorked, hoursWorkedTotal);
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
