package com.yy.test;

import com.yy.vokiller.annotation.VoConfigure;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {


    @VoConfigure({"response1","response2"})
    private Integer id;
    @VoConfigure({"response2"})
    private String name;
    private String sex;
    private Date birthday;
    @VoConfigure({"response2"})
    private List<String> permissionList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }
}
