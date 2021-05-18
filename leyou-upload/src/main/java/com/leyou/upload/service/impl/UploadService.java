package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.IUploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService implements IUploadService {

    private static  final List<String> CONTENT_TYPE = Arrays.asList("image/gif","image/jpeg","image/png");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Override
    public String uploadImage(MultipartFile file) {

        try{
            //文件类型
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            if(!CONTENT_TYPE.contains(contentType)){
                LOGGER.info("文件类型不合法，当前文件为：{}",originalFilename);
                return null;
            }


            //文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null ){
                LOGGER.info("文件内容不合法，当前文件为：{}",originalFilename);
                return null;
            }


            //保存到服务器
            String s = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath sp = this.fileStorageClient.uploadFile(file.getInputStream(), file.getSize(), s,null);
            //file.transferTo(new File("/Users/zhanglei745/IdeaProjects/leyou-image/"+originalFilename));

            //返回文件地址
            //return "image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/" + sp.getFullPath();
        }catch(Exception e){
            e.printStackTrace();

        }
        return null;

    }
}
