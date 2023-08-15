package com.macro.melon.test.file;

import com.macro.melon.test.MelonTicket;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@PropertySource("classpath:test.yml")
@Component
public class MelonCaptchaImgDownload implements CaptchaImgDownload{

    public String downloadImage(MelonTicket melonTicket, String folderPath) throws IOException {
        WebElement captchaImg = melonTicket.findId("captchaImg");
        String src = captchaImg.getAttribute("src");

        String folderWithDate = createFolderWithDate(folderPath);
        String captchaImgName = createCaptchaImgName();
        String targetFileName = folderPath+"/"+folderWithDate+"/"+captchaImgName+".png";

        try {
            String[] parts = src.split(",");
            String base64Data = parts[1];
            byte[] imageData = Base64.getDecoder().decode(base64Data);

            try (InputStream in = new ByteArrayInputStream(imageData);
                 FileOutputStream out = new FileOutputStream(targetFileName)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("이미지 다운로드 완료");
            }

        } catch (IOException e) {
                System.out.println("e = " + e);
        }
        return targetFileName;
    }

    public String createFolderWithDate(String folderPath){
        // 오늘 날짜 구하기
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());

        // 폴더 생성
        File folder = new File(folderPath+"/"+currentDate);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (success) {
                System.out.println("폴더 생성 성공: " + currentDate);
            } else {
                System.out.println("폴더 생성 실패");
            }
        } else {
            System.out.println("이미 폴더가 존재합니다: " + currentDate);
        }

        return currentDate;
    }

    public String createCaptchaImgName(){
        Date now = new Date();
        SimpleDateFormat combinedFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        String currentTime = combinedFormat.format(now);
        return currentTime;
    }
}
