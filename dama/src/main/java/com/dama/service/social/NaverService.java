package com.dama.service.social;

import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.entity.SocialType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2//커밋용주석
@RequiredArgsConstructor
public class NaverService {
    public UserRequestDto getUserInfo(String accessToken){
        UserRequestDto userRequestDto = new UserRequestDto();

        try {
            URL url=new URL("https://openapi.naver.com/v1/nid/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestMethod("POST");

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line="";
            String result="";

            while ((line=bufferedReader.readLine())!=null){
                result+=line;
            }
            System.out.println("result = " + result);

            JsonParser jsonParser=new JsonParser();
            JsonElement element = jsonParser.parse(result);

            JsonObject response = element.getAsJsonObject().get("response").getAsJsonObject();

            String naverId = response.getAsJsonObject().get("id").getAsString();
//            String imgUrl = response.getAsJsonObject().get("profile_image").getAsString();
            String email = response.getAsJsonObject().get("email").getAsString();
            String username = response.getAsJsonObject().get("name").getAsString();

            System.out.println("username = " + username);
            userRequestDto.setSocialId(naverId);
            userRequestDto.setEmail(email);
            userRequestDto.setUsername(username);
//            userRequestDto.setImgURL(imgUrl);
            userRequestDto.setSocialType(SocialType.NAVER);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return userRequestDto;
    }
}
