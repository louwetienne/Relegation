package tsc.com.relegation;

/**
 * Created by etienne on 2014/10/07.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // All User Games table name
    private static final String TABLE_USER_GROUPS = "user_groups";

    // Common Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";

    // Login Table Columns names

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";

    // Latest Game Table Column names
    private static final String KEY_ISACTIVE = "isactive";
    private static final String KEY_GAMEID = "gameid";
    private static final String KEY_GROUPID = "groupid";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_USER_GAMES = "CREATE TABLE " + TABLE_USER_GROUPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GROUPID + " INTEGER,"
                + KEY_GAMEID + " INTEGER,"
                + KEY_ISACTIVE + " INTEGER" + ")";
        db.execSQL(CREATE_USER_GAMES);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GROUPS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // unique ID
        values.put(KEY_CREATED_AT, created_at); // Created At
        //values.put(KEY_ID, userID);

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Storing user groups detail in database
     * */
    public void addGroup(Integer groupID, Integer gameID, Integer isActive) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUPID, groupID); // Group ID
        values.put(KEY_GAMEID, gameID); // Game ID
        values.put(KEY_ISACTIVE, isActive); // Is Active

        // Inserting Row
        db.insert(TABLE_USER_GROUPS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            //user.put("userID", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, Integer> getActiveGroupGame(){
        HashMap<String,Integer> game = new HashMap<String,Integer>();
        String selectQuery = "SELECT " + KEY_GROUPID + "," + KEY_GAMEID + " FROM " + TABLE_USER_GROUPS + " WHERE " + KEY_ISACTIVE + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            game.put(KEY_GROUPID, cursor.getInt(0));
            game.put(KEY_GAMEID, cursor.getInt(1));
        }
        cursor.close();
        db.close();
        // return user
        return game;
    }

    /**
     * Updating active game
     * This will take the group ID as a param
     * This will be the game that is currently being viewed in the app.
     * One game will be pulled for each group as there will only be one active game per group
     * */
    public HashMap<String, String> updateActiveGame(Integer groupID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "UPDATE " + TABLE_USER_GROUPS + " SET " + KEY_ISACTIVE + " = 0";
        db.execSQL(selectQuery);

        selectQuery = "UPDATE " + TABLE_USER_GROUPS + " SET " + KEY_ISACTIVE + " = 1 WHERE " + KEY_GROUPID + " = " + groupID.toString();
        db.execSQL(selectQuery);


        db = this.getReadableDatabase();
        HashMap<String,String> game = new HashMap<String,String>();
        selectQuery = "SELECT " + KEY_GROUPID + "," + KEY_GAMEID + " FROM " + TABLE_USER_GROUPS + " WHERE " + KEY_ISACTIVE + " = 1";


        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            game.put(KEY_GROUPID, cursor.getString(0));
            game.put(KEY_GAMEID, cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return user
        return game;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getLoginRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Getting the number of active games
     * return true if rows are there in table
     * */
    public int getActiveGamesRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_GROUPS;
        countQuery = countQuery + " WHERE " + KEY_ISACTIVE + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.delete(TABLE_USER_GROUPS, null, null);
        db.close();
    }

}
