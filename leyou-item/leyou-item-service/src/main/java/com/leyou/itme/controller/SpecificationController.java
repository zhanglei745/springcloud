package com.leyou.itme.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.itme.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private ISpecificationService specificationService;


    @GetMapping("group/{cid}")
    private ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable Long cid){

        List<SpecGroup> list = this.specificationService.querySpecGroupsByCid(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);


    }

}
