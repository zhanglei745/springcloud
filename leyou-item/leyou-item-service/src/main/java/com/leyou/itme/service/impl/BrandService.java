package com.leyou.itme.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.BrandPojo;
import com.leyou.itme.service.IBrandService;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;


    @Override
    public PageResult<BrandPojo> queryBrands(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        Example example = new Example(BrandPojo.class);
        Example.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)){
            criteria.andLike("name","%"+key+"%").orLike("letter","%"+key+"%");
        }
        //分页
        PageHelper.startPage(page,rows);

        //排序
        if(!StringUtils.isEmpty(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "asc" : "desc"));
        }
        List<BrandPojo> brandPojos = this.brandMapper.selectByExample(example);

        PageInfo<BrandPojo> pageinfo = new PageInfo<>(brandPojos);

        return new PageResult<>(pageinfo.getTotal(),pageinfo.getList());
    }

    @Override
    public void save(BrandPojo brand, List<Long> cids) {

        this.brandMapper.insertSelective(brand);

        cids.forEach(cid->{
            this.brandMapper.insertBrandCategorys(brand.getId(),cid);
        });

    }
}
