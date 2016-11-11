package com.alanmarksp.marvelapimiddleware.services;


import com.alanmarksp.marvelapimiddleware.models.CharacterDataWrapper;
import com.alanmarksp.marvelapimiddleware.models.ComicDataWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApiService {

    @GET("/v1/public/characters?limit=100")
    Call<CharacterDataWrapper> listCharacters();

    @GET("/v1/public/characters?limit=100")
    Call<CharacterDataWrapper> listCharacters(@Header("If-None-Match") String eTag);

    @GET("/v1/public/characters?limit=100")
    Call<CharacterDataWrapper> listCharacters(@Query("offset") Integer offset);

    @GET("/v1/public/characters?limit=100")
    Call<CharacterDataWrapper> listCharacters(@Query("offset") Integer offset, @Header("If-None-Match") String eTag);

    @GET("/v1/public/characters/{characterId}")
    Call<CharacterDataWrapper> retrieveCharacters(@Path("characterId") Integer characterId);

    @GET("/v1/public/characters/{characterId}")
    Call<CharacterDataWrapper> retrieveCharacters(@Path("characterId") Integer characterId, @Header("If-None-Match") String eTag);

    @GET("/v1/public/characters/{characterId}/comics")
    Call<ComicDataWrapper> listComicsFromCharacter(@Path("characterId") Integer characterId);

    @GET("/v1/public/characters/{characterId}/comics")
    Call<ComicDataWrapper> listComicsFromCharacter(@Path("characterId") Integer characterId, @Header("If-None-Match") String eTag);

}
