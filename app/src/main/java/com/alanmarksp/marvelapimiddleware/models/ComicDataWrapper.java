package com.alanmarksp.marvelapimiddleware.models;


public class ComicDataWrapper extends DataWrapper<ComicDataContainer> {

    public ComicDataWrapper(int code, String status, String attributionText, String eTag, ComicDataContainer data) {
        super(code, status, attributionText, eTag, data);
    }
}
