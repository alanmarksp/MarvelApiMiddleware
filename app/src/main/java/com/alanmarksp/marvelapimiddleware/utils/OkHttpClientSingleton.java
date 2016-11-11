package com.alanmarksp.marvelapimiddleware.utils;


import android.content.Context;

import com.alanmarksp.marvelapimiddleware.BuildConfig;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientSingleton {

    private static final String TIMESTAMP_KEY = "ts";
    private static final String HASH_KEY = "hash";
    private static final String APIKEY_KEY = "apikey";

    private static OkHttpClient okHttpClient;

    private OkHttpClientSingleton() {
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    String timeStamp = String.valueOf(System.currentTimeMillis());

                    String hash = md5(timeStamp + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY);

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter(TIMESTAMP_KEY, timeStamp)
                            .addQueryParameter(HASH_KEY, hash)
                            .addQueryParameter(APIKEY_KEY, BuildConfig.PUBLIC_API_KEY)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            });

            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

    private static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
