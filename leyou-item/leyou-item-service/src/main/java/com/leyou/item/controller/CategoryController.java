package com.leyou.item.controller;

import com.leyou.item.pojo.CategoryPojo;
import com.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<CategoryPojo>> queryByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        //参数校验
        if(null==pid || pid<0){
            return ResponseEntity.badRequest().build();
        }
        //数据获取
        List<CategoryPojo> list = this.categoryService.queryListByPid(pid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        //返回数据
        return ResponseEntity.ok(list);

    }

    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam List<Long> ids){
        List<String> names = this.categoryService.queryNameByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        //返回数据
        return ResponseEntity.ok(names);
    }


}
