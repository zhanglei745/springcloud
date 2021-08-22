package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ISpecificationService;
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


    @GetMapping("groups/{cid}")
    private ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable Long cid){

        List<SpecGroup> list = this.specificationService.querySpecGroupsByCid(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);


    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySeceParam(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean searching
    ){
        List<SpecParam> specParams = this.specificationService.queryParams(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(specParams)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }


}
