package com.alanmarksp.marvelapimiddleware.models;


import java.util.Date;

public class HttpCache {

    private String url;
    private String eTag;
    private Date date;
    private int offset;

    public HttpCache(String url, String eTag, Date date, int offset) {
        this.url = url;
        this.eTag = eTag;
        this.date = date;
        this.offset = offset;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
