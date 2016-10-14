package com.silentgo.lc4e.web.service;


import com.silentgo.core.cache.annotation.Cache;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.lc4e.dao.SysMenuDao;
import com.silentgo.lc4e.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teddy on 2015/8/2.
 */
@Service
public class MenuService {

    @Inject
    SysMenuDao sysMenuDao;

    @Cache(cacheName = "UiData", key = "menus")
    public List<MenuEntity> getMenuTree() {
        MenuEntity menuTree;
        List<MenuEntity> allMenus = sysMenuDao.queryListOrderByParentIdAndOrder();

        if (allMenus == null || allMenus.isEmpty()) {
            return new ArrayList<>();
        }
        menuTree = allMenus.get(0);
        menuTree.setChildren(new ArrayList<>());
        allMenus.remove(0);
        getMenu(allMenus, menuTree);
        return menuTree.getChildren();
    }

    private void getMenu(List<MenuEntity> allMenus, MenuEntity curMenu) {
        List<MenuEntity> children = curMenu.getChildren();
        for (int i = 0, len = allMenus.size(); i < len; ) {
            if (allMenus.get(i).getParentId().equals(curMenu.getId())) {
                children.add(allMenus.get(i));
                allMenus.remove(i);
                len--;
            } else {
                break;
            }
        }
        for (MenuEntity aChildren : children) {
            aChildren.setChildren(new ArrayList<>());
            getMenu(allMenus, aChildren);
        }
    }

}
