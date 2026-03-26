package com.ruoyi.Xidian.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

//指标明细
public class

TaskDataMetric {
    // 这一行数据的ID
    private Long id;

    // 属于哪个数据项
    private Long dataGroupId;


    // 指标编码（前端一般不显示）
    private String metricCode;

    // 新数据i / 新数据j / 新数据k
    private String metricName;

    // 推荐值
    private BigDecimal recommendedValue;

    // 波动阈
    private BigDecimal fluctuationRate;

    // 单位
    private String unit;

    // 行顺序
    private Integer sortNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //getters
    public Long getId() {
        return id;
    }
    public Long getDataGroupId() {
        return dataGroupId;
    }
    public String getMetricCode() {
        return metricCode;
    }
    public String getMetricName() {
        return metricName;
    }
    public BigDecimal getRecommendedValue() {
        return recommendedValue;
    }
    public BigDecimal getFluctuationRate() {
        return fluctuationRate;
    }
    public String getUnit() {
        return unit;
    }
    public Integer getSortNo() {
        return sortNo;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setDataGroupId(Long dataGroupId) {
        this.dataGroupId = dataGroupId;
    }
    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
    public void setRecommendedValue(BigDecimal recommendedValue) {
        this.recommendedValue = recommendedValue;
    }
    public void setFluctuationRate(BigDecimal fluctuationRate) {
        this.fluctuationRate = fluctuationRate;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
