package com.example.mymemory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterReviseActivity extends AppCompatActivity {
    private AlertDialog dialog;
    AlertDialog.Builder builder;
    EditText edt_pw;
    EditText edt_name;
    EditText edt_email;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_revise);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 홈버튼 생김

        builder = new AlertDialog.Builder(RegisterReviseActivity.this,R.style.AppCompatAlertDialogStyle);
        edt_pw = findViewById(R.id.edt_R_PW);
        edt_name = findViewById(R.id.edt_R_name);
        edt_email = findViewById(R.id.edt_R_Email);
        Button btn_revise = findViewById(R.id.btn_revise);

        btn_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String u_pw = edt_pw.getText().toString();
                final String u_name = edt_name.getText().toString();
                final String u_email = edt_email.getText().toString();

                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                builder.setMessage("정보수정 완료");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }else{
                                dialog = builder.setMessage("정보수정 실패").setPositiveButton("확인",null).create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterReviseRequest reviseRequest = new RegisterReviseRequest(user_id,u_pw,u_name,u_email,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterReviseActivity.this);
                queue.add(reviseRequest);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //버튼의 액션
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        LoginActivity loginActivity = new LoginActivity();
        user_id = loginActivity.user_id;
        onList();
    }

    public void onList(){
        //회원 정보 불러들이기
        final Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    response = response.trim();
                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean success = jsonResponse.getBoolean("success");
                    String u_pw = jsonResponse.getString("u_pw");
                    String u_name = jsonResponse.getString("u_name");
                    String u_email = jsonResponse.getString("u_email");

                    if(success) {
                        edt_pw.setText(u_pw);
                        edt_name.setText(u_name);
                        edt_email.setText(u_email);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterReviseActivity.this, R.style.AppCompatAlertDialogStyle);
                        dialog = builder.setMessage("회원정보 실패").setNegativeButton("확인",null).create();
                        dialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        UserInfoRequest userInfoRequest = new UserInfoRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterReviseActivity.this);
        queue.add(userInfoRequest);
    }
}