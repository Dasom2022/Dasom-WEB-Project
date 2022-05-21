package com.dama.controller.api;

import com.dama.model.entity.Member;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;

@Controller
public class QRController {

    @PostMapping("/register")
    public String makeQr(HttpServletRequest request, HttpSession session, String storeName, Member member) throws Exception{

        //현재 서비스가 돌아가고 있는 서블릿 경로의 resource
        String root = "C:\\Dasom-WEB-Project\\dama\\src\\main\\resources\\templates";

        String savePath = root + "\\qrCodes\\";

        File file = new File(savePath);
        if(!file.exists()){
            file.mkdirs();
        }

        String code = "{\"name\": \""+ member.getUsername() + "\",\"num\": \""+member.getPassword()+ "\"}";

        String codeUrl = new String(code.getBytes("UTF-8"), "ISO-8859-1");

        int qrCodeColor = 0xFF2e4e96;

        int backgroundColor = 0xFFFFFFFF;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(codeUrl, BarcodeFormat.QR_CODE, 800, 800);

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodeColor, backgroundColor);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

        ImageIO.write(bufferedImage, "png", new File(savePath+member.getId()+"_qrcode.png"));

        return "qrlogin";
    }
}
