package com.example.mymemory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoActivity extends AppCompatActivity {
    ImageView p_img;
    TextView p_txt;

    String photo_id;
    Bitmap bitmap;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        p_img = findViewById(R.id.img_pt);
        p_txt = findViewById(R.id.text_pt);

        photo_id = getIntent().getStringExtra("photo_id");

        Log.d("TEST" , " : " + photo_id);
        final Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    response = response.trim();
                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean success = jsonResponse.getBoolean("success");
                    String photo_img = jsonResponse.getString("photo_img");
                    String photo_txt = jsonResponse.getString("photo_txt");

                    if(success) {
                        p_txt.setText(photo_txt);
                        imgoutput(photo_img);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoActivity.this, R.style.AppCompatAlertDialogStyle);
                        dialog = builder.setMessage("회원정보 실패").setNegativeButton("확인",null).create();
                        dialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PhotoInfoRequest photoInfoRequest = new PhotoInfoRequest(photo_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(PhotoActivity.this);
        queue.add(photoInfoRequest);
    }

    private class ImgOutPut extends AsyncTask<String, String, Bitmap> {
        Context context;
        HttpURLConnection conn = null;
        Bitmap bit;

        public ImgOutPut(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.disconnect();
                InputStream is = conn.getInputStream();
                bit = BitmapFactory.decodeStream(is);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            p_img.setImageBitmap(bit);
        }
    }

    public void imgoutput(String imgName) {
        String urlString = "http://10.0.2.2/MyMemory/newImage"+imgName;
        try {
            ImgOutPut imgoutput = new ImgOutPut(PhotoActivity.this);
            bitmap = imgoutput.execute(urlString).get();
        } catch (Exception e) {
            Log.d("Test","out");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
