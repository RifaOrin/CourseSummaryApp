package com.example.a2020_1_60_103prouject01;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ClassSummaryDB extends SQLiteOpenHelper {
    public ClassSummaryDB(Context context) {
        super(context, "ClassSummaryDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB@OnCreate");
        String sql = "CREATE TABLE summaries  ("
                + "ID TEXT PRIMARY KEY,"
                + "course TEXT,"
                + "type TEXT,"
                + "datetime INTEGER,"
                + "lecture TEXT,"
                + "topic TEXT,"
                + "summary TEXT"
                + ")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Write code to modify database schema here");
        // db.execSQL("ALTER table my_table  ......");
        // db.execSQL("CREATE TABLE  ......");
    }

    public void insertLecture(String ID, String course, String type, Long date, String lecture, String topic, String summary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();
        cols.put("ID", ID);
        cols.put("course", course);
        cols.put("type", type);
        cols.put("datetime", date);
        cols.put("lecture", lecture);
        cols.put("topic", topic);
        cols.put("summary", summary);
        db.insert("summaries", null ,  cols);
        db.close();
    }

    public void updateLecture(String ID, String course, String type, Long date, String lecture, String topic, String summary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();
        cols.put("course", course);
        cols.put("type", type);
        cols.put("datetime", date);
        cols.put("lecture", lecture);
        cols.put("topic", topic);
        cols.put("summary", summary);
        db.update("summaries", cols, "ID=?", new String[ ] {ID} );
        db.close();
    }
    public void deleteLecture(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("summaries", "ID=?", new String[ ] {ID} );
        db.close();
    }
    public Cursor selectLectures(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = null;
        try {
            cur = db.rawQuery(query, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return cur;
    }



}
