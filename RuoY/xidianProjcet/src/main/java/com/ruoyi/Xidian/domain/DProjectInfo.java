package com.ruoyi.Xidian.domain;

import com.ruoyi.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

/**
 * 项目信息对象 d_project_info
 * 
 * @author ruoyi
 * @date 2026-01-23
 */
public class DProjectInfo extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目描述 */
    @Excel(name = "项目描述")
    private String projectContentDesc;

    /** 文件夹路径 */
    @Excel(name = "文件夹路径")
    private String path;

    /** 项目完整路径 */
    private String fullPath;

    private String type;

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }
    public void setProjectContentDesc(String projectContentDesc) 
    {
        this.projectContentDesc = projectContentDesc;
    }

    public String getProjectContentDesc() 
    {
        return projectContentDesc;
    }
    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
    public void setFullPath(String fullPath)
    {
        this.fullPath = fullPath;
    }

    public String getFullPath()
    {
        return fullPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("projectContentDesc", getProjectContentDesc())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("path", getPath())
            .toString();
    }
}
