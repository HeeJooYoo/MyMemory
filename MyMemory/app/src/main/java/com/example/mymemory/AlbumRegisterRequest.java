package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AlbumRegisterRequest extends StringRequest {
    final static  private  String URL = "http://10.0.2.2/MyMemory/AlbumRegister.php";
    private Map<String, String> parameters;

    public AlbumRegisterRequest(String album_id, String album_img, String album_title, String u_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("album_id", album_id);
        parameters.put("album_img", album_img);
        parameters.put("album_title", album_title);
        parameters.put("u_id", u_id);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
