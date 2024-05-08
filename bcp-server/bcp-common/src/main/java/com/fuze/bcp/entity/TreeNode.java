package com.fuze.bcp.entity;

import com.fuze.bcp.domain.MongoBaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点对应的数据结构
 */
public class TreeNode {

    public String label = null;

    public String id = null;

    public List<TreeNode>   children = new ArrayList<TreeNode>();

    public MongoBaseEntity data = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public MongoBaseEntity getData() {
        return data;
    }

    public void setData(MongoBaseEntity data) {
        this.data = data;
    }

}
