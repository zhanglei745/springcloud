package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.BrandPojo;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.IGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //添加查询条件
        if(StringUtils.isNotEmpty(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //添加上下架过滤条件
        if (saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        //添加分页
        PageHelper.startPage(page,rows);
        //执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);

        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);

        //spu 转化成  spubo
        List<SpuBo> spuBos = spus.stream().map(spu -> { //stream 表达式，可以吧旧的集合转成新的集合
            SpuBo spubo = new SpuBo();
            BeanUtils.copyProperties(spu, spubo);
            BrandPojo brandPojo = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spubo.setCname(brandPojo.getName());
            List<String> names = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spubo.setBname(StringUtils.join(names, "-"));
            return spubo;
        }).collect(Collectors.toList());//collectors转换成相应的集合对象
        //返回分页对象

        return new PageResult<>(spuPageInfo.getTotal(),spuBos);

    }
}
