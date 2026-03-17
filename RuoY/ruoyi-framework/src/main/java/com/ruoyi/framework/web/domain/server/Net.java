package com.ruoyi.framework.web.domain.server;

import com.ruoyi.common.utils.Arith;

/**
 * 网络吞吐量信息
 *
 * @author ruoyi
 */
public class Net
{
    /**
     * 主要网卡名称
     */
    private String name;

    /**
     * 接收速率，单位 B/s
     */
    private double recvRate;

    /**
     * 发送速率，单位 B/s
     */
    private double sentRate;

    /**
     * 总吞吐量，单位 B/s
     */
    private double totalRate;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getRecvRate()
    {
        return Arith.round(recvRate, 2);
    }

    public void setRecvRate(double recvRate)
    {
        this.recvRate = recvRate;
    }

    public double getSentRate()
    {
        return Arith.round(sentRate, 2);
    }

    public void setSentRate(double sentRate)
    {
        this.sentRate = sentRate;
    }

    public double getTotalRate()
    {
        return Arith.round(totalRate, 2);
    }

    public void setTotalRate(double totalRate)
    {
        this.totalRate = totalRate;
    }
}
