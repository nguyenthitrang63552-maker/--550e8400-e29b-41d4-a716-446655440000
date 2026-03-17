package com.ruoyi.framework.web.domain.server;

import com.ruoyi.common.utils.Arith;

/**
 * GPU相关信息
 *
 * @author ruoyi
 */
public class Gpu
{
    /**
     * GPU名称
     */
    private String name;

    /**
     * 厂商
     */
    private String vendor;

    /**
     * 当前使用率
     */
    private double usage;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    public double getUsage()
    {
        return Arith.round(usage, 2);
    }

    public void setUsage(double usage)
    {
        this.usage = usage;
    }
}
