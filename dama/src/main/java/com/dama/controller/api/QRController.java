package com.dama.controller.api;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dama.model.dto.QRDTO;
import com.dama.model.dto.response.QRLoginResponseDto;
import com.dama.model.entity.Member;
import com.dama.principal.SecurityUtil;
import com.dama.service.MemberService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class QRController {

    private final SecurityUtil securityUtil;

    private final MemberService memberService;
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String dirName="MemberQR";

    public String uploadImageUrl;

    public String fileName;

//    public String API_URL="http://43.200.61.12:3333/api/qrCode/loginState/";

    public String TEST_API_URL="http://localhost:3333/api/qrCode/loginState/";

    private final AmazonS3Client amazonS3Client;

    @PostMapping("/qr")
    public String makeQR(@RequestBody QRDTO qrdto) throws IOException {
        String filePath="C:/makeqr/";
        fileName=dirName+"/"+memberService.findByUsername(qrdto.getUsername()).getUsername();
        try {
            File file = null;

            Member findMember = memberService.findByUsername(qrdto.getUsername());
            TEST_API_URL+=qrdto.getUsername();
            file = new File(filePath+"/"+findMember.getId());
            if (!file.exists()) {
                file.mkdirs();
            }

            QRCodeWriter writer = new QRCodeWriter();
            String url = new String(TEST_API_URL.getBytes("UTF-8"), "ISO-8859-1");
            BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            int qrColor = 0xFF000000;

            MatrixToImageConfig config = new MatrixToImageConfig(qrColor, 0xFFFFFFFF);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);
            File qrFile = new File(filePath + "/" + findMember.getId() + "/" + findMember + ".jpeg");
            ImageIO.write(qrImage, "jpeg", qrFile);

            amazonS3Client.putObject(new PutObjectRequest(bucket,fileName,qrFile).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket,fileName).toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @GetMapping("/qrCode/loginState/{username}")
    public ResponseEntity<QRLoginResponseDto> getSecurityContextHolderToQR(@PathVariable("username")String username){

        QRLoginResponseDto qrLoginResponseDto = securityUtil.returnLoginMemberInfoToQR();

        return new ResponseEntity<>(qrLoginResponseDto, HttpStatus.OK);
    }
}
