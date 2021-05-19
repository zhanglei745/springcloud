package com.leyou.item.controller;

import com.leyou.item.pojo.GoodsPosition;
import com.leyou.item.service.impl.GoodsPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("goodsposition")
public class GoodsPositionController {

    @Autowired
    private GoodsPositionService goodsPositionService;

    @GetMapping("list")
    public ResponseEntity<List<GoodsPosition>> querylistByPid(@RequestParam(value = "pid",defaultValue = "0")Integer pid){
        if(pid==null || pid <0){
            return  ResponseEntity.badRequest().build();
        }
        List<GoodsPosition> list = goodsPositionService.queryListByPid(pid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);


    }



}
