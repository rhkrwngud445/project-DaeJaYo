package com.example.project_4weeks_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    private Retrofit mRetrofit;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    public static String baseURL = "http://211.237.50.150:7080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipe);
        textView1 = findViewById(R.id.textView1);
        textView1.setMovementMethod(new ScrollingMovementMethod());
        textView2 = findViewById(R.id.textView2);
        textView2.setMovementMethod(new ScrollingMovementMethod());
        textView3 = findViewById(R.id.textView3);
        textView3.setMovementMethod(new ScrollingMovementMethod());
        textView4 = findViewById(R.id.textView4);
        textView4.setMovementMethod(new ScrollingMovementMethod());
        textView5 = findViewById(R.id.textView5);
        textView5.setMovementMethod(new ScrollingMovementMethod());
        textView6 = findViewById(R.id.textView6);
        textView6.setMovementMethod(new ScrollingMovementMethod());
        textView7 = findViewById(R.id.textView7);
        textView7.setMovementMethod(new ScrollingMovementMethod());
        textView8 = findViewById(R.id.textView8);
        textView8.setMovementMethod(new ScrollingMovementMethod());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeInterface recipeInterface = mRetrofit.create(RecipeInterface.class);

        Call<DataClass> call = recipeInterface.getRecipe("568996252afd631611d12bb9f54eb5a5d431445805a6fccabe8c16b3df67495f",
                "json", "Grid_20150827000000000226_1", 1, 5);
        call.enqueue(new Callback<DataClass>() {
            @Override
            public void onResponse(Call<DataClass> call, Response<DataClass> response) {
                DataClass result = response.body();
                Log.d("mainAcitvity", String.valueOf(response.body().grid.row.get(0).recipeId));
            }

            @Override
            public void onFailure(Call<DataClass> call, Throwable t) {
                Log.d("mainAcitvity",t.getMessage());
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
    @SerializedName("Grid_20150827000000000226_1")
    Grid grid;

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
}
class Grid {
    @SerializedName("row")
    List<row> row;


}
class row{
    @SerializedName("RECIPE_ID")
     String recipeId = null;

    @SerializedName("RECIPE_NM_KO")
    private String recope_nm_ko = null;

    @SerializedName("SUMRY")
    private String sumry  = null  ;

    @SerializedName("NATION_NM")
    private String nation_nm = null  ;

    @SerializedName("TY_CODE")
    private String ty_code = null  ;

    @SerializedName("TY_NM")
    private String ty_nm = null  ;

    @SerializedName("COOKING_TIME")
    private String cooking_time = null  ;

    @SerializedName("CALORIE")
    private String calorie ;
}


