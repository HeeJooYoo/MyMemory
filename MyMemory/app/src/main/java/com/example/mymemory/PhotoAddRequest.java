package com.example.mymemory;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PhotoAddRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2/MyMemory/PhotoAdd.php";
    private Map<String, String> parameters;

    public PhotoAddRequest(String photo_id, String photo_img, String photo_txt, String album_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("photo_id", photo_id);
        parameters.put("photo_img", photo_img);
        parameters.put("photo_txt", photo_txt);
        parameters.put("album_id", album_id);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
