package com.iashandyman.IASHandyman.shared.models.weekly;

public class WeeklyHoursWorked {
    private RegularHoursAmount regularHoursAmount;
    private NightHoursAmount nightHoursAmount;
    private SundayHoursAmount sundayHoursAmount;
    private ExtraRegularHoursAmount extraRegularHoursAmount;
    private ExtraNightHoursAmount extraNightHoursAmount;
    private ExtraSundayHoursAmount extraSundayHoursAmount;

    public WeeklyHoursWorked(RegularHoursAmount regularHoursAmount, NightHoursAmount nightHoursAmount, SundayHoursAmount sundayHoursAmount, ExtraRegularHoursAmount extraRegularHoursAmount, ExtraNightHoursAmount extraNightHoursAmount, ExtraSundayHoursAmount extraSundayHoursAmount) {
        this.regularHoursAmount = regularHoursAmount;
        this.nightHoursAmount = nightHoursAmount;
        this.sundayHoursAmount = sundayHoursAmount;
        this.extraRegularHoursAmount = extraRegularHoursAmount;
        this.extraNightHoursAmount = extraNightHoursAmount;
        this.extraSundayHoursAmount = extraSundayHoursAmount;
    }

    public WeeklyHoursWorked() {
        this.regularHoursAmount = new RegularHoursAmount(0.0);
        this.nightHoursAmount = new NightHoursAmount(0.0);
        this.sundayHoursAmount = new SundayHoursAmount(0.0);
        this.extraRegularHoursAmount = new ExtraRegularHoursAmount(0.0);
        this.extraNightHoursAmount = new ExtraNightHoursAmount(0.0);
        this.extraSundayHoursAmount = new ExtraSundayHoursAmount(0.0);
    }

    public RegularHoursAmount getRegularHoursAmount() {
        return regularHoursAmount;
    }

    public void setRegularHoursAmount(RegularHoursAmount regularHoursAmount) {
        this.regularHoursAmount = regularHoursAmount;
    }

    public NightHoursAmount getNightHoursAmount() {
        return nightHoursAmount;
    }

    public void setNightHoursAmount(NightHoursAmount nightHoursAmount) {
        this.nightHoursAmount = nightHoursAmount;
    }

    public SundayHoursAmount getSundayHoursAmount() {
        return sundayHoursAmount;
    }

    public void setSundayHoursAmount(SundayHoursAmount sundayHoursAmount) {
        this.sundayHoursAmount = sundayHoursAmount;
    }

    public ExtraRegularHoursAmount getExtraRegularHoursAmount() {
        return extraRegularHoursAmount;
    }

    public void setExtraRegularHoursAmount(ExtraRegularHoursAmount extraRegularHoursAmount) {
        this.extraRegularHoursAmount = extraRegularHoursAmount;
    }

    public ExtraNightHoursAmount getExtraNightHoursAmount() {
        return extraNightHoursAmount;
    }

    public void setExtraNightHoursAmount(ExtraNightHoursAmount extraNightHoursAmount) {
        this.extraNightHoursAmount = extraNightHoursAmount;
    }

    public ExtraSundayHoursAmount getExtraSundayHoursAmount() {
        return extraSundayHoursAmount;
    }

    public void setExtraSundayHoursAmount(ExtraSundayHoursAmount extraSundayHoursAmount) {
        this.extraSundayHoursAmount = extraSundayHoursAmount;
    }
}
