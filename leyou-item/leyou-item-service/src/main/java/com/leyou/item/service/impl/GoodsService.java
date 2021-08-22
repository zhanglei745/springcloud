package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.IGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;


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

    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);//执行效率高

        //新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        savsSkuAndStock(spuBo);
    }

    private void savsSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //删除 sku 根据spuid
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            this.stockMapper.deleteByPrimaryKey(sku.getId());//删除stock
        });
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        this.skuMapper.delete(sku);

        //新增sku和stock
        this.savsSkuAndStock(spuBo);

        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());



    }
}
