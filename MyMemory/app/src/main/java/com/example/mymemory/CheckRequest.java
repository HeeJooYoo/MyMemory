package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//중복 아이디 체크
public class CheckRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2/MyMemory/UserCheck.php";
    private Map<String, String> parameters;

    public CheckRequest(String u_id, Response.Listener<String> Listener) {
        super(Method.POST, URL, Listener, null);

        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
    }

    @Override
    public Map<String, String> getParams() {return parameters;}
}

