package com.example.hhj73.fix;

/**
 * Created by hhj73 on 2018-04-17.
 */

public class User {
    boolean type; // true: 노인, false: 학생
    String id;
    String pw;
    String name;
    String bday;
    boolean gender; // true: 여성, false: 남성
    String phone;
    String address;
    String cost;
    boolean smoking, curfew, pet, help;
    String unique;

    public User(boolean type, String id, String pw, String name, String bday, boolean gender,
                String phone, String address, String cost, boolean smoking, boolean curfew,
                boolean pet, boolean help, String unique) {

        this.type = type;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.bday = bday;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.cost = cost;
        this.smoking = smoking;
        this.curfew = curfew;
        this.pet = pet;
        this.help = help;
        this.unique = unique;
    }

    public boolean getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public String getPw() {
        return this.pw;
    }

    public String getName() {
        return this.name;
    }

    public String getBday() {
        return this.bday;
    }

    public boolean getGender() {
        return this.gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCost() {
        return this.cost;
    }

    public boolean getSmoking() {
        return this.smoking;
    }

    public boolean getCurfew() {
        return this.curfew;
    }

    public boolean getPet() {
        return this.pet;
    }

    public boolean getHelp() {
        return this.help;
    }

    public String getUnique() {
        return this.unique;
    }


}
