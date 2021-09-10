package com.leyou.item.api;

import com.leyou.item.pojo.BrandPojo;
import org.springframework.web.bind.annotation.*;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("{id}")
    public BrandPojo queryBrandById(@PathVariable("id") Long id);


}
