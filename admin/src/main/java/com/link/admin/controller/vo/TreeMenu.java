package com.link.admin.controller.vo;

import com.jfinal.kit.StrKit;
import com.link.model.Menu;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linkzz on 2017-06-10.
 */
public class TreeMenu extends Menu {
    private List<Menu> nodes;
    private List<TreeMenu> treeMenus;
    private int size = 0;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<TreeMenu> getTreeMenus() {
        return treeMenus;
    }

    public void setTreeMenus(List<TreeMenu> treeMenus) {
        this.treeMenus = treeMenus;
    }

    public TreeMenu(){}

    public TreeMenu(List<Menu> nodes){
        this.nodes = nodes;
    }

    public List<TreeMenu> buildTree(){
        List<TreeMenu> treeMenus0 = new ArrayList<>();
        for (Menu node : nodes) {
            if (StrKit.isBlank(node.getParent())) {
                TreeMenu treeMenu = new TreeMenu();
                BeanUtils.copyProperties(node,treeMenu);
                build(node,treeMenu);
                treeMenus0.add(treeMenu);
            }
        }
        return treeMenus0;
    }

    private void build(Menu node,TreeMenu treeMenu){
        List<Menu> children = getChildren(node);
        if (!children.isEmpty()) {
            List<TreeMenu> list = new ArrayList<>();
            for (Menu child : children) {
                TreeMenu treeMenu1 = new TreeMenu();
                BeanUtils.copyProperties(child,treeMenu1);
                build(child,treeMenu1);
                list.add(treeMenu1);
            }
            treeMenu.setTreeMenus(list);
            treeMenu.setSize(children.size());
        }
    }

    private List<Menu> getChildren(Menu node){
        List<Menu> children = new ArrayList<Menu>();
        String id = node.getId();
        for (Menu child : nodes) {
            if (id.equals(child.getParent())) {
                children.add(child);
            }
        }
        return children;
    }
}
