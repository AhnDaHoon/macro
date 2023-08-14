package com.macro.melon.test.file;

import java.io.IOException;

public interface CaptchaImgDownload {

    public String downloadImage(String imageUrl, String folderPath) throws IOException;

    public String createFolderWithDate(String folderPath);

    public String createCaptchaImgName();
}
