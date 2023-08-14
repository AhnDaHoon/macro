package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TesseractTest {
    MelonTicket melonTicket = new MelonTicket();

    Tesseract tesseract;

    String imgPathAndName = "C:\\Users\\user\\Desktop\\ticket_img\\20230814\\20230814200014.png";

    @BeforeEach
    void setupTest() {
        tesseract = melonTicket.getTesseract();

    }

    @Test
    void convertImageToString() {
        String result = null;

        File file = new File(imgPathAndName);

        if(file.exists() && file.canRead()) {
            try {
                result = tesseract.doOCR(file).substring(0, 6);
            } catch (TesseractException e) {
                result = e.getMessage();
            }
        } else {
            result = "not exist";
        }
        System.out.println(result);
        assertEquals("IQDUKR", result);
    }
}
