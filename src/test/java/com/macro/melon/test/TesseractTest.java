package com.macro.melon.test;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TesseractTest {
    MelonTicketService melonTicketService = new MelonTicketService();

    Tesseract tesseract;

    String imgPathAndName = "C:\\Users\\user\\Desktop\\ticket_img\\20230814\\20230814200014.png";

    @BeforeEach
    void setupTest() {
        tesseract = melonTicketService.getTesseract();

    }

    @Test
    void convertImageToString() {
        String result = null;

        File file = new File(imgPathAndName);

        if(file.exists() && file.canRead()) {
            try {
                result = tesseract.doOCR(file);
            } catch (TesseractException e) {
                result = e.getMessage();
            }
        } else {
            result = "not exist";
        }
        System.out.println(result.substring(0, 6));
        assertEquals("IQDUKR", result.substring(0, 6));
    }
}
