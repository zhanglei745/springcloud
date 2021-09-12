package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.BrandPojo;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {

    private List<Map<String,Object>> categories;
    private List<BrandPojo> brands;

    public SearchResult() {
    }

    public SearchResult(List<Map<String, Object>> categories, List<BrandPojo> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<BrandPojo> brands) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<BrandPojo> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<BrandPojo> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandPojo> brands) {
        this.brands = brands;
    }
}
