package com.alanmarksp.marvelapimiddleware.models;


import java.util.List;

public class ComicDataContainer extends DataContainer<Comic> {

    public ComicDataContainer(int offset, int limit, int total, int count, List<Comic> results) {
        super(offset, limit, total, count, results);
    }

}
