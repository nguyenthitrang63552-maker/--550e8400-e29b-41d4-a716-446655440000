package com.ruoyi.Xidian.domain.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeTableVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    private String ParentName;
    private String Type;
    private List<?> children = new ArrayList<>();

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String ParentName) {
        this.ParentName = ParentName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
