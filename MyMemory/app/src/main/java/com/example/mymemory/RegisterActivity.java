package com.example.mymemory;

import android.content.DialogInterface;
import android.graphics.Color;
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

public class RegisterActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private boolean checkdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText edt_id = findViewById(R.id.edt_ID);
        final EditText edt_pw = findViewById(R.id.edt_PW);
        final EditText edt_pw2 = findViewById(R.id.edt_PW2);
        final EditText edt_name = findViewById(R.id.edt_name);
        final EditText edt_email = findViewById(R.id.edt_Email);
        final Button btn_check = findViewById(R.id.btn_check);
        Button btn_ok = findViewById(R.id.btn_OK);
        builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AppCompatAlertDialogStyle);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String u_id = edt_id.getText().toString();

                if(u_id.equals("")){
                    dialog = builder.setMessage("아이디를 입력해주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    checkdate = false;
                    edt_id.setFocusable(true);
                    return;
                }

                if(checkdate) return;
                final Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean newID = jsonResponse.getBoolean("newID");

                            if(newID){
                                dialog = builder.setMessage("사용 가능한 아이디입니다.").setPositiveButton("확인",null).create();
                                dialog.show();
                                edt_id.setEnabled(false);
                                edt_id.setBackgroundColor(Color.GRAY);
                                btn_check.setBackgroundColor(Color.GRAY);
                                checkdate = true;
                            } else {
                                dialog = builder.setMessage("중복된 아이디입니다.").setPositiveButton("확인",null).create();
                                dialog.show();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                CheckRequest checkRequest = new CheckRequest(u_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(checkRequest);
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_id = edt_id.getText().toString();
                String u_pw = edt_pw.getText().toString();
                String u_pw2 = edt_pw2.getText().toString();
                String u_name = edt_name.getText().toString();
                String u_email = edt_email.getText().toString();

                if(!checkdate){
                    dialog = builder.setMessage("중복체크를 해주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }
                if (u_id.equals("") || u_pw.equals("") || u_name.equals("") || u_email.equals("")) {
                    dialog = builder.setMessage("모든 정보를 입력해주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }

                if (!u_pw.equals(u_pw2)) {
                    dialog = builder.setMessage("비밀번호 불일치").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }

                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                builder.setMessage("회원가입 성공");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }else{
                                dialog = builder.setMessage("회원가입 실패").setPositiveButton("확인",null).create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(u_id, u_pw, u_name, u_email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }

    protected  void onStop(){
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
