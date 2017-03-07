package com.fame.plumbum.lithicsin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 23/7/16.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lithics_db";

    private static final String TABLE_CHAT = "chat";
    private static final String TABLE_NOTIF = "notif";

    private static final String KEY_ID = "id";
    private static final String STATUS = "status";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_NAME = "remote_name";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STATUS + " INT NOT NULL, "
                + REMOTE_ID + " TEXT NOT NULL, " + REMOTE_NAME + " TEXT NOT NULL, "
                + MESSAGE + " TEXT NOT NULL, " + TIMESTAMP + " TEXT NOT NULL" + ")";
        String CREATE_TABLE_NOTIF = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIF + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MESSAGE + " TEXT NOT NULL,"
                + TIMESTAMP + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_NOTIF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIF);
        onCreate(db);
    }

    public void addChat(ChatTable chatTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, chatTable.getStatus());
        values.put(REMOTE_ID, chatTable.getRemote_id());
        values.put(REMOTE_NAME, chatTable.getRemote_name());
        values.put(MESSAGE, chatTable.getMessage());
        values.put(TIMESTAMP, chatTable.getTimestamp());

        db.insert(TABLE_CHAT, null, values);
    }

    public void addNotif(NotifTable table) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MESSAGE, table.getMessage());
        values.put(TIMESTAMP, table.getTimestamp());

        db.insert(TABLE_NOTIF, null, values);
    }

    public List<NotifTable> getNotifs() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_NOTIF;
        Cursor cursor = db.rawQuery(getQuery, null);
        List<NotifTable> notifTable = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notifTable.add(new NotifTable(cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notifTable;
    }


    public List<ChatTable> getChat(String remote_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + REMOTE_ID + " = ?";
        Cursor cursor = db.rawQuery(getQuery, new String[]{REMOTE_ID});
        List<ChatTable> chatTables = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                chatTables.add(new ChatTable(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return chatTables;
    }

    public int getChatCount(String remote_id) {
        String countQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + REMOTE_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{remote_id});
        cursor.close();
        return cursor.getCount();
    }

    public void deleteChat(ChatTable chat) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT, KEY_ID + "=?", new String[]{String.valueOf(chat.getId())});
    }

    public void deleteNotif(NotifTable notif) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIF, KEY_ID + "=?", new String[]{String.valueOf(notif.getId())});
    }

    public List<ChatTable> getPeronalChats(String remote) {
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + REMOTE_ID + " = ?";
        Cursor cursor = db.rawQuery(getQuery, new String[]{remote});

        List<ChatTable> chatTables = new ArrayList<>();
        List<String> remote_chats = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int i = 0;
            for (; i < remote_chats.size(); i++) {
                if (remote_chats.get(i).contentEquals(cursor.getString(3)))
                    break;
            }
            if (i == remote_chats.size()) {
                int counter = 1;
                chatTables.add(new ChatTable(cursor.getInt(counter), cursor.getString(counter + 1), cursor.getString(counter + 2), cursor.getString(counter + 3), cursor.getString(counter + 4)));
                remote_chats.add(cursor.getString(1));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return chatTables;
    }
}