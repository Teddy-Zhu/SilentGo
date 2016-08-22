package com.silentgo.web.model;

import com.alibaba.fastjson.JSON;
import com.silentgo.core.config.Const;

import java.util.Date;

/**
 * Project : silentgo
 * com.silentgo.web.model
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/22.
 */
public class User {

    private String username;

    private String password;

    private Integer age;

    private Date birth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
