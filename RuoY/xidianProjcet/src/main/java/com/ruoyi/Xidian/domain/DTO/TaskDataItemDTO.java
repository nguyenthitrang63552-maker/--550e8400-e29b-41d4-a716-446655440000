package com.ruoyi.Xidian.domain.DTO;

import java.math.BigDecimal;
import java.util.List;

public class TaskDataItemDTO {
    private String dataName;
    private String outputType;
    private Long startTimeMs;
    private Long endTimeMs;
    private BigDecimal frequencyHz;
    private List<TaskDataMetricDTO> metrics;

    public String getDataName() {
        return dataName;
    }
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    public String getOutputType() {
        return outputType;
    }
    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }
    public Long getStartTimeMs() {
        return startTimeMs;
    }
    public void setStartTimeMs(Long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }
    public Long getEndTimeMs() {
        return endTimeMs;
    }
    public void setEndTimeMs(Long endTimeMs) {
        this.endTimeMs = endTimeMs;
    }
    public BigDecimal getFrequencyHz() {
        return frequencyHz;
    }
    public void setFrequencyHz(BigDecimal frequencyHz) {
        this.frequencyHz = frequencyHz;
    }
    public List<TaskDataMetricDTO> getMetrics() {
        return metrics;
    }
    public void setMetrics(List<TaskDataMetricDTO> metrics) {
        this.metrics = metrics;
    }
}
