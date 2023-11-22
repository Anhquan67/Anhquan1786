package com.example.hikeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hikeapp.model.HikeModel;
import com.example.hikeapp.model.ObservationItem;

import java.util.ArrayList;
import java.util.UUID;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "hike_table";
    private static final String OBSERVATION_TABLE_NAME = "observation_table";
    private static final String KEY_ID = "id";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";
    private static final String KEY_IS_PARKING = "is_parking";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_OBSERVATION = "observation";
    private static final String KEY_DATE_OBSERVATION = "date_observation";
    private static final String KEY_TIME_OBSERVATION = "time_observation";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_HIKE_PARENT_KEY = "hike_uuid";

    public DatabaseHandler(Context context) {
        super(context, "HIKE_DATABASE", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createHikeTable = String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_NAME,
                KEY_ID,
                KEY_UUID,
                KEY_NAME,
                KEY_LOCATION,
                KEY_DATE,
                KEY_IS_PARKING,
                KEY_LENGTH,
                KEY_LEVEL,
                KEY_DESCRIPTION,
                KEY_IMAGE
        );
        String createObservationItemTable = String.format(
                "CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                OBSERVATION_TABLE_NAME,
                KEY_HIKE_PARENT_KEY,
                KEY_OBSERVATION,
                KEY_DATE_OBSERVATION,
                KEY_TIME_OBSERVATION,
                KEY_COMMENT
        );
        db.execSQL(createHikeTable);
        db.execSQL(createObservationItemTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropHikeTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        String dropObservationItemTable = String.format("DROP TABLE IF EXISTS %s", OBSERVATION_TABLE_NAME);
        db.execSQL(dropHikeTable);
        db.execSQL(dropObservationItemTable);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String dropExpenseTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        String dropExpenseItemTable = String.format("DROP TABLE IF EXISTS %s", OBSERVATION_TABLE_NAME);
        db.execSQL(dropExpenseTable);
        db.execSQL(dropExpenseItemTable);
        onCreate(db);
    }

    public void addHikeModel(HikeModel hikeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UUID, UUID.randomUUID().toString());
        values.put(KEY_NAME, hikeModel.getName());
        values.put(KEY_LOCATION, hikeModel.getLocation());
        values.put(KEY_DATE, hikeModel.getDate());
        values.put(KEY_IS_PARKING, hikeModel.isParkingAvailable());
        values.put(KEY_LEVEL, hikeModel.getLevel());
        values.put(KEY_LENGTH, hikeModel.getLength());
        values.put(KEY_DESCRIPTION, hikeModel.getDescription());
        values.put(KEY_IMAGE, hikeModel.getImage());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addObservationItem(ObservationItem observationItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HIKE_PARENT_KEY, observationItem.getUuid());
        values.put(KEY_OBSERVATION, observationItem.getObservation());
        values.put(KEY_DATE_OBSERVATION, observationItem.getDate());
        values.put(KEY_TIME_OBSERVATION, observationItem.getTime());
        values.put(KEY_COMMENT, observationItem.getComment());
        db.insert(OBSERVATION_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HikeModel> getAllHikeModel() {
        ArrayList<HikeModel> hikeModelList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HikeModel hikeModel = new HikeModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            );
            hikeModelList.add(hikeModel);
            cursor.moveToNext();
        }
        cursor.close();
        return hikeModelList;
    }

    public ArrayList<ObservationItem> getAllObservationItemByParentId(String hikeUUID) {
        ArrayList<ObservationItem> observationItemList = new ArrayList<>();
        String hikeUuid = "'" + hikeUUID + "'";
        String query = "SELECT * FROM " + OBSERVATION_TABLE_NAME + " WHERE " + KEY_HIKE_PARENT_KEY + " like " + hikeUuid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ObservationItem observationItem = new ObservationItem(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            observationItemList.add(observationItem);
            cursor.moveToNext();
        }
        cursor.close();
        return observationItemList;
    }

    public HikeModel getHikeByUUID(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME, null,
                KEY_UUID + " = ?", new String[]{uuid}, null, null, null
        );
        cursor.moveToFirst();
        HikeModel hikeModel = new HikeModel(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
        );
        cursor.close();
        return hikeModel;
    }

    public HikeModel getHikeByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME, null,
                KEY_NAME + " like '%" + name + "%'", null, null, null, null
        );
        cursor.moveToFirst();
        HikeModel hikeModel = new HikeModel(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
        );
        cursor.close();
        return hikeModel;
    }

    public void deleteHike(String hikeUUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_UUID + " = ?", new String[]{hikeUUID});
        db.delete(OBSERVATION_TABLE_NAME, KEY_HIKE_PARENT_KEY + " = ?", new String[]{hikeUUID});
        db.close();
    }

    public void updateHike(HikeModel hikeModel, String hikeUUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, hikeModel.getName());
        values.put(KEY_LOCATION, hikeModel.getLocation());
        values.put(KEY_DATE, hikeModel.getDate());
        values.put(KEY_IS_PARKING, hikeModel.isParkingAvailable());
        values.put(KEY_LENGTH, hikeModel.getLength());
        values.put(KEY_LEVEL, hikeModel.getLevel());
        values.put(KEY_DESCRIPTION, hikeModel.getDescription());
        db.update(TABLE_NAME, values, KEY_UUID + " = ?", new String[]{hikeUUID});
        db.close();
    }

    public void updateHikeImage(String filePath, String hikeUUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, filePath);
        db.update(TABLE_NAME, values, KEY_UUID + " = ?", new String[]{hikeUUID});
        db.close();
    }
}


