package com.example.recipes;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.ItemKeyedDataSource;

public class RecipeDataSourceFactory  extends DataSource.Factory {
    private MutableLiveData<ItemKeyedDataSource<String, Recipe>> recipeLiveDataSource = new MutableLiveData<>();
    @Override
    public DataSource create() {
        RecipeDataSource recipeDataSource = new RecipeDataSource();
        recipeLiveDataSource.postValue(recipeDataSource);
        return recipeDataSource;
    }

    public MutableLiveData<ItemKeyedDataSource<String, Recipe>> getRecipeLiveDataSource() {
        return recipeLiveDataSource;
    }

}

