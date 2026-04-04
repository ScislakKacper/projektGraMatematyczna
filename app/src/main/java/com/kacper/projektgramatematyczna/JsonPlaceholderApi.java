package com.kacper.projektgramatematyczna;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderApi {
    @GET("db.json")
    public Call<List<Pytanie>> getPytania();
}
