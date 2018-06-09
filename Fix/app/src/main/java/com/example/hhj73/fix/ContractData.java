package com.example.hhj73.fix;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by skrud on 2018-06-08.
 */

public class ContractData {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy년 mm월 dd일");
    private String contractwritedate;
    private String studentName;
    private String seniorName;
    private String startdate;
    private int monthperiod;
    private String expirationdate;
    private String monthlyfee;
    private String address;
    private boolean smokingConsent,petConsent, cerfewConsent, helpConsent, extraConsent;
    private int homecomingtime;
    private String extraspecial;
    private boolean finalagree;

    //생성자
    ContractData(){}



    //디폴트로 생성할 것.
    ContractData(String studentName, String seniorName, String monthlyfee, String address, boolean smoking_j, boolean smoking_s,
                 boolean pet_j, boolean pet_s, boolean cerfew_j, boolean cerfew_s, boolean help_j, boolean help_s, String extra_s, String extra_j) {
        this.contractwritedate = dateFormat.format(new Date()); //바로 오늘 날짜만 가능!
        this.studentName = studentName;
        this.seniorName = seniorName;
        this.startdate = dateFormat.format(new Date());     //디폴트로 오늘날짜
        this.monthperiod = 0;
        this.monthlyfee = monthlyfee;
        this.address = address;

        try {
            Date date = dateFormat.parse(this.startdate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH,this.monthperiod);
            this.expirationdate = dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (smoking_j == smoking_s)
            this.smokingConsent = true;
        else
            this.smokingConsent = false;
        if (pet_j == pet_s)
            this.petConsent = true;
        else
            this.petConsent = false;
        if (cerfew_s == cerfew_j)
            this.cerfewConsent = true;
        else
            this.cerfewConsent = false;
        if(help_j == help_s)
            this.helpConsent = true;
        else
            this.helpConsent = false;
        this.extraConsent = false;
        this.finalagree = false;
        if (extra_j != null && extra_s==null)
            this.extraspecial ="학생 : "+extra_j+"\n";
        else if (extra_j == null && extra_s!=null)
            this.extraspecial ="어르신 : "+extra_s+"\n";
        else if (extra_s != null && extra_j!=null)
            this.extraspecial ="어르신 : "+extra_j+"\n"+"학생 : "+extra_s+"\n";

    }

    public String getContractwritedate() {
        return contractwritedate;
    }

    public void setContractwritedate(String contractwritedate) {
        this.contractwritedate = contractwritedate;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSeniorName() {
        return seniorName;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        //스타트데이트하면 바로 만료데이트 업데이트
        this.startdate = startdate;
        try {
            Date date = dateFormat.parse(this.startdate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH,this.monthperiod);
            this.expirationdate = dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getMonthperiod() {
        return monthperiod;
    }

    public void setMonthperiod(int monthperiod) {
        //기간 업데이트하면 바로 만료데이트 업데이트
        this.monthperiod = monthperiod;
        try {
            Date date = dateFormat.parse(this.startdate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH,this.monthperiod);
            this.expirationdate = dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getMonthlyfee() {
        return monthlyfee;
    }

    public void setMonthlyfee(String monthlyfee) {
        this.monthlyfee = monthlyfee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSmokingConsent() {
        return smokingConsent;
    }

    public void setSmokingConsent(boolean smokingConsent) {
        this.smokingConsent = smokingConsent;
    }

    public boolean isPetConsent() {
        return petConsent;
    }

    public void setPetConsent(boolean petConsent) {
        this.petConsent = petConsent;
    }

    public boolean isCerfewConsent() {
        return cerfewConsent;
    }

    public void setCerfewConset(boolean cerfewConsent) {
        this.cerfewConsent = cerfewConsent;
    }

    public int getHomecomingtime() {
        return homecomingtime;
    }

    public void setHomecomingtime(int homecomingtime) {
        this.homecomingtime = homecomingtime;
    }

    public boolean isHelpConsent() {
        return helpConsent;
    }

    public void setHelpConsent(boolean helpConsent) {
        this.helpConsent = helpConsent;
    }

    public String getExtraspecial() {
        return extraspecial;
    }

    public void setExtraspecial(String extraspecial) {
        this.extraspecial = this.extraspecial+extraspecial;
    }

    public boolean isExtraConsent() {
        return extraConsent;
    }

    public void setExtraConsent(boolean extraConsent) {
        this.extraConsent = extraConsent;
    }

    public boolean isFinalagree() {
        return finalagree;
    }

    public void setFinalagree(boolean finalagree) {
        this.finalagree = finalagree;
    }

    public String getExpirationdate() {
        return expirationdate;
    }

}
