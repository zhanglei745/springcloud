package com.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spu_detail")
public class SpuDetail {

    @Id
    private Long spuId;
    private String description;//商品描述
    private String specialSpec;//特殊规格参数
    private String genericSpec;//全局规则参数
    private String packingList;//包装清单
    private String afterService;//售后服务

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }
}
