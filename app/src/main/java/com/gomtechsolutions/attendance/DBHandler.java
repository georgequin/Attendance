package com.gomtechsolutions.attendance;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.

    // below variable is for our database name.
    private static final String DB_NAME = "attendance_db";

    // below int is our database version
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "students";
    private static final String ATTENDANCE_TABLE ="attendance";
    private static final String COL_ID = "id";
    private static final String COL_DEPARTMENT = "dept";
    private static final String COL_MEASURE = "measure";
    private static final String COL_REG_NO = "reg_no";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_LEFT_FINGERPRINT = "left_finger_print";
    private static final String COL_RIGHT_FINGERPRINT = "right_finger_print";
    private static final String COL_ATT_NAME = "name";
    private static final String COL_ATT_REG_NO = "reg_no";
    private static final String COL_ATT_ID = "id";
    private static final String COL_ATT_DATE = "date";




    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql  = "CREATE TABLE " + ATTENDANCE_TABLE + "("
                + COL_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ATT_NAME + " varchar,"
                + COL_ATT_DATE + " varchar,"
                + COL_ATT_REG_NO + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP )";

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DEPARTMENT + " varchar,"
                + COL_MEASURE + " varchar,"
                + COL_REG_NO + " varchar,"
                + COL_NAME + " varchar,"
                + COL_EMAIL + " varchar,"
                + COL_LEFT_FINGERPRINT + " bolb,"
                + COL_RIGHT_FINGERPRINT + " bolb)";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(sql);
    }
    public void addAttendance(String name, String reg_no){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ATT_NAME,name);
        values.put(COL_ATT_REG_NO,reg_no);

        sqLiteDatabase.insert(ATTENDANCE_TABLE, null, values);
        sqLiteDatabase.close();

    }
    public void addStudent(String dept, String measure, String reg_no, String name, String email, Bitmap left_fingerprint, Bitmap right_fingerprint){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DEPARTMENT,dept);
        values.put(COL_MEASURE,measure);
        values.put(COL_REG_NO,reg_no);
        values.put(COL_NAME, name);
        values.put(COL_EMAIL,email);
        values.put(COL_LEFT_FINGERPRINT, String.valueOf(left_fingerprint));
        values.put(COL_RIGHT_FINGERPRINT, String.valueOf(right_fingerprint));

        sqLiteDatabase.insert(TABLE_NAME,null,values);
        sqLiteDatabase.close();
    }
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<ArrayList<String> > readStudent() {
        ArrayList<String> stdName = new ArrayList<String>();
        ArrayList<String> stdFinger = new ArrayList<String>();
        ArrayList<String> reg_no = new ArrayList<String>();
        ArrayList<ArrayList<String>> std = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorStudents = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursorStudents.moveToFirst();
        while (cursorStudents.isAfterLast() == false){
            stdName.add(cursorStudents.getString(cursorStudents.getColumnIndex(COL_NAME)));
            stdFinger.add(cursorStudents.getString(cursorStudents.getColumnIndex(COL_LEFT_FINGERPRINT)).getBytes().toString());
            reg_no.add(cursorStudents.getString(cursorStudents.getColumnIndex(COL_REG_NO)));
            cursorStudents.moveToNext();
        }
        std.add(stdName);
        std.add(stdFinger);
        std.add(reg_no);
        return std;
    }
    public ArrayList<ArrayList<String>> readAttendance() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();
        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorStudents = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE, null);
        ArrayList<ArrayList<String>> students =new ArrayList<ArrayList<String>>();
        cursorStudents.moveToFirst();
        do{
            ArrayList<String> studentArrayList = new ArrayList<>();
            studentArrayList.add(cursorStudents.getString(0));//id
            studentArrayList.add(cursorStudents.getString(1));//date
            students.add(studentArrayList);
            studentArrayList.clear();
        }while (cursorStudents.moveToNext());
        // at last closing our cursor
        // and returning our array list.
        cursorStudents.close();
        return students;
    }
    public ArrayList<ArrayList<String>> getAttendanceByDate(Date date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorStudents = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE + "WHERE date = "+ date + "", null);
        ArrayList<ArrayList<String>> attendance =new ArrayList<ArrayList<String>>();
        cursorStudents.moveToFirst();
        do{
            ArrayList<String> studentArrayList = new ArrayList<>();
            studentArrayList.add(cursorStudents.getString(0));//id
            studentArrayList.add(cursorStudents.getString(1));//date
            attendance.add(studentArrayList);
            studentArrayList.clear();
        }while (cursorStudents.moveToNext());
        cursorStudents.close();
        return attendance;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // this method is called to check if the table exists already.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
