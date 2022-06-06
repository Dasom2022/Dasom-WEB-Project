package com.dama.service.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public String tokenReturn(ResponseEntity<String> accessTokenResponse){
        String body = accessTokenResponse.getBody();
        JsonParser jsonParser=new JsonParser();
        JsonElement value = jsonParser.parse(body);
        System.out.println("value = " + value);
        String access_token = value.getAsJsonObject().get("access_token").getAsString();
        System.out.println("access_token = " + access_token);

        return access_token;
        /*String access_token = (String) obj1.get("access_token");
        System.out.println("access_token = " + access_token);
    */}
}
