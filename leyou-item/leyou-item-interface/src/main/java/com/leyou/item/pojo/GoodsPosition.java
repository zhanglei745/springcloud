package com.leyou.item.pojo;

import javax.persistence.Table;

@Table(name ="goods_position")
public class GoodsPosition extends BasePojo{


    private String name;//位置名称
    private String position;//位置所在地
    private String type;//收纳类型
    private String volume;//收纳体积
    private Integer pid ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
