package com.example.hhj73.fix;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by skrud on 2018-06-08.
 */

public class ContractData {

    private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    private Date contractwritedate;
    private String studentName;
    private String seniorName;
    private Date startdate;
    private int monthperiod;
    private int monthlyfee;
    private boolean breakfast_j;
    private boolean breakfast_s;
    private boolean smoking_j;
    private boolean smoking_s;
    private boolean pet_j;
    private boolean pet_s;
    private boolean cerfew_j;
    private boolean cerfew_s;
    private int homecomingtime;
    private boolean help_j;
    private boolean help_s;
    private ArrayList<String> extraspecial;
    private boolean finalagree;


    //생성자 오버로딩
    //특별사항이 없는 경우
    ContractData(Date startdate, String studentName, String seniorName, int monthperiod, int monthlyfee, boolean breakfast_j, boolean breakfast_s, boolean smoking_j, boolean smoking_s,
                 boolean pet_j, boolean pet_s, boolean cerfew_j, boolean cerfew_s, boolean help_j, boolean help_s){
        this.studentName = studentName;
        this.seniorName = seniorName;
        this.startdate = startdate;
        this.monthperiod = monthperiod;
        this.monthlyfee = monthlyfee;
        this.breakfast_j = breakfast_j;
        this.breakfast_s = breakfast_s;
        this.smoking_j = smoking_j;
        this.smoking_s = smoking_s;
        this.pet_j = pet_j;
        this.pet_s = pet_s;
        this.cerfew_j = cerfew_j;
        this.cerfew_s = cerfew_s;
        this.help_j = help_j;
        this.help_s = help_s;
        this.finalagree = false;
    }
    //특별사항 있는 경우
    ContractData(Date startdate, int monthperiod, int monthlyfee, boolean breakfast_j, boolean breakfast_s, boolean smoking_j, boolean smoking_s,
                 boolean pet_j, boolean pet_s, boolean cerfew_j, boolean cerfew_s, boolean help_j, boolean help_s, ArrayList<String> extraspecial){
        this.startdate = startdate;
        this.monthperiod = monthperiod;
        this.monthlyfee = monthlyfee;
        this.breakfast_j = breakfast_j;
        this.breakfast_s = breakfast_s;
        this.smoking_j = smoking_j;
        this.smoking_s = smoking_s;
        this.pet_j = pet_j;
        this.pet_s = pet_s;
        this.cerfew_j = cerfew_j;
        this.cerfew_s = cerfew_s;
        this.help_j = help_j;
        this.help_s = help_s;
        this.extraspecial = extraspecial;
        this.finalagree = false;
    }

    public Date getstartdate() {
        return startdate;
    }
    public void setstartdate(Date startdate) {
        this.startdate = startdate;
    }
    public int getmonthperiod() {
        return monthperiod;
    }
    public void setmonthperiod(int monthperiod) {
        this.monthperiod = monthperiod;
    }
    public int getmonthlyfee() {
        return monthlyfee;
    }
    public void setmonthlyfee(int monthlyfee) {
        this.monthlyfee = monthlyfee;
    }

    public boolean isBreakfast_j() {
        return breakfast_j;
    }

    public void setBreakfast_j(boolean breakfast_j) {
        this.breakfast_j = breakfast_j;
    }

    public boolean isBreakfast_s() {
        return breakfast_s;
    }

    public void setBreakfast_s(boolean breakfast_s) {
        this.breakfast_s = breakfast_s;
    }

    public boolean isSmoking_j() {
        return smoking_j;
    }

    public void setSmoking_j(boolean smoking_j) {
        this.smoking_j = smoking_j;
    }

    public boolean isSmoking_s() {
        return smoking_s;
    }

    public void setSmoking_s(boolean smoking_s) {
        this.smoking_s = smoking_s;
    }

    public boolean isPet_j() {
        return pet_j;
    }

    public void setPet_j(boolean pet_j) {
        this.pet_j = pet_j;
    }

    public boolean isPet_s() {
        return pet_s;
    }

    public void setPet_s(boolean pet_s) {
        this.pet_s = pet_s;
    }

    public boolean isCerfew_j() {
        return cerfew_j;
    }

    public void setCerfew_j(boolean cerfew_j) {
        this.cerfew_j = cerfew_j;
    }

    public boolean isCerfew_s() {
        return cerfew_s;
    }

    public void setCerfew_s(boolean cerfew_s) {
        this.cerfew_s = cerfew_s;
    }

    public int gethomecomingtime() {
        return homecomingtime;
    }
    public void sethomecomingtime(int homecomingtime) {
        this.homecomingtime = homecomingtime;
    }

    public boolean isHelp_j() {
        return help_j;
    }

    public void setHelp_j(boolean help_j) {
        this.help_j = help_j;
    }

    public boolean isHelp_s() {
        return help_s;
    }
    public void setHelp_s(boolean help_s) {
        this.help_s = help_s;
    }

    public ArrayList<String> getExtraspecial() {
        return extraspecial;
    }
    public void setExtraspecial(ArrayList<String> extraspecial) {
        this.extraspecial = extraspecial;
    }
    public boolean isFinalagree() {
        return finalagree;
    }
    public void setfinalagree(boolean finalagree) {
        this.finalagree = finalagree;
    }

    public Date getcontractwritedate() {
        return contractwritedate;
    }
    //계약서 서로 동의 누를 때 그날 날짜로 들어감.
    public void setcontractwritedate(Date contractdate) {
        this.contractwritedate = contractdate;
    }
}
