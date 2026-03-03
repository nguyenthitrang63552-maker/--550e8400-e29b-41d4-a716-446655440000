package com.ruoyi.Xidian.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class DTargetInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 目标唯一标识（UUID） */
    private String targetId;

    /** 目标名称 */
    @Excel(name = "目标名称")
    private String targetName;

    /** 目标类型 */
    @Excel(name = "目标类型")
    private String targetType;

    /** 身份标识 */
    @Excel(name = "身份标识")
    private String targetIdentity;

    /** 载机自身信息描述 */
    @Excel(name = "载机自身信息描述")
    private String targetDesc;

    /** 载机传感器描述 */
    @Excel(name = "载机传感器描述")
    private String sensorPlatformInfo;

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }

    public String getTargetType()
    {
        return targetType;
    }

    public void setTargetIdentity(String targetIdentity)
    {
        this.targetIdentity = targetIdentity;
    }

    public String getTargetIdentity()
    {
        return targetIdentity;
    }

    public void setTargetDesc(String targetDesc)
    {
        this.targetDesc = targetDesc;
    }

    public String getTargetDesc()
    {
        return targetDesc;
    }

    public void setSensorPlatformInfo(String sensorPlatformInfo)
    {
        this.sensorPlatformInfo = sensorPlatformInfo;
    }

    public String getSensorPlatformInfo()
    {
        return sensorPlatformInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("targetId", getTargetId())
                .append("targetName", getTargetName())
                .append("targetType", getTargetType())
                .append("targetIdentity", getTargetIdentity())
                .append("targetDesc", getTargetDesc())
                .append("sensorPlatformInfo", getSensorPlatformInfo())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
