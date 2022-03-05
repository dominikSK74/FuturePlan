package com.example.futureplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseTests extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TESTS_TABLE";
    public static final String COLUMN_SUBJECT_NAME = "SUBJECT_NAME";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_ID = "ID";

    public DataBaseTests(@Nullable Context context) {
        super(context, "tests.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SUBJECT_NAME + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_TITLE + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);

    }

    public void deleteRow(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COLUMN_TITLE+"='"+title+"'");
        db.close();
    }

    public boolean insertData(HomeworkModel homeworkModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SUBJECT_NAME, homeworkModel.getSubject());
        contentValues.put(COLUMN_DATE, homeworkModel.getDate());
        contentValues.put(COLUMN_TITLE, homeworkModel.getTitle());

        long insert = db.insert(TABLE_NAME, null, contentValues);
        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor fetch() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DataBaseTests.TABLE_NAME, new String[]{DataBaseTests.COLUMN_SUBJECT_NAME,DataBaseTests.COLUMN_DATE, DataBaseTests.COLUMN_TITLE}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private String[][] TestsTable = {
            {"","",""},
            {"","",""},
            {"","",""},
            {"","",""},
            {"","",""},
            {"","",""},
            {"","",""}
    };

    public ArrayList<HashMap<String, String>> getAdapterList(Context ctx){
        DataBaseTests dataBaseTests = new DataBaseTests(ctx);

        Cursor cursor = dataBaseTests.fetch();
        cursor.moveToFirst();

        for(int i = 0 ; i < cursor.getCount(); i++) {
            for (int j = 0; j < TestsTable.length; j++) {
                if(TestsTable[j][0].equals("")) {
                    if (cursor.getString(1) != null) {
                        TestsTable[j][0] = cursor.getString(2);
                        TestsTable[j][1] = cursor.getString(0);
                        TestsTable[j][2] = cursor.getString(1);
                    }
                    break;
                }
            }
            cursor.moveToNext();
        }
        int numberOfFields = 0;
        for (int j = 0; j < TestsTable.length; j++) {
            if(!TestsTable[j][0].equals("")) {
                numberOfFields++;
            }else{
                break;
            }
        }
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item;
        for(int i=0;i<numberOfFields;i++){
            item = new HashMap<String,String>();
            item.put( "line1", TestsTable[i][0]);
            item.put( "line2", TestsTable[i][1]);
            item.put( "line3", TestsTable[i][2]);
            list.add( item );
        }




        return list;
    }

    public ArrayList<HashMap<String, String>> getListTests(Context ctx, String date){
        DataBaseTests dataBaseTests = new DataBaseTests(ctx);

        Cursor cursor = dataBaseTests.fetch();
        cursor.moveToFirst();
        System.out.println(date);

        for(int i = 0 ; i < cursor.getCount(); i++) {
            if (cursor.getString(1).equals(date)) {
                for (int j = 0; j < TestsTable.length; j++) {
                    if(TestsTable[j][0].equals("")) {
                        if (cursor.getString(1) != null) {
                            TestsTable[j][0] = cursor.getString(0);
                            TestsTable[j][1] = cursor.getString(2);
                        }
                        break;
                    }
                }
            }
            cursor.moveToNext();
        }
        int numberOfFields = 0;
        for (int j = 0; j < TestsTable.length; j++) {
            if(!TestsTable[j][0].equals("")) {
                numberOfFields++;
            }else{
                break;
            }
        }


        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item;
        for(int i=0;i<numberOfFields;i++){
            item = new HashMap<String,String>();
            item.put( "line1", TestsTable[i][0]);
            item.put( "line2", TestsTable[i][1]);
            list.add( item );
        }




        return list;
    }

}
