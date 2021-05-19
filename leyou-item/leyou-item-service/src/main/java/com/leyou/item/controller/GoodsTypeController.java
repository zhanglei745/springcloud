package com.leyou.item.controller;


import com.leyou.item.pojo.GoodsType;
import com.leyou.item.service.IGoodsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("goodstype")
public class GoodsTypeController {

    @Autowired
    private IGoodsType goodsTypeService;

    @GetMapping("list")
    public ResponseEntity<List<GoodsType>> queryGoodsTypeByPid(@RequestParam(value = "pid",defaultValue = "0")Integer pid){
        if(pid==null || pid < 0){
            return ResponseEntity.badRequest().build();
        }
        List<GoodsType> goodsTypes = goodsTypeService.queryGoodsTypeByPid(pid);
        if(CollectionUtils.isEmpty(goodsTypes)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(goodsTypes);

    }

}
