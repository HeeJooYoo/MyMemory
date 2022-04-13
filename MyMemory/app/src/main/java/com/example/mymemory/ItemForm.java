package com.example.mymemory;

import android.graphics.Bitmap;

public class ItemForm {
    private String album_title;
    private Bitmap album_img;
    private String album_id;

    public ItemForm(String album_id, Bitmap album_img, String album_title){ //new생성자를 통해서 생성자가 만들어진다.
        this.album_id = album_id;
        this.album_img = album_img;
        this.album_title = album_title;
    }
    public String getTitle(){//외부로 text값을 리턴해서 내보내준다.
        return album_title;
    }
    public Bitmap getImage(){//외부로 이미지 값을 리턴해서 보내준다.
        return album_img;
    }
    public String getID(){
        return album_id;
    }
    public void setTitle(String album_title){//외부에서 받은 text를 내부로 넣어준다.
        this.album_title = album_title;
    }
    public void setImage(Bitmap album_img){//외부에서 받은 imagenumber를 내부로 넣어준다.
        this.album_img = album_img;
    }
    public void setID(String album_id){
        this.album_id = album_id;
    }
}
