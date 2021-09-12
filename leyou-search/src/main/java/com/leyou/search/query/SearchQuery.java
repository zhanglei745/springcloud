package com.leyou.search.query;

public class SearchQuery {

    private String key;//搜索条件
    private Integer page;//当前页
    private static final Integer DEFAULT_SEIZ = 20;//固定页面大小
    private static final Integer DEFAULT_PAGE = 1;//默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page ==null){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE,page);//获取的页码不能小于1
    }

    public void setPage(Integer page) {
        this.page = page;
    }

   public Integer getSize(){
        return DEFAULT_SEIZ;
   }
}
