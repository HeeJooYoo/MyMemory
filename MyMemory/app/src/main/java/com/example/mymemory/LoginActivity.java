package com.example.mymemory;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private AlertDialog dialog;
    AlertDialog.Builder builder;
    public static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        final EditText edt_id = findViewById(R.id.login_ID);
        final EditText edt_pw = findViewById(R.id.login_PW);
        final Button btn_Login = findViewById(R.id.btn_Login);
        TextView txt_Find = findViewById(R.id.txt_Find);

        findViewById(R.id.btn_Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register_intent);
            }
        });

        //아이디 패스워드 찾기에 밑줄 긋기
        SpannableString content = new SpannableString("아이디/비밀번호 찾기");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txt_Find.setText(content);

        txt_Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent find_intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(find_intent);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String u_id = edt_id.getText().toString();
                final String u_pw = edt_pw.getText().toString();

                if(u_id.equals("") || u_pw.equals("")){
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                user_id = u_id;
                                finish();
                            }else{
                                dialog = builder.setMessage("로그인 정보를 확인하세요").setNegativeButton("확인",null).create();
                                dialog.show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(u_id, u_pw, responseListener);
                RequestQueue queue = Volley.newRequestQueue (LoginActivity.this);
                queue.add(loginRequest);

                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);*/
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
