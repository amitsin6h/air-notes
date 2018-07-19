package in.amitsin6h.airnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitsin6h on 10/27/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AirNotes";

    // Contacts table name
    private static final String TABLE_NOTES = "notes";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CAT = "category";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DATETIME = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOTES + " VARCHAR," + KEY_CAT + " VARCHAR," + KEY_DATETIME + " TEXT );";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create tables again
        onCreate(db);
    }


    // Adding new contact
    public void addNotes(Notes notes) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, notes.getNotes());
        values.put(KEY_CAT, notes.getCategory());
        values.put(KEY_DATETIME, notes.getCreatedAt());

        // Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection

    }

    // Getting single contact
    public Notes getNotes(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_NOTES, KEY_CAT, KEY_DATETIME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notes notes = new Notes(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return notes;
    }


    // Getting All Contacts
    public List<Notes> getAllNotes() {
        List<Notes> notesList = new ArrayList<Notes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notes notes = new Notes();
                notes.set_id(Integer.parseInt(cursor.getString(0)));
                notes.setNotes(cursor.getString(1));
                notes.setCategory(cursor.getString(2));
                notes.setCreatedAt(cursor.getString(3));
                // Adding contact to list
                notesList.add(notes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return notesList;
    }




    public List<String> getNotesCategory(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT(" +KEY_CAT+ ") FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //fetch the row
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }

    /*public String getNotesByCategory(String catName){
        //List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT %s FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {catName});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        *//*if (cursor.moveToFirst()) {
            do {
                //fetch the row
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }*//*

        cursor.moveToNext();

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return cursor.getColumnNames(KEY_CAT);
    }*/





    // Getting contacts Count
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //Log.e("MSG: ", ""+cursor.getCount()+"");
        // return count
        return cursor.getCount();
    }




    // Updating single contact
    public int updateNotes(Notes notes) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, notes.getNotes());
        values.put(KEY_CAT, notes.getCategory());
        values.put(KEY_DATETIME, notes.getCreatedAt());

        // updating row
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(notes.get_id()) });
    }

    // Deleting single contact
    public void deleteNotes(Notes notes) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[] { String.valueOf(notes.get_id()) });
        db.close();
    }



}