package com.example.recipes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity
        implements  RecipeListAdapter.ViewHolder.ClickHandler{

    static RecipeListAdapter recipesAdapter;
    static final int PARSE_MAX_UPLOAD_SIZE = 10485760;
    final ArrayList<Recipe> recipes = new ArrayList<>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()== R.id.share) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //ask permission for media access
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
            else{
                getPhoto();
            }
        }
        else if (item.getItemId() == R.id.logout){
            ParseUser.logOut();
            startActivity(new Intent(getApplicationContext(), LogonSignIn.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPhoto(){
        if (recipesAdapter.getSelectedItems()==null || recipesAdapter.getSelectedItems().size() == 0)  {
            Toast.makeText(this, "Please select a recipe first", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check if a recipe is selected , otherwise exit method
        if(requestCode==1 && resultCode==RESULT_OK && data!= null){
            Uri selectedImage = data.getData();
            try {
                Bitmap source_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                final Bitmap bitmap = Bitmap.createScaledBitmap(source_bitmap, 120,120,false);

                if (bitmap.getByteCount()> PARSE_MAX_UPLOAD_SIZE){
                    Toast.makeText(this, "File size too large, please select smaller size", Toast.LENGTH_SHORT).show();
                    return;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, outputStream);
                byte[] byteArray = outputStream.toByteArray();
                final ParseFile file = new ParseFile("image.png",byteArray);
                final Recipe recipe = recipesAdapter.getData().get(recipesAdapter.getSelectedItems().get(0));

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
                query.whereEqualTo("name",recipe.getRecipeTitle());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list != null && e == null){
                            for (ParseObject parseObject:list) {
                                parseObject.put("bitmap",file);
                                parseObject.saveInBackground();
                                recipe.setRecipeBitmap(bitmap);
                            }
                            recipesAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.i("Parse msg","Error: "+e.toString())   ;
                        }
                    }
                });
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1 && grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            getPhoto();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle("Recipes");
        //load recipe data. Bitmap to be loaded when view is bound in onBIndViewHolder
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && e == null){
                    for (ParseObject parseObject:list) {
                        final Recipe recipe = new Recipe(parseObject.getObjectId(), parseObject.getString("name"), parseObject.getString("desciption"),parseObject.getParseFile("bitmap"));
                        recipes.add(recipe);
                    }
                }
                else{
                    Log.i("Parse msg","Error: "+e.toString());
                }
                recipesAdapter.notifyDataSetChanged();
            }

        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesAdapter = new RecipeListAdapter(recipes,this);
        recyclerView.setAdapter(recipesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onItemClicked(int position){
        //Toast.makeText(this, "you selected "+recipes.get(position).getRecipeTitle(), Toast.LENGTH_SHORT).show();
        //recipesAdapter.selectedPosition = position;
        recipesAdapter.toggleSelection(position);

    }

    public boolean onItemLongClicked(int position){
        //Toast.makeText(this, "you long-clicked "+recipes.get(position).getRecipeTitle(), Toast.LENGTH_SHORT).show();
        //recipesAdapter.selectedPosition = position;
        //return true to indicate event handling is now done, no additional handling by onclick needed
        return true;
    }

}
