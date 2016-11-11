package com.alanmarksp.marvelapimiddleware.helpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alanmarksp.marvelapimiddleware.contracts.CharacterContract.CharacterEntry;
import com.alanmarksp.marvelapimiddleware.contracts.ComicContract.ComicEntry;
import com.alanmarksp.marvelapimiddleware.contracts.HttpCacheContract.HttpCacheEntry;

public class MarvelApiDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "MarvelApiMiddleware.db";

    private static final String CHARACTER_SQL_CREATE_ENTRIES = "CREATE TABLE " +
            CharacterEntry.TABLE_NAME + " (" +
            CharacterEntry._ID + " INTEGER PRIMARY KEY," +
            CharacterEntry.COLUMN_NAME_CHARACTER_ID + " INTEGER," +
            CharacterEntry.COLUMN_NAME_NAME + " TEXT," +
            CharacterEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            CharacterEntry.COLUMN_NAME_RATE + " REAL DEFAULT 0," +
            CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH + " TEXT," +
            CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION + " TEXT)";

    private static final String CHARACTER_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            CharacterEntry.TABLE_NAME;

    private static final String COMIC_SQL_CREATE_ENTRIES = "CREATE TABLE " +
            ComicEntry.TABLE_NAME + " (" +
            ComicEntry._ID + " INTEGER PRIMARY KEY," +
            ComicEntry.COLUMN_NAME_COMIC_ID + " INTEGER," +
            ComicEntry.COLUMN_NAME_TITLE + " TEXT," +
            ComicEntry.COLUMN_NAME_ISSUE_NUMBER + " REAL," +
            ComicEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            ComicEntry.COLUMN_NAME_VARIANT_DESCRIPTION + " TEXT," +
            ComicEntry.COLUMN_NAME_ISBN + " TEXT," +
            ComicEntry.COLUMN_NAME_FORMAT + " TEXT," +
            ComicEntry.COLUMN_NAME_PAGE_COUNT + " INTEGER," +
            ComicEntry.COLUMN_NAME_THUMBNAIL_PATH + " TEXT," +
            ComicEntry.COLUMN_NAME_THUMBNAIL_EXTENSION + " TEXT)";

    private static final String COMIC_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            ComicEntry.TABLE_NAME;

    private static final String HTTP_CACHE_SQL_CREATE_ENTRIES = "CREATE TABLE " +
            HttpCacheEntry.TABLE_NAME + " (" +
            HttpCacheEntry._ID + " INTEGER PRIMARY KEY," +
            HttpCacheEntry.COLUMN_NAME_URL + " TEXT," +
            HttpCacheEntry.COLUMN_NAME_ETAG + " TEXT," +
            HttpCacheEntry.COLUMN_NAME_DATE + " INTEGER," +
            HttpCacheEntry.COLUMN_NAME_OFFSET + " INTEGER DEFAULT 0," +
            "UNIQUE (" + HttpCacheEntry.COLUMN_NAME_URL + "," +
            HttpCacheEntry.COLUMN_NAME_OFFSET + ") ON CONFLICT REPLACE )";

    private static final String HTTP_CACHE_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            HttpCacheEntry.TABLE_NAME;

    public MarvelApiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHARACTER_SQL_CREATE_ENTRIES);
        db.execSQL(COMIC_SQL_CREATE_ENTRIES);
        db.execSQL(HTTP_CACHE_SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(CHARACTER_SQL_DELETE_ENTRIES);
        db.execSQL(COMIC_SQL_DELETE_ENTRIES);
        db.execSQL(HTTP_CACHE_SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
