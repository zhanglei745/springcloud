package com.leyou.upload.test;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class FastDFSTest {

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    //@Test
    public void testUpload() throws FileNotFoundException{
        File file = new File("/Users/zhanglei745/Pictures/1618196552115.jpg");
        StorePath storePath = this.fileStorageClient.uploadFile(
                new FileInputStream(file),file.length(),"jpg",null
        );
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());

    }

    //@Test
    public void testUploadAndCreateThumb()throws FileNotFoundException{
        File file = new File("/Users/zhanglei745/Pictures/1618196552115.jpg");
        StorePath storePath = this.fileStorageClient.uploadFile(
                new FileInputStream(file),file.length(),"jpg",null
        );
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());

        //获取缩略图
        String thumbImagePath = this.thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(thumbImagePath);

    }


}
