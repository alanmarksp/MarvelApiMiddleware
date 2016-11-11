package com.alanmarksp.marvelapimiddleware.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alanmarksp.marvelapimiddleware.contracts.HttpCacheContract.HttpCacheEntry;
import com.alanmarksp.marvelapimiddleware.helpers.MarvelApiDBHelper;
import com.alanmarksp.marvelapimiddleware.models.HttpCache;

import java.util.Date;

public class HttpCacheDao {

    private MarvelApiDBHelper marvelApiDBHelper;

    public HttpCacheDao(Context context) {
        marvelApiDBHelper = new MarvelApiDBHelper(context);
    }

    public long insert(HttpCache httpCache) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HttpCacheEntry.COLUMN_NAME_URL, httpCache.getUrl());
        values.put(HttpCacheEntry.COLUMN_NAME_ETAG, httpCache.getETag());
        values.put(HttpCacheEntry.COLUMN_NAME_DATE, httpCache.getDate().getTime());
        values.put(HttpCacheEntry.COLUMN_NAME_OFFSET, httpCache.getOffset());

        return db.insert(HttpCacheEntry.TABLE_NAME, null, values);
    }

    public HttpCache retrieve(String url, int offset) {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                HttpCacheEntry.COLUMN_NAME_URL,
                HttpCacheEntry.COLUMN_NAME_ETAG,
                HttpCacheEntry.COLUMN_NAME_DATE,
                HttpCacheEntry.COLUMN_NAME_OFFSET,
        };

        String selection = HttpCacheEntry.COLUMN_NAME_URL + " = ? AND " +
                HttpCacheEntry.COLUMN_NAME_OFFSET + " = ? ";
        String[] selectionArgs = {url, String.valueOf(offset)};

        Cursor cursor = db.query(HttpCacheEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        HttpCache httpCache = null;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            httpCache = new HttpCache(
                    cursor.getString(cursor.getColumnIndex(HttpCacheEntry.COLUMN_NAME_URL)),
                    cursor.getString(cursor.getColumnIndex(HttpCacheEntry.COLUMN_NAME_ETAG)),
                    new Date(cursor.getLong(cursor.getColumnIndex(HttpCacheEntry.COLUMN_NAME_DATE))),
                    cursor.getInt(cursor.getColumnIndex(HttpCacheEntry.COLUMN_NAME_OFFSET))
            );
        }

        cursor.close();

        return httpCache;
    }

    public int update(HttpCache httpCache) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HttpCacheEntry.COLUMN_NAME_URL, httpCache.getUrl());
        values.put(HttpCacheEntry.COLUMN_NAME_ETAG, httpCache.getETag());
        values.put(HttpCacheEntry.COLUMN_NAME_DATE, httpCache.getDate().getTime());
        values.put(HttpCacheEntry.COLUMN_NAME_OFFSET, httpCache.getOffset());

        String selection = HttpCacheEntry.COLUMN_NAME_URL + " = ? AND " +
                HttpCacheEntry.COLUMN_NAME_OFFSET + " = ? ";
        String[] selectionArgs = {httpCache.getUrl(), String.valueOf(httpCache.getOffset())};

        return db.update(HttpCacheEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public int invalidate(String url) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HttpCacheEntry.COLUMN_NAME_DATE, 0);

        String selection = HttpCacheEntry.COLUMN_NAME_URL + " = ? ";
        String[] selectionArgs = {url};

        return db.update(HttpCacheEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public int delete(HttpCache httpCache) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        String selection = HttpCacheEntry.COLUMN_NAME_URL + " = ? " +
                HttpCacheEntry.COLUMN_NAME_OFFSET + " = ? ";
        String[] selectionArgs = {httpCache.getUrl(), String.valueOf(httpCache.getOffset())};

        return db.delete(HttpCacheEntry.TABLE_NAME, selection, selectionArgs);
    }
}
