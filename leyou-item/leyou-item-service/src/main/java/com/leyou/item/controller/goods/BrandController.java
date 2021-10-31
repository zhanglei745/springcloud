package com.leyou.item.controller.goods;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.BrandPojo;
import com.leyou.item.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<BrandPojo>> querypage(@RequestParam(value = "key",required = false)String key,
                                                           @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                           @RequestParam(value = "rows",defaultValue = "5")Integer rows,
                                                           @RequestParam(value = "sortBy",required = false)String sortBy,
                                                           @RequestParam(value = "desc",required = false)Boolean desc){
        PageResult<BrandPojo> brandPojoPageResult = brandService.queryBrands(key, page, rows, sortBy, desc);
        if(CollectionUtils.isEmpty(brandPojoPageResult.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brandPojoPageResult);

    }
    @PostMapping
    public ResponseEntity<Void> saveBrand(BrandPojo brand,@RequestParam(value = "cids") List<Long> cids){
            this.brandService.save(brand,cids);
            return ResponseEntity.status(HttpStatus.CREATED).build();//返回201
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<BrandPojo>> queryBrandByCid(@PathVariable("cid")Long cid){
        List<BrandPojo> brands = this.brandService.queryBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<BrandPojo> queryBrandById(@PathVariable("id") Long id){

        BrandPojo brandPojo = this.brandService.queryBrandById(id);
        if(null == brandPojo){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brandPojo);
    }


}
