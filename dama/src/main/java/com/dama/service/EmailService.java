package com.dama.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public static String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception{
        StringBuilder st = new StringBuilder();
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("DAMA 인증번호가 도착했습니다.");//제목

        st.append("<div style='margin:100px;'>");
        st.append("<h1> 안녕하세요 DAMA 입니다. </h1>");
        st.append("<br>");
        st.append("<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>");
        st.append("<br>");
        st.append("<p>인증을 마치면 정상적으로 서비스가 사용가능합니다<p>");
        st.append("<br>");
        st.append("<p>감사합니다!!<p>");
        st.append("<div>");
        st.append("<h3 style='color:blue;'>회원가입 코드입니다.</h3>");
        st.append("<div style='font-size:130%'>");
        st.append("CODE : <strong>");
        st.append(ePw).append("</strong><div><br/> ");
        st.append("</div>");
        message.setText(st.toString(), "utf-8", "html");//내용
        message.setFrom(new InternetAddress("lsh6451217@gmail.com","DAMA"));//보내는 사람

        return message;
    }
    //    인증코드 만들기
    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    public void sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        System.out.println("to = " + to);
        System.out.println("message = " + message);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}