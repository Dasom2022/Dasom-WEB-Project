package com.dama.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.bytebuddy.description.type.TypeList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QrUtil {

    @PostMapping("/api/qr")
    public String makeQr(HttpServletRequest request, HttpSession session) throws WriterException, IOException {

        String root = request.getSession().getServletContext().getRealPath("resource");

        String savePath = "C:\\makeqr\\";

        File file = new File(savePath);
        if(!file.exists()){
            file.mkdirs();
        }

        String url = "localhost:8080/";
        //링크 생성
        String codeurl = new String(url.getBytes("UTF-8"), "ISO-8859-1");
        //색상
        int qrCodeColor = 0xFF2e4e96;
        //배경
        int backgroundColor = 0xFFFFFFFF;

        //QR 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(codeurl, BarcodeFormat.QR_CODE, 200, 200);

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodeColor, backgroundColor);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = sdf.format(new Date());

        File temp = new File(savePath+fileName+".png");
        System.out.println("temp :"+ temp);
        ImageIO.write(bufferedImage, "png", temp);

        return fileName+".png";
    }

    public static void makeQR(String url, int width, int height, String file_path, String file_name){
        try{
            File file = null;

            file = new File(file_path);
            if(!file.exists()){
                file.mkdirs();
            }

            QRCodeWriter writer = new QRCodeWriter();
            url = new String(url.getBytes("UTF-8"), "ISO-8859-1");
            BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);

            int qrColor = 0xFFad1004;

            MatrixToImageConfig config = new MatrixToImageConfig(qrColor, 0xFFFFFFFF);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);

            ImageIO.write(qrImage, "png", new File(file_path+file_name));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
