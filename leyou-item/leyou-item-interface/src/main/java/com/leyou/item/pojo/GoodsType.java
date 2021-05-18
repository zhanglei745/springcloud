package com.leyou.item.pojo;

import javax.persistence.Table;

@Table(name = "goods_type")
public class GoodsType extends BasePojo {

    private String name;
    private Integer level;
    private String describes;
    private Integer pid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
