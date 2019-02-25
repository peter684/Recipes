package com.example.recipes;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RecipeDataSource extends ItemKeyedDataSource<String, Recipe> {
    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<Recipe> callback) {
        /*
        Loads all data from recipes; to be updated to load initiual subset (e.g. titles starting with a-e)
         */
        String initialKey= params.requestedInitialKey;
        int loadSize = params.requestedLoadSize;

        final ArrayList<Recipe> items = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && e == null){
                    for (ParseObject parseObject:list) {
                        final Recipe recipe = new Recipe(parseObject.getObjectId(), parseObject.getString("name"), parseObject.getString("desciption"),parseObject.getParseFile("bitmap"));
                        items.add(recipe);
                    }
                    callback.onResult(items);
                }
                else{
                    Log.i("Parse msg","Error: "+e.toString());
                }
            }

        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull final LoadCallback<Recipe> callback) {
        /*
        Loads all data from recipes; to be updated to load initiual subset (e.g. titles starting with a-e)
         */
        String initialTitle = params.key;
        int loadSize = params.requestedLoadSize;

        final ArrayList<Recipe> items = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && e == null){
                    for (ParseObject parseObject:list) {
                        final Recipe recipe = new Recipe(parseObject.getObjectId(), parseObject.getString("name"), parseObject.getString("desciption"),parseObject.getParseFile("bitmap"));
                        items.add(recipe);
                    }
                    callback.onResult(items);
                }
                else{
                    Log.i("Parse msg","Error: "+e.toString());
                }
            }

        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull final LoadCallback<Recipe> callback) {
/*
        Loads all data from recipes; to be updated to load initiual subset (e.g. titles starting with a-e)
         */
        String initialTitle = params.key;
        int loadSize = params.requestedLoadSize;

        final ArrayList<Recipe> items = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && e == null){
                    for (ParseObject parseObject:list) {
                        final Recipe recipe = new Recipe(parseObject.getObjectId(), parseObject.getString("name"), parseObject.getString("desciption"),parseObject.getParseFile("bitmap"));
                        items.add(recipe);
                    }
                    callback.onResult(items);
                }
                else{
                    Log.i("Parse msg","Error: "+e.toString());
                }
            }

        });
    }

    @NonNull
    @Override
    public String getKey(@NonNull Recipe item) {
        return item.getRecipeTitle();
    }
}
