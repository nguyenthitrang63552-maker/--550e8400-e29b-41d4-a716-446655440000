package com.ruoyi.system.domain.dto;

import java.io.Serializable;

/**
 * 图层启用状态明细 DTO。
 */
public class LayerStatusDetailDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer configId;

    private String layerCode;

    private String layerName;

    private Boolean isEnabled;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getConfigId()
    {
        return configId;
    }

    public void setConfigId(Integer configId)
    {
        this.configId = configId;
    }

    public String getLayerCode()
    {
        return layerCode;
    }

    public void setLayerCode(String layerCode)
    {
        this.layerCode = layerCode;
    }

    public String getLayerName()
    {
        return layerName;
    }

    public void setLayerName(String layerName)
    {
        this.layerName = layerName;
    }

    public Boolean getIsEnabled()
    {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
}
