package com.macro.melon.test.file;

import com.macro.melon.test.MelonTicket;

import java.io.IOException;

public interface CaptchaImgDownload {

    public String downloadImage(MelonTicket melonTicket, String folderPath) throws IOException;

    public String createFolderWithDate(String folderPath);

    public String createCaptchaImgName();


}
