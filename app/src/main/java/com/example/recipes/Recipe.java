package com.example.recipes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class Recipe {

    public interface DownloadCompleteListener{
        public void onDataLoaded(String recipeId);
    }

    private DownloadCompleteListener downloadCompleteListener;


    public void setDownloadCompleteListener(DownloadCompleteListener listener){
        downloadCompleteListener = listener;
    }

    private String id;
    private String title;
    private String body;
    private Bitmap bitmap = null;

    public Recipe(String id){
        this.id =id;
    }

    public Recipe(String id, String title, String body){
        this.id = id;
        this.title= title;
        this.body=body;
    }

    public Recipe(String id, String title, String body, ParseFile file) {
        this.id = id;
        this.title = title;
        this.body = body;
        if (file != null) {
           this.bitmap = getRecipeBitmap();
        }
    }

    public void setRecipeTitle(String title){
        this.title=title;
    }

    public void setRecipeBody(String body){
        this.body=body;
    }

    public void setRecipeBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    public String getRecipeTitle(){
        return title;
    }

    public String getRecipeBody(){
        return body;
    }


    public Bitmap getRecipeBitmap(){
        if (bitmap != null){
            return bitmap;
        }
        else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
            query.getInBackground(id, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                if (object != null && e == null) {
                    try {
                        ParseFile file = object.getParseFile("bitmap");
                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (data != null && data.length > 0) {
                                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        Log.i("Parse", "File read from db: " + title);
                                        if (downloadCompleteListener != null){
                                            downloadCompleteListener.onDataLoaded(id);
                                        }

                                    }
                                }
                            });

                        }

                    } catch (Exception parseException) {
                        Log.i("Parse msg", "Error reading bitmap: " + e.getMessage());
                    }

                }
                }
            });
            return bitmap;
        }
    }

    public String getId(){
        return id;
    }



}
