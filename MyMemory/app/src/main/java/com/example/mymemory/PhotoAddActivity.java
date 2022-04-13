package com.example.mymemory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//사진+글 추가
public class PhotoAddActivity extends AppCompatActivity {
    int num;
    static final int REQUEST_TAKE_ALBUM = 2002;
    static final int REQUEST_IMAGE_CROP = 2003;
    String mCurrentPhotoPath;
    Uri photoURI, albumURI;

    boolean isAlbum = false;

    ImageView pt_img;
    EditText pt_edt;
    Button pt_btn;

    private AlertDialog dialog;
    AlertDialog.Builder builder;

    String albumID, p_img;
    public static String photoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder = new AlertDialog.Builder(PhotoAddActivity.this, R.style.AppCompatAlertDialogStyle);
        pt_img = findViewById(R.id.img_add_pt);
        pt_edt = findViewById(R.id.edt_add_pt);
        pt_btn = findViewById(R.id.btn_pt_OK);

        AlbumItemActivity albumItemActivity = new AlbumItemActivity();
        String a_id = albumItemActivity.albumID;
        albumID = a_id;

        pt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlbum();
            }
        });

        pt_btn.setOnClickListener(new View.OnClickListener() {
            String photo_img, photo_edt;
            @Override
            public void onClick(View v) {
                try {
                    photo_img = mCurrentPhotoPath.substring(28);
                    photo_edt = pt_edt.getText().toString();
                    photoID = getIntent().getStringExtra("photo_id");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = response.trim();
                            JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                dialog = builder.setMessage("성공").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create();
                                dialog.show();
                            } else {
                                dialog = builder.setMessage("실패")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (JSONException je) {
                            Log.d("JSONError", " : " + je);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Test", e + "!");
                        }
                    }
                };

                try {
                    if(!isAlbum){
                        dialog = builder.setMessage("사진 등록은 필수로 해주세요.").setPositiveButton("확인", null).create();
                        dialog.show();
                        return;
                    } else {
                        p_img = photo_img;
                        PhotoAddRequest photoAddRequest = new PhotoAddRequest(photoID, photo_img, photo_edt, albumID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(PhotoAddActivity.this);
                        queue.add(photoAddRequest);

                        onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("TEST", " : " + albumID + photo_img);
                Response.Listener responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Log.d("Success" , "!");
                            }else{
                                Log.d("fail" , "!");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                AlbumImaUdateRequest albumImaUdateRequest = new AlbumImaUdateRequest(albumID, photo_img,responseListener);
                RequestQueue queue = Volley.newRequestQueue(PhotoAddActivity.this);
                queue.add(albumImaUdateRequest);
            }
        });
    }

    public class AlbumImaUdateRequest extends StringRequest {
        final static  private  String URL = "http://10.0.2.2/MyMemory/AlbumImgUpdate.php";
        private Map<String, String> parameters;

        public AlbumImaUdateRequest(String album_id, String album_img, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("album_id", album_id);
            parameters.put("album_img", album_img);
        }
        public Map<String, String> getParams() {
            return parameters;
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

    public void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM); //데이터를 이전 액티비티로 돌려줌
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void cropImage() {
        Log.i("cropImage", "Call");
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");

        //cropIntent.putExtra("outputX", 300); // crop한 이미지의 x축 크기
       // cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장

        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }


    //startActivityForResult로 호출됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "CALL");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        try {
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            isAlbum = true;
                            cropImage();
                        } catch (Exception e) {
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    galleryAddPic();
                    pt_img.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    pt_img.setImageURI(albumURI);
                    Log.d("TEST,PhotoAdd", " : " + albumURI);
                    uploadFile(mCurrentPhotoPath);
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imgFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() +"/MyMemory");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }
        imgFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imgFile.getAbsolutePath();

        Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
        return imgFile;
    }

    public class UploadFile extends AsyncTask<String, String, String> {
        Context context; // 생성자 호출 시
        ProgressDialog mProgressDialog; // 진행 상태 다이얼로그
        String fileName; // 파일 위치

        HttpURLConnection conn = null; // 네트워크 연결 객체
        DataOutputStream dos = null; // 서버 전송 시 데이터 작성한 뒤 전송

        String lineEnd = "\r\n"; // 구분자
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024*1024;
        File sourceFile;
        int serverResponseCode;
        String TAG = "FileUpload";

        public UploadFile(Context context) {
            this.context = context;
        }

        public void setPath(String uploadFilePath) {
            this.fileName = uploadFilePath;
            this.sourceFile = new File(uploadFilePath);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("이미지 업로드......");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (!sourceFile.isFile()) { // 해당 위치의 파일이 있는지 검사
                Log.e(TAG, "sourceFile(" + fileName + ") is Not A File");
                return null;
            } else {
                String success = "success";
                Log.i(TAG, "sourceFile(" + fileName + ") is A File");
                try {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(strings[0]);
                    Log.i("strings[0]", strings[0]);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 읽기 가능
                    conn.setDoOutput(true); // 쓰기 가능
                    conn.setUseCaches(false); // 캐시가 카피되게 할지 여부
                    conn.setRequestMethod("POST"); // 전송 방식
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); // boundary 기준으로 인자를 구분함
                    conn.setRequestProperty("uploaded_file", fileName);
                    Log.i(TAG, "fileName: " + fileName);

                    // dataoutput은 outputstream이란 클래스를 가져오며, outputStream는 FileOutputStream의 하위 클래스이다.
                    // output은 쓰기, input은 읽기, 데이터를 전송할 때 전송할 내용을 적는 것으로 이해할 것
                    dos = new DataOutputStream(conn.getOutputStream());

                    // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다. 하나의 인자 전달 data1 = newImage
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"data1\"" + lineEnd); // name으 \ \ 안 인자가 php의 key
                    dos.writeBytes(lineEnd);
                    dos.writeBytes("newImage"); // newImage라는 값을 넘김
                    dos.writeBytes(lineEnd);


                    // 이미지 전송, 데이터 전달 uploadded_file라는 php key값에 저장되는 내용은 fileName
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data..., 마지막에 two~~ lineEnd로 마무리 (인자 나열이 끝났음을 알림)
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i(TAG, "[UploadImageToServer] HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {
                        Log.d("Test","success");
                    }

                    // 결과 확인
                    BufferedReader rd = null;

                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.i("Upload State", line);
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                } catch (MalformedURLException ex) {
                    Log.d("Test", "MalformedURLException " + ex.getMessage());
                } catch (NetworkOnMainThreadException ne) {
                    Log.d("Test", "NetworkOnMain " + ne.getMessage());
                } catch (Exception e) {
                    Log.d("Test", "exception " + e.getMessage());
                    // TODO: handle exception
                }
                return success;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.cancel();
        }
    }

    public void uploadFile(String filePath) {
        String url = "http://10.0.2.2/MyMemory/ImgUpLoad.php";
        try {
            UploadFile uploadFile = new UploadFile(PhotoAddActivity.this);
            uploadFile.setPath(filePath);
            uploadFile.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}