package com.example.project_4weeks_ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class add_menu extends AppCompatActivity {
    private final int GALLERY_CODE_MainImage = 1112;
    private final int GALLERY_CODE_AddRecipe_Image = 1113;
    ArrayList<Ingredient> ingredient_array; // 추가될 메뉴의 재료정보 list
    ArrayList<add_recipe> recipe_array;
    add_Ingre_Adapter ingre_adapter; // 재료정보 리사이클러뷰 어댑터
    Add_recipe_Adapter recipe_adapter;
    String storage;

    // 이미지 추가
    ImageView iv_MenuImage ;
    ImageView iv_addRecipeImg;
    TextView test;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            // 대표 이미지를 선택하는 경우
            if (requestCode == GALLERY_CODE_MainImage) {
                try {
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        bitmap = rotate(bitmap);
                    }
                    iv_MenuImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 레시피 사진을 선택하는 경우
            else if(requestCode == GALLERY_CODE_AddRecipe_Image){
                Toast.makeText(getApplicationContext(),"123", Toast.LENGTH_SHORT).show();
                try {
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        bitmap = rotate(bitmap);
                    }
                    storage = getFilePath(uri);
                    iv_addRecipeImg.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(storage)));
                    test.setText("1234");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    // 사진 회전 방지
    private Bitmap rotate(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0,0, width, height, matrix, true);
        return resizedBitmap;
    }

    // 갤러리에서 선택한 사진 절대경로 구하기
    private String getFilePath(Uri uri){
        String path;
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        path = cursor.getString(index);
        cursor.close();
        return path;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        iv_MenuImage = findViewById(R.id.iv_MenuImg);
        iv_addRecipeImg = findViewById(R.id.iv_addRecipeImg);
        test = findViewById(R.id.test);
        // 대표 이미지 추가 버튼
        Button btn_add_MenuImg = (Button)findViewById(R.id.btn_add_MenuImg);
        btn_add_MenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_CODE_MainImage);

            }
        });


        // 재료 추가 리사이클러뷰 관련 코드
        RecyclerView rec_addIngre = (RecyclerView)findViewById(R.id.rec_addIngre);
        LinearLayoutManager addIngre_layoutManager = new LinearLayoutManager(this);
        rec_addIngre.setLayoutManager(addIngre_layoutManager);
        ingredient_array = new ArrayList<>();
        ingre_adapter = new add_Ingre_Adapter(ingredient_array);
        rec_addIngre.setAdapter(ingre_adapter);
        // 재료 추가 리사이클러뷰 관련 코드 end

        // 재료 추가 버튼
        Button btn_addIngre = (Button)findViewById(R.id.btn_addIngre);
        btn_addIngre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addIngre_dialog = new AlertDialog.Builder(add_menu.this);
                View view = LayoutInflater.from(add_menu.this).inflate(R.layout.add_ingre_dialog, null, false);
                addIngre_dialog.setView(view); // 출력할 dialog에 custon dialog 적용

                Button btn_submit = (Button)view.findViewById(R.id.btn_addIngre_submit); // 추가 버튼
                EditText inputName = (EditText)view.findViewById(R.id.etv_addIngre_dialog_name);
                EditText inputCount = (EditText)view.findViewById(R.id.etv_addIngre_dialog_count);
                EditText inputUnit = (EditText)view.findViewById(R.id.etv_addIngre_dialog_unit);

                AlertDialog dialog = addIngre_dialog.create();
                dialog.show(); // dialog show

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = inputName.getText().toString();
                        String count = inputCount.getText().toString();
                        String unit = inputUnit.getText().toString();
                        Ingredient new_ingredient = new Ingredient(Integer.toString(ingredient_array.size()+1), name, count, unit);
                        ingredient_array.add(new_ingredient);
                        ingre_adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

            }
        });

        // 재료 삭제 버튼
        Button btn_deleteIngre = (Button)findViewById(R.id.btn_deleteIngre);
        btn_deleteIngre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteIngre_dialog = new AlertDialog.Builder(add_menu.this);
                View view = LayoutInflater.from(add_menu.this).inflate(R.layout.delete_ingre_dialog, null, false);
                deleteIngre_dialog.setView(view); // 출력할 dialog에 custon dialog 적용

                Button btn_submit = (Button)view.findViewById(R.id.btn_addIngre_submit); // 삭제 버튼
                EditText delete_num = (EditText)view.findViewById(R.id.etv_addIngre_dialog_name);
                AlertDialog dialog = deleteIngre_dialog.create();
                dialog.show();

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(delete_num.getText())){
                            int num = Integer.parseInt(delete_num.getText().toString());
                            if(num <= 0 || num > ingredient_array.size()){
                                Toast.makeText(getApplicationContext(), "잘못된 숫자를 입력하셨습니다.\n 다시 입력해주세요",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                ingredient_array.remove(num -1);
                                ingre_adapter.notifyItemRemoved(num-1);
                                for(int i =0  ; i < ingredient_array.size(); i++){
                                    int cur_num = Integer.parseInt(ingredient_array.get(i).get_ingre_num());
                                    if(cur_num > num){
                                        ingredient_array.get(i).set_ingre_num(Integer.toString(cur_num-1));
                                    }
                                }
                                ingre_adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        // 레시피 추가 리사이클러뷰 관련 코드
        RecyclerView rec_addRecipe = (RecyclerView)findViewById(R.id.rec_addRecipe);
        LinearLayoutManager addRecipe_layoutManager = new LinearLayoutManager(this);
        rec_addRecipe.setLayoutManager(addRecipe_layoutManager);
        recipe_array = new ArrayList<>();
        recipe_adapter = new Add_recipe_Adapter(recipe_array);
        rec_addRecipe.setAdapter(recipe_adapter);
        // 레시피 추가 리사이클러뷰 관련 코드 end


        // 레시피 추가 버튼
        Button btn_addRecipe = (Button)findViewById(R.id.btn_addRecipe);
        btn_addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addRecipe_dialog = new AlertDialog.Builder(add_menu.this);
                View view = LayoutInflater.from(add_menu.this).inflate(R.layout.add_recipe_dialog,null,false);
                addRecipe_dialog.setView(view);

                Button btn_submit = (Button)view.findViewById(R.id.btn_addRecipe_submit); // 추가 버튼
                EditText inputTxT = (EditText)view.findViewById(R.id.etv_addRecipe_dialog_txt);
                AlertDialog dialog = addRecipe_dialog.create();
                dialog.show();

                // 레시피 사진 추가 버튼
                Button btn_addRecipe_Img = (Button)view.findViewById(R.id.btn_addRecipe_Img);
                btn_addRecipe_Img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,""), GALLERY_CODE_AddRecipe_Image);
                    }
                });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(storage == null){
                            Toast.makeText(getApplicationContext(),"이미지를 선택해주세요 !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(!TextUtils.isEmpty(inputTxT.getText())) {
                                Toast.makeText(getApplicationContext(),"조리과정을 작성해주세요 !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String txt = inputTxT.getText().toString();
                                add_recipe new_recipe = new add_recipe(Integer.toString(recipe_array.size() + 1), storage, txt);
                                recipe_array.add(new_recipe);
                                recipe_adapter.notifyDataSetChanged();
                                storage = null;
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });



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
}