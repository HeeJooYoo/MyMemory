package com.example.mymemory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;
    private RecyclerView rv;
    private RecyclerView.LayoutManager glm;

    private ArrayList<ItemForm> albumList;
    private RVAdapter rvadapter;
    Bitmap btmImg, screen; //등록된 이미지, 등록되지 않은 이미지
    ItemForm rItem;

    String albumID;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable;
        drawable = getResources().getDrawable(R.drawable.screen);
        screen = ((BitmapDrawable)drawable).getBitmap();

        LoginActivity loginActivity = new LoginActivity();
        String id = loginActivity.user_id;
        userID = id;

        dialog = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        glm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(glm);

        albumList = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ed_dig = new EditText(MainActivity.this);

                dialog.setTitle("앨범추가");       // 제목 설정
                dialog.setView(ed_dig);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String title = ed_dig.getText().toString();
                        dialogInterface.dismiss();
                        if (title.equals("")) {
                            Toast.makeText(getApplicationContext(), "앨범 제목을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Response.Listener responseListener1 = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        response = response.trim();
                                        JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                        boolean success = jsonResponse.getBoolean("success");
                                        int count = jsonResponse.getInt("count"); //로그인한 회원이 등록한 투어의 수를 알려주는 변수

                                        if(success) {
                                            count = count + 1; //투어아이디 부여를 위해 1을 증가시킴
                                            String cnt = Integer.toString(count);
                                            albumID = userID + cnt; //투어아이디는 회원아이디에 숫자를 부여해서 만듦.
                                            album(title);
                                        }else{
                                            Log.d("test1","카운트 실패");
                                            return;
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            AlbumIDRequest albumIDRequest = new AlbumIDRequest(userID,responseListener1);
                            RequestQueue queue1 = Volley.newRequestQueue(MainActivity.this);
                            queue1.add(albumIDRequest);
                        }
                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void album(String title) {
        final String _title = title;
        final String img = "1";
        final Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = response.trim();
                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        rv.setAdapter(rvadapter);
                        btmImg = screen;
                        albumList.add(new ItemForm(albumID, btmImg, _title));
                    } else {
                        Log.d("test1", "카운트 실패");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AlbumRegisterRequest albumRegisterRequest = new AlbumRegisterRequest(albumID, img, _title, userID, responseListener);
        RequestQueue queue1 = Volley.newRequestQueue(MainActivity.this);
        queue1.add(albumRegisterRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); //뒤로가기
                return true;
            case R.id.revise:
                intent = new Intent(getApplicationContext(), RegisterReviseActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                onBackPressed();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        onList();
    }

    public void onList(){
        albumList = new ArrayList<>();
        final Response.Listener responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = response.trim();
                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    JSONArray albumData =  jsonResponse.getJSONArray("albumData");

                    String album_id, album_img, album_title;
                    for (int i = 0; i < albumData.length(); i++) {
                        JSONObject object = albumData.getJSONObject(i);
                        album_id = object.getString("album_id");
                        album_img = object.getString("album_img");
                        album_title = object.getString("album_title");
                        imgoutput(album_img);

                        rItem = new ItemForm(album_id, btmImg, album_title);
                        albumList.add(rItem);
                    }
                    rvadapter = new RVAdapter(MainActivity.this, albumList);
                    rv.setAdapter(rvadapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AlbumListRequest albumListRequest = new AlbumListRequest(userID, responseListener1);
        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        queue1.add(albumListRequest);
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
                URL url = new URL(strings[0]);; //URL화
                conn = (HttpURLConnection) url.openConnection(); //URL을 연결한 객체 생성
                conn.setDoInput(true); //읽기모드 지원
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
            ImgOutPut imgoutput = new ImgOutPut(MainActivity.this);
            btmImg = imgoutput.execute(urlString).get();
        } catch (Exception e) {
            Log.d("Test","out");
        }
    }
}