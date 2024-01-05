package org.choongang.file;

import org.choongang.file.service.FileInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class ThumbnailTest {
    @Autowired
    private FileInfoService fileInfoService;

    @Test
    void getThumbTest(){
        String[] data = fileInfoService.getThumb(252L, 150,150);
        System.out.println(Arrays.toString(data));
    }

    @Test
    void getThumbTest1(){
        String[] data = fileInfoService.getThumb(3L, 150,150);
        System.out.println(Arrays.toString(data));
    }

    @Test
    void getThumbTest2(){

    }
}
