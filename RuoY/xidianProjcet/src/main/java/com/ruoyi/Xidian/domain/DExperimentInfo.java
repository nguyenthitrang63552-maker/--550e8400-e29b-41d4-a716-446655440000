package com.ruoyi.Xidian.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

/**
 * 试验信息主对象 d_experiment_info
 * 
 * @author ruoyi
 * @date 2026-01-23
 */
public class DExperimentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 试验编号（UUID） */
    @Excel(name = "试验编号", readConverterExp = "U=UID")
    private String experimentId;

    /** 目标编号（外键） */
    private String targetId;

    /** 试验名称 */
    @Excel(name = "试验名称")
    private String experimentName;

    /** 目标类型 */
    private String targetType;

    /** 项目代码（外键） */
    @Excel(name = "项目代码", readConverterExp = "外=键")
    private Long projectId;

    private String projectName;

    /** 试验开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "试验开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 试验地点 */
    @Excel(name = "试验地点")
    private String location;

    private String path;
    /** 平台配置信息 */
    private String platformConfig;

    /** 试验内容描述 */
    @Excel(name = "试验内容描述")
    private String contentDesc;
    private String type;

    /** 父菜单名称 */
    private String parentName;

    /** 父菜单ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    public void setExperimentId(String experimentId) 
    {
        this.experimentId = experimentId;
    }

    public String getExperimentId() 
    {
        return experimentId;
    }

    public void setTargetId(String targetId) 
    {
        this.targetId = targetId;
    }

    public String getTargetId() 
    {
        return targetId;
    }

    public void setExperimentName(String experimentName) 
    {
        this.experimentName = experimentName;
    }

    public String getExperimentName() 
    {
        return experimentName;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setPlatformConfig(String platformConfig) 
    {
        this.platformConfig = platformConfig;
    }

    public String getPlatformConfig() 
    {
        return platformConfig;
    }

    public void setContentDesc(String contentDesc) 
    {
        this.contentDesc = contentDesc;
    }

    public String getContentDesc() 
    {
        return contentDesc;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }
    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path=path;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
     public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }
    public String getTargetType()
    {
        return targetType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("experimentId", getExperimentId())
            .append("targetId", getTargetId())
            .append("experimentName", getExperimentName())
            .append("targetType", getTargetType())
            .append("projectId", getProjectId()).append("projectName", getProjectName())
            .append("startTime", getStartTime())
            .append("location", getLocation())
            .append("platformConfig", getPlatformConfig())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("contentDesc", getContentDesc())
            .toString();
    }
}
