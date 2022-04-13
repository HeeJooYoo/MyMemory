package com.example.mymemory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//앨범의 사진 목록
public class AlbumItemActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private final int REQUEST_PERMISSION_CODE = 2222; //권한동의를 위한 변수
    int num;

    public static String albumID;
    String photoID;

    private View header;

    private RecyclerView rv2;
    private RecyclerView.LayoutManager glm;
    private ArrayList<PhotoForm> imgList;
    private ImgAdapter imgAdapter;
    PhotoForm rItem;

    Bitmap btmImg;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        header = getLayoutInflater().inflate(R.layout.album_item_img, null, false);
        img = header.findViewById(R.id.album_img);

        albumID = getIntent().getStringExtra("album_id");

        rv2 = findViewById(R.id.rv2);
        glm = new GridLayoutManager(this, 3);

        rv2.setLayoutManager(glm);
        rv2.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_add: {
                int permissionCheck = ContextCompat.checkSelfPermission(AlbumItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Response.Listener responseListener1 = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                response = response.trim();
                                JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                boolean success = jsonResponse.getBoolean("success");
                                int count = jsonResponse.getInt("count");

                                if (success) {
                                    count = count + 1;
                                    String cnt = Integer.toString(count);
                                    photoID = albumID +"_"+ cnt; //사진 id = 앨범id_숫자
                                    Intent addIntent = new Intent(AlbumItemActivity.this, PhotoAddActivity.class);
                                    addIntent.putExtra("photo_id", photoID);
                                    startActivity(addIntent);
                                } else {
                                    Log.d("test1", "카운트 실패");
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PhotoIDRequest photoIDRequest = new PhotoIDRequest(albumID, responseListener1);
                    RequestQueue queue1 = Volley.newRequestQueue(AlbumItemActivity.this);
                    queue1.add(photoIDRequest);
                } else {
                    requestPermission();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Snackbar.make(AlbumItemActivity.this, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Toast.makeText(AlbumItemActivity.this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AlbumItemActivity.this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent addIntent = new Intent(AlbumItemActivity.this, PhotoAddActivity.class);
                    startActivity(addIntent);
                } else {
                    Toast toast = Toast.makeText(this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onList();
    }

    public void onList() {
        imgList = new ArrayList<>();
        final Response.Listener responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = response.trim();
                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    JSONArray photoData =  jsonResponse.getJSONArray("photoData");

                    String photo_id, photo_img, photo_txt;
                    for (int i = 0; i < photoData.length(); i++) {
                        JSONObject object = photoData.getJSONObject(i);
                        photo_id = object.getString("photo_id");
                        photo_img = object.getString("photo_img");
                        photo_txt = object.getString("photo_txt");

                        imgoutput(photo_img);

                        rItem = new PhotoForm(photo_id, btmImg, photo_txt);
                        imgList.add(rItem);
                        Log.d("TEST : ", albumID + photo_id + photo_img + photo_txt);
                    }
                    imgAdapter = new ImgAdapter(AlbumItemActivity.this, imgList);
                    rv2.setAdapter(imgAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        PhotoListRequest photoListRequest = new PhotoListRequest(albumID, responseListener1);
        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        queue1.add(photoListRequest);

    }

    private class ImgOutPut extends AsyncTask<String, Integer, Bitmap> {
        Context context;
        HttpURLConnection conn = null;
        Bitmap bit;

        public ImgOutPut(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);;
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
        }
    }

    public void imgoutput(String imgName) {
        String urlString = "http://10.0.2.2/MyMemory/newImage"+imgName;
        Log.d("Test" , " : " + imgName);
        try {
            ImgOutPut imgoutput = new ImgOutPut(AlbumItemActivity.this);
            btmImg = imgoutput.execute(urlString).get();
        } catch (Exception e) {
            Log.d("Test","out");
        }
    }
}
