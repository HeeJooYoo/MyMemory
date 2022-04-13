package com.example.mymemory;

import android.graphics.Bitmap;

public class PhotoForm {
    private String photo_id;
    private Bitmap photo_img;
    private String photo_txt;

    public PhotoForm(String photo_id, Bitmap photo_img, String photo_txt){ //new생성자를 통해서 생성자가 만들어진다.
        this.photo_id = photo_id;
        this.photo_img = photo_img;
        this.photo_txt = photo_txt;
    }
    public String getText(){//외부로 text값을 리턴해서 내보내준다.
        return photo_txt;
    }
    public Bitmap getImage(){//외부로 이미지 값을 리턴해서 보내준다.
        return photo_img;
    }
    public String getID(){
        return photo_id;
    }
    public void setText(String photo_txt){//외부에서 받은 text를 내부로 넣어준다.
        this.photo_txt = photo_txt;
    }
    public void setImage(Bitmap photo_img){//외부에서 받은 imagenumber를 내부로 넣어준다.
        this.photo_img = photo_img;
    }
    public void setID(String photo_id){
        this.photo_id = photo_id;
    }
}

