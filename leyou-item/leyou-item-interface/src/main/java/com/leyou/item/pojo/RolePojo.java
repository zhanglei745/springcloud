package com.leyou.item.pojo;

import javax.persistence.Table;

@Table(name = "t_role")
public class RolePojo extends BasePojo{

    private String roleName;
    private Integer roleType;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}
