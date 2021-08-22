package com.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stock")
public class Stock {

    @Id
    private Long skuId;
    private Integer seckillStock;
    private Integer seckillTitle;
    private Integer stock;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Integer getSeckillTitle() {
        return seckillTitle;
    }

    public void setSeckillTitle(Integer seckillTitle) {
        this.seckillTitle = seckillTitle;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
