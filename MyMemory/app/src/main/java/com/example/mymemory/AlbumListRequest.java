package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AlbumListRequest extends StringRequest {
    final static  private  String URL = "http://10.0.2.2/MyMemory/AlbumList.php";
    private Map<String, String> parameters;

    public  AlbumListRequest(String u_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}

