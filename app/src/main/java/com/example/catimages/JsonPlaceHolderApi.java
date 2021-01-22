package com.example.catimages;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("v1/images/search")
    Call<List<Cats>> getCats(@Query("limit") int limit);
}
