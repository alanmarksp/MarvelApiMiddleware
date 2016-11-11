package com.alanmarksp.marvelapimiddleware.loaders;


import android.content.Context;
import android.database.Cursor;

import com.alanmarksp.marvelapimiddleware.dao.CharacterDao;
import com.alanmarksp.marvelapimiddleware.dao.HttpCacheDao;
import com.alanmarksp.marvelapimiddleware.models.Character;
import com.alanmarksp.marvelapimiddleware.models.CharacterDataWrapper;
import com.alanmarksp.marvelapimiddleware.models.HttpCache;
import com.alanmarksp.marvelapimiddleware.services.MarvelApiService;
import com.alanmarksp.marvelapimiddleware.utils.OkHttpClientSingleton;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterLoader {
    private static final String BASE_URL = "https://gateway.marvel.com";

    private static final int DB_PAGE_SIZE = 10;
    private static final int API_PAGE_SIZE = 100;

    private MarvelApiService service;

    private CharacterDao characterDao;
    private HttpCacheDao httpCacheDao;

    public CharacterLoader(Context context) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClientSingleton.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MarvelApiService.class);

        characterDao = new CharacterDao(context);
        httpCacheDao = new HttpCacheDao(context);
    }

    public List<Character> loadPage(int offset) throws IOException {
        List<Character> characters = characterDao.list(offset);

        if (characters.size() == DB_PAGE_SIZE) {
            return characters;
        }

        List<Character> apiCharacters = loadPageFromApi(offset);

        if (apiCharacters != null) {
            return apiCharacters;
        }

        return characters;
    }

    private List<Character> loadPageFromApi(int offset) throws IOException {
        int apiOffset = offset * DB_PAGE_SIZE / API_PAGE_SIZE * API_PAGE_SIZE;

        Call<CharacterDataWrapper> call = service.listCharacters(apiOffset);

        HttpCache httpCache = httpCacheDao.retrieve(getUrl(call.request()), apiOffset);

        if (httpCache != null) {
            if (getDateDiff(httpCache.getDate(), new Date(), TimeUnit.DAYS) < 1) {
                return null;
            }

            call = service.listCharacters(apiOffset, httpCache.getETag());
        }

        Response<CharacterDataWrapper> response = call.execute();

        if (response.code() == 200) {
            String url = getUrl(call.request());
            String eTag = response.body().getETag();
            Date date = new Date();

            if (httpCache == null) {
                httpCache = new HttpCache(url, eTag, date, apiOffset);
                httpCacheDao.insert(httpCache);
            } else {
                if (!httpCache.getETag().equals(eTag)) {
                    httpCacheDao.invalidate(httpCache.getUrl());
                }
                httpCache.setETag(eTag);
                httpCache.setDate(date);
                httpCacheDao.insert(httpCache);
            }

            List<Character> results = response.body().getData().getResults();
            if (results.size() > 0) {
                storeCharacters(results);
                return characterDao.list(offset);
            }
        }

        return null;
    }

    private void storeCharacters(List<Character> characters) {
        for (Character character : characters) {
            Character oldCharacter = characterDao.retrieve(character.getId());

            if (oldCharacter != null) {
                character.setRate(oldCharacter.getRate());
                characterDao.update(character);
            } else {
                characterDao.insert(character);
            }
        }
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diff = date2.getTime() - date1.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    private String getUrl(Request request) {
        HttpUrl httpUrl = request.url();
        return httpUrl.host() + httpUrl.encodedPath();
    }
}
