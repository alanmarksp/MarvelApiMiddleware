package com.alanmarksp.marvelapimiddleware.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alanmarksp.marvelapimiddleware.contracts.CharacterContract.CharacterEntry;
import com.alanmarksp.marvelapimiddleware.dao.CharacterDao;
import com.alanmarksp.marvelapimiddleware.helpers.MarvelApiDBHelper;


public class MarvelApiContentProvider extends ContentProvider {

    private static final String QUERY_PARAMETER_OFFSET = "offset";

    private static final String AUTHORITY = MarvelApiContentProvider.class.getPackage().getName();

    private static final String CHARACTER_ENTITY = "characters";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int CHARACTERS = 1;
    private static final int CHARACTER = 2;

    private MarvelApiDBHelper marvelApiDBHelper;

    private CharacterDao characterDao;

    static {
        uriMatcher.addURI(AUTHORITY, CHARACTER_ENTITY, CHARACTERS);
        uriMatcher.addURI(AUTHORITY, CHARACTER_ENTITY + "/#", CHARACTER);
    }

    @Override
    public boolean onCreate() {
        marvelApiDBHelper = new MarvelApiDBHelper(getContext());
        characterDao = new CharacterDao(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)) {
            case CHARACTERS: {
                int offset = Integer.parseInt(uri.getQueryParameter(QUERY_PARAMETER_OFFSET));

                return characterDao.listCursor(offset);
            }
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CHARACTERS: {
                return "marvelApi.dir/characters";
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CHARACTER:

                if (selection != null) {
                    selection += CharacterEntry.COLUMN_NAME_CHARACTER_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = CharacterEntry.COLUMN_NAME_CHARACTER_ID + " = " + uri.getLastPathSegment();
                }

                return db.update(CharacterEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        }
        return 0;
    }
}
