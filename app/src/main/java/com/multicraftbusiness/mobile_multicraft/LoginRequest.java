package com.multicraftbusiness.mobile_multicraft;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asus Pc on 16/05/2018.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://tifb.multicraftbusiness.com/loginandroid/login.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password,Response.Listener<String> Listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, Listener, null);
        params = new HashMap<>();
        params.put("nama_user", username);
        params.put("password", password);

    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}

