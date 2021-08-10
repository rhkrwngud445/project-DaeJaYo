package com.example.project_4weeks_ui;

public class add_recipe {
    String num;
    String img_storage;
    String txt;

    public add_recipe(String num, String img_storage, String txt) {
        this.img_storage = img_storage;
        this.txt = txt;
        this.num = num;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getImg_storage() {
        return this.img_storage;
    }

    public void setImg_storage(String img_storage) {
        this.img_storage = img_storage;
    }


    public String getTxt() {
        return this.txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
