package com.dama.service.social;


import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.entity.SocialType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//커밋용주석
@RequiredArgsConstructor
@Service
public class KakaoService {

    //AccessToken으로 UserInfo 받기
    public UserRequestDto getUserInfo(String access_Token) {

        //UserRequestDto에 정보 받기
        UserRequestDto userInfo = new UserRequestDto();

        try {
            URL url = new URL("https://kapi.kakao.com/v2/user/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String kakaoId = element.getAsJsonObject().get("id").getAsString();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String imgURL = properties.getAsJsonObject().get("profile_image").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            //    UserRequestDto에 값 주입
            userInfo.setUsername(nickname);
            userInfo.setSocialId(kakaoId);
            userInfo.setEmail(email);
            userInfo.setImgURL(imgURL);
            userInfo.setSocialType(SocialType.KAKAO);


        } catch (IOException e) {   // 잘못된 값 주입하고 에러 터지는 지 Test
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;    //글로벌 에러 처리할 때 변경
        }

        return userInfo;
    }

}

