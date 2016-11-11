package com.alanmarksp.marvelapimiddleware.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.alanmarksp.marvelapimiddleware.contracts.CharacterContract.CharacterEntry;
import com.alanmarksp.marvelapimiddleware.helpers.MarvelApiDBHelper;
import com.alanmarksp.marvelapimiddleware.models.Character;
import com.alanmarksp.marvelapimiddleware.models.Image;

import java.util.ArrayList;
import java.util.List;

public class CharacterDao {

    private static final int DB_PAGE_SIZE = 10;
    private MarvelApiDBHelper marvelApiDBHelper;

    public CharacterDao(Context context) {
        marvelApiDBHelper = new MarvelApiDBHelper(context);
    }

    public List<Character> list() {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                CharacterEntry.COLUMN_NAME_CHARACTER_ID,
                CharacterEntry.COLUMN_NAME_NAME,
                CharacterEntry.COLUMN_NAME_DESCRIPTION,
                CharacterEntry.COLUMN_NAME_RATE,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION
        };

        Cursor cursor = db.query(CharacterEntry.TABLE_NAME, projection, null, null, null, null, null, null);

        return getCharacters(cursor);
    }

    public List<Character> list(int offset) {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                CharacterEntry.COLUMN_NAME_CHARACTER_ID,
                CharacterEntry.COLUMN_NAME_NAME,
                CharacterEntry.COLUMN_NAME_DESCRIPTION,
                CharacterEntry.COLUMN_NAME_RATE,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION
        };

        Cursor cursor = db.query(CharacterEntry.TABLE_NAME, projection, null, null, null, null, null, offset * DB_PAGE_SIZE + "," + DB_PAGE_SIZE);

        return getCharacters(cursor);
    }

    public Cursor listCursor() {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                CharacterEntry.COLUMN_NAME_CHARACTER_ID,
                CharacterEntry.COLUMN_NAME_NAME,
                CharacterEntry.COLUMN_NAME_DESCRIPTION,
                CharacterEntry.COLUMN_NAME_RATE,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION
        };

        return db.query(CharacterEntry.TABLE_NAME, projection, null, null, null, null, null, null);
    }

    public Cursor listCursor(int offset) {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                CharacterEntry.COLUMN_NAME_CHARACTER_ID,
                CharacterEntry.COLUMN_NAME_NAME,
                CharacterEntry.COLUMN_NAME_DESCRIPTION,
                CharacterEntry.COLUMN_NAME_RATE,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION
        };

        return db.query(CharacterEntry.TABLE_NAME, projection, null, null, null, null, null, offset * DB_PAGE_SIZE + "," + DB_PAGE_SIZE);
    }

    @NonNull
    private List<Character> getCharacters(Cursor cursor) {
        List<Character> characters = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Character character = new Character(
                    cursor.getInt(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_CHARACTER_ID)),
                    cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_RATE)),
                    new Image(
                            cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH)),
                            cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION))
                    )
            );

            characters.add(character);
        }

        return characters;
    }

    public long insert(Character character) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CharacterEntry.COLUMN_NAME_CHARACTER_ID, character.getId());
        values.put(CharacterEntry.COLUMN_NAME_NAME, character.getName());
        values.put(CharacterEntry.COLUMN_NAME_DESCRIPTION, character.getDescription());
        values.put(CharacterEntry.COLUMN_NAME_RATE, character.getRate());
        values.put(CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH, character.getThumbnail().getPath());
        values.put(CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION, character.getThumbnail().getExtension());

        return db.insert(CharacterEntry.TABLE_NAME, null, values);
    }

    public Character retrieve(int characterId) {
        SQLiteDatabase db = marvelApiDBHelper.getReadableDatabase();

        String[] projection = {
                CharacterEntry.COLUMN_NAME_CHARACTER_ID,
                CharacterEntry.COLUMN_NAME_NAME,
                CharacterEntry.COLUMN_NAME_DESCRIPTION,
                CharacterEntry.COLUMN_NAME_RATE,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH,
                CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION
        };

        String selection = CharacterEntry.COLUMN_NAME_CHARACTER_ID + " = ? ";

        String[] selectionArgs = {String.valueOf(characterId)};

        Cursor cursor = db.query(CharacterEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Character character = null;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            character = new Character(
                    cursor.getInt(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_CHARACTER_ID)),
                    cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_RATE)),
                    new Image(
                            cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH)),
                            cursor.getString(cursor.getColumnIndex(CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION))
                    )
            );
        }

        cursor.close();

        return character;
    }

    public int update(Character character) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CharacterEntry.COLUMN_NAME_CHARACTER_ID, character.getId());
        values.put(CharacterEntry.COLUMN_NAME_NAME, character.getName());
        values.put(CharacterEntry.COLUMN_NAME_DESCRIPTION, character.getDescription());
        values.put(CharacterEntry.COLUMN_NAME_RATE, character.getRate());
        values.put(CharacterEntry.COLUMN_NAME_THUMBNAIL_PATH, character.getThumbnail().getPath());
        values.put(CharacterEntry.COLUMN_NAME_THUMBNAIL_EXTENSION, character.getThumbnail().getExtension());

        String selection = CharacterEntry.COLUMN_NAME_CHARACTER_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(character.getId())};

        return db.update(CharacterEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public int delete(Character character) {
        SQLiteDatabase db = marvelApiDBHelper.getWritableDatabase();

        String selection = CharacterEntry.COLUMN_NAME_CHARACTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(character.getId())};

        return db.delete(CharacterEntry.TABLE_NAME, selection, selectionArgs);
    }
}
