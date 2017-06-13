package com.kehinde.bakingapp.util;

import com.kehinde.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kehinde on 6/3/17.
 */

public interface ApiService {

    @GET("android-baking-app-json")
    Call<ArrayList<Recipe>> getRecipes();
}
