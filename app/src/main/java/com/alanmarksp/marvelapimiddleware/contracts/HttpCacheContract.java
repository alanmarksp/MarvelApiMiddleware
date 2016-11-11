package com.alanmarksp.marvelapimiddleware.contracts;


import android.provider.BaseColumns;

public final class HttpCacheContract {

    private HttpCacheContract() {
    }

    public static class HttpCacheEntry implements BaseColumns {
        public static final String TABLE_NAME = "HttpCache";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_ETAG = "eTag";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_OFFSET = "offset";
    }
}
