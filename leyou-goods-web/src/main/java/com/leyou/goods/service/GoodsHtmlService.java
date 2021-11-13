package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;

    public void createHtml(Long spuId) {

        Context context = new Context();
        context.setVariables(this.goodsService.loadData(spuId));
        String path = "/usr/local/nginx/html/item/"+spuId+".html";
        File file = new File(path);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(printWriter!= null){
                printWriter.close();
            }
        }



    }


}
