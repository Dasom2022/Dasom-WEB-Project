package com.dama.controller.api;

import com.dama.model.dto.QRDTO;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class QRController {

    private final MemberService memberService;

    @PostMapping("/qr")
    public String makeQr(HttpServletRequest request, HttpSession session) throws WriterException, IOException {

        String root = request.getSession().getServletContext().getRealPath("resource");

        String savePath = "C:\\makeqr\\";

        File file = new File(savePath);
        if (!file.exists()) {
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

        File temp = new File(savePath + fileName + ".png");
        System.out.println("temp :" + temp);
        ImageIO.write(bufferedImage, "png", temp);

        return fileName + ".png";
    }

    @PostMapping("/qr2")
    public void makeQR(@RequestBody QRDTO qrdto) {
        try {

            Member findMember = memberService.findByUsername(qrdto.getUsername());

            File file = null;

            file = new File(qrdto.getFile_path()+"/"+findMember.getId());
            if (!file.exists()) {
                file.mkdirs();
            }

            QRCodeWriter writer = new QRCodeWriter();
            String url = new String(qrdto.getUrl().getBytes("UTF-8"), "ISO-8859-1");
            BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            int qrColor = 0xFFad1004;

            MatrixToImageConfig config = new MatrixToImageConfig(qrColor, 0xFFFFFFFF);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);

            ImageIO.write(qrImage, "jpeg", new File(qrdto.getFile_path() + qrdto.getFile_name() + ".jpeg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
