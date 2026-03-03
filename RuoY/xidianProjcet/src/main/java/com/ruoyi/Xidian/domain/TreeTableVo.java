package com.ruoyi.Xidian.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.TreeEntity;

import java.util.Date;

public class TreeTableVo extends TreeEntity {
    private String id;
    private String name;
    private String targetId;
    private String targetType;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    private String Location;
    private String contentDesc;

    private String path;

    private String Type;


    public String getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public Date getStartTime()
    {
        return startTime;
    }
    public String getLocation()
    {
        return Location;
    }
    public String getContentDesc()
    {
        return contentDesc;
    }
    public String getPath()
    {
        return path;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    public void setLocation(String Location)
    {
        this.Location = Location;
    }
    public void setContentDesc(String contentDesc)
    {
        this.contentDesc = contentDesc;
    }
    public void setPath(String path)
    {
        this.path = path;
    }
    public String getType()
    {
        return Type;
    }
    public void setType(String Type)
    {
        this.Type = Type;
    }
    public String getTargetType()
    {
        return targetType;
    }
    public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }
    public String getTargetId()
    {
        return targetId;
    }
    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }
}
