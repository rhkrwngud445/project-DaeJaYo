package com.example.project_4weeks_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TagActivity extends AppCompatActivity {
    MyApp myapp;
    RadioGroup radioGroupMount;
    RadioGroup radioGroupTime;
    Button button;
    static String   mount= "";
    static String time = "";
    static String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myapp = ((MyApp)getApplicationContext());
        setContentView(R.layout.activity_tag);
        radioGroupMount = findViewById(R.id.rbg_tag_mount);
        radioGroupTime = findViewById(R.id.rbg_tag_time);
        radioGroupMount.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroupTime.setOnCheckedChangeListener(radioGroupButtonChangeListener1);
        button = findViewById(R.id.bt_setTag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),select_menu.class);
                myapp.setTagCheck(true);
                startActivity(intent);
            }
        });


    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rb_tag_1mount) {
                mount="1인분";
            } else if (i == R.id.rb_tag_2mount) {
                mount="2인분";
            }
            else if ( i==R.id.rb_tag_3mount){
                mount="3인분";
            }
            else if( i==R.id.rb_tag_above3mount){
                mount="default";
            }
        }
    };
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rb_tag_15min) {
                time="15분 이내";
            } else if (i == R.id.rb_tag_30min) {
                time="30분 이내";
            }
            else if ( i == R.id.rb_tag_60min){
                time="60분 이내";
            }
            else if ( i== R.id.rb_tag_defaultMin){
                time ="default";
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
