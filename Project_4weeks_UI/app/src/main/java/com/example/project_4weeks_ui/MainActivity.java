package com.example.project_4weeks_ui;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SELECT_MENU = 101;
    private Retrofit mRetrofit;
    public static String baseURL = "http://211.237.50.150:7080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), select_menu.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_MENU);
            }
        });






        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeInterface recipeInterface = mRetrofit.create(RecipeInterface.class);

        Call<DataClass> call = recipeInterface.getRecipe("568996252afd631611d12bb9f54eb5a5d431445805a6fccabe8c16b3df67495f",
                "json", "Grid_20150827000000000226_1", 1, 2);
        call.enqueue(new Callback<DataClass>() {
            @Override
            public void onResponse(Call<DataClass> call, Response<DataClass> response) {
                DataClass result = response.body();
                Log.d("mainAcitvity", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<DataClass> call, Throwable t) {
                Log.d("mainAcitvity","failure");
            }
        });
    }
}

interface RecipeInterface {
    @GET("openapi/{API_KEY}/{TYPE}/{API_URL}/{START_INDEX}/{END_INDEX}")
    Call<DataClass> getRecipe(@Path("API_KEY") String api_key,
                              @Path("TYPE") String type,
                              @Path("API_URL") String api_url,
                              @Path("START_INDEX") int start_index,
                              @Path("END_INDEX") int end_index
                              )
                              ;
}

class DataClass {

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    @SerializedName("RECIPE_ID")
    public String recipeId;

    @SerializedName("body")
    public String body;


    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
    @Override
    public String toString() {
        return "PostResult{" +
                "name=" + recipeId +
                ", nickname=" + body + '\'' +
                '}';
    }
}