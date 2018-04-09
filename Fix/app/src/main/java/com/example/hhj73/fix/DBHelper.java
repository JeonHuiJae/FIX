package com.example.hhj73.fix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hhj73 on 2018-03-21.
 */

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {

        // 노인 테이블
        // 이름, 성별, 흡연, 통금, 반려동물, 주거비용, 도움, 주소, 특이사항
        db.execSQL("CREATE TABLE SENIOR (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, gender BOOLEAN, smoke BOOLEAN, curfew BOOLEAN, pet INTEGER," +
                "help BOOLEAN, address TEXT, information TEXT);");

        // 학생 테이블
        // 이름, 성별, 흡연, 통금, 반려동물, 주거비용, 도움, 특이사항
        db.execSQL("CREATE TABLE STUDENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, gender BOOLEAN, smoke BOOLEAN, curfew BOOLEAN, pet INTEGER, " +
                "help BOOLEAN, information TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //////////////////// 데이터 저장 /////////////////////////

    public void insertSenior(String name, boolean gender, boolean smoke, boolean curfew,
                             int pet, boolean help, String address, String information) {
        // 노인 정보를 데이터 베이스에 추가하는 함수

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        String str = name + "," + gender + "," + smoke + "," + curfew + ","  + pet +"," + help + "," + address + "," + information;
        db.execSQL("INSERT INTO SENIOR VALUES (" + str +");");
        db.close();
    }

    public void  insertStudent(String name, boolean gender, boolean smoke, boolean curfew, int pet, boolean help, String information) {
        // 학생 정보를 데이터 베이스에 추가하는 함수

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        String str = name + "," + gender + "," + smoke + "," + curfew + ","  + pet +"," + help + "," + information;
        db.execSQL("INSERT INTO STUDENT VALUES (" + str +");");
        db.close();
    }


    //////////////////// 데이터 삭제 /////////////////////////

    public void deleteSenior(String name) {
        SQLiteDatabase db = getWritableDatabase();

        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM PRODUCT WHERE name='"+name+"';");
        db.close();
    }

    public void deleteStudent(String name) {
        SQLiteDatabase db = getWritableDatabase();

        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM PRODUCT WHERE name='"+name+"';");
        db.close();
    }

    //////////////////// 데이터 꺼내기 /////////////////////////

}
