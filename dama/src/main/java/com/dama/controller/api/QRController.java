package com.dama.controller.api;

import com.dama.model.dto.QRDTO;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import com.google.zxing.BarcodeFormat;
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
import java.awt.image.BufferedImage;
import java.io.File;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class QRController {

    private final MemberService memberService;

    @PostMapping("/qr2")
    public void makeQR(@RequestBody QRDTO qrdto) {
        String filePath="C:/makeqr/";
        try {
            File file = null;

            Member findMember = memberService.findByUsername(qrdto.getUsername());

            file = new File(filePath+"/"+findMember.getId());
            if (!file.exists()) {
                file.mkdirs();
            }

            QRCodeWriter writer = new QRCodeWriter();
            String url = new String(qrdto.getUrl().getBytes("UTF-8"), "ISO-8859-1");
            BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            int qrColor = 0xFF000000;

            MatrixToImageConfig config = new MatrixToImageConfig(qrColor, 0xFFFFFFFF);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);

            ImageIO.write(qrImage, "jpeg", new File(filePath +"/"+findMember.getId()+"/"+ findMember + ".jpeg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
