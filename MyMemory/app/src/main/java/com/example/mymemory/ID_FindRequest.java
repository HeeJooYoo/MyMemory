package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ID_FindRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2/MyMemory/UserIDFind.php";
    private Map<String,String> parameters;

    public ID_FindRequest(String u_name, String u_email, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_name", u_name);
        parameters.put("u_email",u_email);
    }
    @Override
    public Map<String, String> getParams() {return parameters;}
}

