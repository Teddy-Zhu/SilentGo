package com.silentgo.lc4e.entity;

import com.silentgo.lc4e.dao.SysMenu;

import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.lc4e.entity
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class MenuEntity extends SysMenu {

    public List<MenuEntity> children;

    public List<MenuEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MenuEntity> children) {
        this.children = children;
    }
}
