package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_NOTE_TEXT = "note_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_IMAGE_URI + " TEXT, "
                + COLUMN_NOTE_TEXT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void saveNote(String imageUri, String noteText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_URI, imageUri);
        values.put(COLUMN_NOTE_TEXT, noteText);

        int rowsAffected = db.update(TABLE_NOTES, values, COLUMN_IMAGE_URI + " = ?",
                new String[]{imageUri});
        if (rowsAffected == 0) {
            db.insert(TABLE_NOTES, null, values);
        }
    }

    public String getNote(String imageUri) {
        SQLiteDatabase db = this.getReadableDatabase();
        String note = "";

        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_NOTE_TEXT},
                COLUMN_IMAGE_URI + " = ?", new String[]{imageUri},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TEXT));
            cursor.close();
        }
        return note;
    }
}
