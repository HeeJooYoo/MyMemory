package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterReviseRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2/MyMemory/UserUpdate.php";
    private Map<String, String> parameters;

    public RegisterReviseRequest(String u_id, String u_pw, String u_name, String u_email, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("u_pw", u_pw);
        parameters.put("u_name", u_name);
        parameters.put("u_email", u_email);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
