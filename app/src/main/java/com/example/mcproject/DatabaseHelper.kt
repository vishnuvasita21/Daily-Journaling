package com.example.mcproject
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "JournalDatabase"
        const val TABLE_NAME = "journals"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TAGS = "tags"
        const val COLUMN_IMAGE_URL = "imageUrl"
        const val COLUMN_IS_STARRED = "is_starred"
        const val COLUMN_DATE = "date" // New column for date
        const val COLUMN_LOC = "location"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_TAGS + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOC + " TEXT,"
                + COLUMN_IS_STARRED + " INTEGER"+")")
        db.execSQL(CREATE_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
}
