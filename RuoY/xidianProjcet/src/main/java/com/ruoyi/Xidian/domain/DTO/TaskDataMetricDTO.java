package com.ruoyi.Xidian.domain.DTO;

import java.math.BigDecimal;

public class TaskDataMetricDTO {
    private String metricCode;
    private String metricName;
    private BigDecimal recommendedValue;
    private BigDecimal fluctuationRate;
    private String unit;
    private Integer sortNo;

    public String getMetricCode() {
        return metricCode;
    }
    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }
    public String getMetricName() {
        return metricName;
    }
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
    public BigDecimal getRecommendedValue() {
        return recommendedValue;
    }
    public void setRecommendedValue(BigDecimal recommendedValue) {
        this.recommendedValue = recommendedValue;
    }
    public BigDecimal getFluctuationRate() {
        return fluctuationRate;
    }
    public void setFluctuationRate(BigDecimal fluctuationRate) {
        this.fluctuationRate = fluctuationRate;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Integer getSortNo() {
        return sortNo;
    }
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
