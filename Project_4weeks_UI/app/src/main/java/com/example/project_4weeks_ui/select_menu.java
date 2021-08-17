package com.example.project_4weeks_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class select_menu extends AppCompatActivity {
    MyApp myapp;

    // MainActivity로 부터 선택된 카테고리 이름 받아오기

    public TextView tv_category; // 카테고리 이름을 출력할 textView
    static boolean tagCheck = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Menu> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button tagBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myapp = ((MyApp)getApplicationContext());
        setContentView(R.layout.activity_select_menu);
        tagBt = findViewById(R.id.bt_tag);
        initTagBt();
        tv_category = findViewById(R.id.tv_category);
        tv_category.setText(myapp.getCategory_KR());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // recyclerView 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Menu 를 담을 arrayList


        database = FirebaseDatabase.getInstance(); // 파이어베이스 db 연동
        if (myapp.getTagCheck()==false){
            databaseReference = database.getReference(myapp.getCategory_ENG()); // 선택된 category db로 연결
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 파이어베이스 db에서 데이터를 받아오는 함수
                    arrayList.clear(); // arrayList 초기화
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // for문으로 List 추출
                        Menu menu = new Menu();
                        menu.set_name(snapshot.child("name").getValue().toString());
                        menu.set_img_URL(snapshot.child("img").getValue().toString());
                        menu.set_info1(snapshot.child("info").child("info1").getValue().toString());
                        menu.set_info2(snapshot.child("info").child("info2").getValue().toString());
                        menu.set_info3(snapshot.child("info").child("info3").getValue().toString());
                        // Menu object를 담음

                        arrayList.add(menu); // 받아온 menu object를 arrayList에 추가
                    }
                    adapter.notifyDataSetChanged(); // arrayList 저장 및 갱신
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"DB를 가져오는데 실패했습니다",Toast.LENGTH_SHORT).show();
                    // db가져오면서 error 발생시 Toast 출력
                }
            });
        }
        else{
            callWithTag();
        }

        adapter = new Menu_Adapter(arrayList,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter); // recyclerView에 adapter 연결
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return true;
    }

    private void initTagBt(){
        tagBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TagActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callWithTag(){
        Log.d("menulist",myapp.getCategory_ENG());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("getFirebaseDatabase", "key: " + snapshot.getChildrenCount());
                for(DataSnapshot snapshot1 : snapshot.getChildren()){ // for문으로 List 추출
                    Menu menu = new Menu();
                    if(TagActivity.time.equals("default")){
                        menu.set_name(snapshot1.child("name").getValue().toString());
                        menu.set_img_URL(snapshot1.child("img").getValue().toString());
                        menu.set_info1(snapshot1.child("info").child("info1").getValue().toString());
                        menu.set_info2(snapshot1.child("info").child("info2").getValue().toString());
                        menu.set_info3(snapshot1.child("info").child("info3").getValue().toString());
                        arrayList.add(menu);
                    }
                    else{
                        if(TagActivity.time.equals(snapshot1.child("info").child("info2").getValue().toString())){
                            menu.set_name(snapshot1.child("name").getValue().toString());
                            menu.set_img_URL(snapshot1.child("img").getValue().toString());
                            menu.set_info1(snapshot1.child("info").child("info1").getValue().toString());
                            menu.set_info2(snapshot1.child("info").child("info2").getValue().toString());
                            menu.set_info3(snapshot1.child("info").child("info3").getValue().toString());
                            arrayList.add(menu);
                        }
                    }

                    // Menu object를 담음

                    ; // 받아온 menu object를 arrayList에 추가
                    Log.d("menu",String.valueOf(arrayList.size()));
                }
                    adapter.notifyDataSetChanged(); // arrayList 저장 및 갱신
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("menu",error.toString());
            }

        };


        Log.d("menulog",TagActivity.mount+" "+ TagActivity.time);
        if (TagActivity.mount.equals("default")){
            Query query =database.getReference().child(myapp.getCategory_ENG()).orderByChild("info/info1");
            query.addListenerForSingleValueEvent(postListener);
        }
        else {
            Query query =database.getReference().child(myapp.getCategory_ENG()).orderByChild("info/info1").equalTo(TagActivity.mount);
            query.addListenerForSingleValueEvent(postListener);
        }
    }

}