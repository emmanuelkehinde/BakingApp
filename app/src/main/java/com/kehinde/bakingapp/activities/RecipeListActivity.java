package com.kehinde.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kehinde.bakingapp.IdlingResource.SimpleIdlingResource;
import com.kehinde.bakingapp.R;
import com.kehinde.bakingapp.adapters.RecipeListAdapter;
import com.kehinde.bakingapp.models.Ingredient;
import com.kehinde.bakingapp.models.Recipe;
import com.kehinde.bakingapp.util.ApiService;
import com.kehinde.bakingapp.util.Constants;
import com.kehinde.bakingapp.util.ServiceUtil;
import com.kehinde.bakingapp.widget.IngredientWidgetProvider;
import com.kehinde.bakingapp.widget.db.IngredientContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordLayout) CoordinatorLayout coordLayout;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.recipe_list_recycler) RecyclerView recipe_list_recycler;
    private ArrayList<Recipe> recipeArrayList=new ArrayList<>();
    Call<ArrayList<Recipe>> recipeCall;
    private final String KEY="KEY";
    private SharedPreferences sharedPreferences;


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipes");


        if (savedInstanceState!=null){
            recipeArrayList=savedInstanceState.getParcelableArrayList(KEY);
            prepareList(recipeArrayList);
        }

        sharedPreferences=this.getSharedPreferences("com.kehinde.bakingapp", Context.MODE_PRIVATE);

        // Get the IdlingResource instance
        getIdlingResource();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (recipeArrayList != null && recipeArrayList.isEmpty()) {
            makeRequest();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(KEY,recipeArrayList);
    }

    public void makeRequest(){

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        if (ServiceUtil.isOnline(this)) {
            getRecipes();

        }else{
            Snackbar snackbar = Snackbar.make(coordLayout, "Unable to retrieve recipes; check your Internet connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeRequest();
                }
            });
            snackbar.show();
        }
    }

    private void getRecipes() {
        progress.setVisibility(View.VISIBLE);

        ApiService apiService=getRetrofitInstance().create(ApiService.class);

        recipeCall=apiService.getRecipes();
        recipeCall.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipeArrayList=response.body();
                prepareList(recipeArrayList);
                progress.setVisibility(View.GONE);

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                progress.setVisibility(View.GONE);
//                Log.e("error",t.getLocalizedMessage());
                Snackbar snackbar = Snackbar.make(coordLayout, "Error, please try again", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRecipes();
                    }
                });
                snackbar.show();
            }
        });
    }

    private void prepareList(ArrayList<Recipe> recipeArrayList) {
        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(recipeArrayList, this);
        recipe_list_recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recipe_list_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recipe_list_recycler.setAdapter(recipeListAdapter);
    }

    public Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onRecipeSelected(int position) {

        Intent intent=new Intent(this,RecipeStepListActivity.class);
        intent.putExtra(Constants.RECIPE_EXTRA,recipeArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void onOptionMenuIconClick(TextView opt_recipe, final int adapterPosition) {
        final PopupMenu popupMenu=new PopupMenu(this,opt_recipe);
        popupMenu.inflate(R.menu.widget);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_recipe: displayIngredientInWidget(adapterPosition); break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void displayIngredientInWidget(int adapterPosition) {
        ArrayList<Ingredient> ingredients;
        ingredients=recipeArrayList.get(adapterPosition).getIngredients();
        String recipe=recipeArrayList.get(adapterPosition).getName();
        sharedPreferences.edit().putString(Constants.S_PREF_RECIPE,recipe).apply();


        Uri uri1 = IngredientContract.CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(uri1,null,null,null,null);


        if (cursor!=null) {

            //Delete existing data first
            while (cursor.moveToNext()) {
                Uri uri2 = IngredientContract.CONTENT_URI;
                this.getContentResolver().delete(uri2,
                        IngredientContract.Columns._ID + "=?",
                        new String[]{cursor.getString(0)});

            }

            //Insert
            ContentValues values = new ContentValues();

            for (Ingredient ingredient : ingredients) {
                values.clear();
                values.put(IngredientContract.Columns.QUANTITY, ingredient.getQuantity());
                values.put(IngredientContract.Columns.MEASURE, ingredient.getMeasure());
                values.put(IngredientContract.Columns.INGREDIENT, ingredient.getIngredient());


                Uri uri = IngredientContract.CONTENT_URI;
                getApplicationContext().getContentResolver().insert(uri, values);
            }
        }

        //Update the name of the recipe
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientWidgetProvider.class));
        IngredientWidgetProvider ingredientWidget=new IngredientWidgetProvider();
        ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);


        //Update the ListView of ingredients
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, IngredientWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ing_widget_list);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (recipeCall!=null) {
            recipeCall.cancel();
        }
        finish();
    }
}
