package com.example.project_4weeks_ui;

public class Ingredient {
    String ingre_num;
    String ingre_count;
    String ingre_name;
    String ingre_unit;

    public Ingredient(String ingre_num, String ingre_name, String ingre_count, String ingre_unit) {
        this.ingre_num = ingre_num;
        this.ingre_count = ingre_count;
        this.ingre_name = ingre_name;
        this.ingre_unit = ingre_unit;
    }
    public void set_ingre_num(String ingre_num){
        this.ingre_num = ingre_num;
    }
    public void set_ingre_count(String ingre_count){
        this.ingre_count = ingre_count;
    }
    public void set_ingre_name(String ingre_name){
        this.ingre_name = ingre_name;
    }
    public void set_ingre_unit(String ingre_unit){
        this.ingre_unit = ingre_unit;
    }

    public String get_ingre_num(){
        return this.ingre_num;
    }
    public String get_ingre_count(){
        return this.ingre_count;
    }
    public String get_ingre_name(){
        return this.ingre_name;
    }
    public String get_ingre_unit(){
        return this.ingre_unit;
    }
}


