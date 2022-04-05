package com.iashandyman.IASHandyman.shared.models.totalWeekly;

import com.iashandyman.IASHandyman.shared.models.weekly.WeeklyHoursWorked;

public class TotalWeeklyRegisters {
    private WeeklyHoursWorked weeklyHoursWorked;
    private HoursWorkedTotal hoursWorkedTotal;

    public TotalWeeklyRegisters(WeeklyHoursWorked weeklyHoursWorked, HoursWorkedTotal hoursWorkedTotal) {
        this.weeklyHoursWorked = weeklyHoursWorked;
        this.hoursWorkedTotal = hoursWorkedTotal;
    }

    public TotalWeeklyRegisters() {
    }

    public WeeklyHoursWorked getWeeklyHoursWorked() {
        return weeklyHoursWorked;
    }

    public void setWeeklyHoursWorked(WeeklyHoursWorked weeklyHoursWorked) {
        this.weeklyHoursWorked = weeklyHoursWorked;
    }

    public HoursWorkedTotal getHoursWorkedTotal() {
        return hoursWorkedTotal;
    }

    public void setHoursWorkedTotal(HoursWorkedTotal hoursWorkedTotal) {
        this.hoursWorkedTotal = hoursWorkedTotal;
    }
}
