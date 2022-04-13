package com.example.mymemory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class FindActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    TextView txt_dig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        builder = new AlertDialog.Builder(FindActivity.this, R.style.AppCompatAlertDialogStyle);
        Button id_find = findViewById(R.id.btn_F_ID);
        Button pw_find = findViewById(R.id.btn_F_PW);
        final EditText f_name = findViewById(R.id.edt_F_name);
        final EditText f_email = findViewById(R.id.edt_F_email);
        final EditText f_id = findViewById(R.id.edt_F_ID);
        final EditText f_email2 = findViewById(R.id.edt_F_email2);

        id_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String find_name = f_name.getText().toString();
                final String find_email = f_email.getText().toString();

                if(find_name.equals("") || find_email.equals("")){
                    dialog = builder.setMessage("내용을 입력해주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    f_name.setFocusable(true);
                    return;
                }

                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            response = response.trim();
                            JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean success = jsonResponse.getBoolean("success");
                            final String u_id;
                            txt_dig = new TextView(FindActivity.this);

                            if(success) {
                                u_id = jsonResponse.getString("u_id");
                                builder.setTitle("아이디 확인");
                                txt_dig.setText(u_id);
                                //dialog.setView(txt_dig);
                                dialog.setMessage(u_id);
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this, R.style.AppCompatAlertDialogStyle);
                                dialog = builder.setMessage("입력 정보를 확인해주세요.").setPositiveButton("확인",null).create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ID_FindRequest id_f_Request = new ID_FindRequest(find_name,find_email,responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindActivity.this);
                queue.add(id_f_Request);
            }
        });

        pw_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String find_id = f_id.getText().toString();
                final String find_email2 = f_email2.getText().toString();

                if(f_id.equals("") || f_email2.equals("")){
                    dialog = builder.setMessage("내용을 입력해주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    f_id.setFocusable(true);
                    return;
                }
                Log.d("Log", "test : " + find_id);
                Log.d("Log", "test : " + find_email2);

                final Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            response = response.trim();
                            JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean success = jsonResponse.getBoolean("success");
                            String u_pw;

                            if(success) {
                                u_pw = jsonResponse.getString("u_pw");
                                builder.setTitle("아이디 확인");
                                txt_dig.setText(u_pw);
                                dialog.setView(txt_dig);
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this, R.style.AppCompatAlertDialogStyle);
                                dialog = builder.setMessage("입력 정보를 확인해주세요.").setPositiveButton("확인",null).create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                PW_FindRequest pw_f_Request = new PW_FindRequest(find_id,find_email2,responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindActivity.this);
                queue.add(pw_f_Request);
            }
       });
    }
}
