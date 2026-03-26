package com.ruoyi.Xidian.domain;

import java.math.BigDecimal;
import java.util.List;

//每一个数据表
public class TaskDataGroup {
    private Long id;
    private Long taskId;
    private String groupCode;
    private String groupName;
    private Integer sortNo;

    private String dataName;
    private String outputType;
    private Long startTimeMs;
    private Long endTimeMs;
    private BigDecimal frequencyHz;//数据帧率
    private Boolean enabled;
    private String status;

    private List<TaskDataMetric> metricList;

    // getter/setter
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMetricList(List<TaskDataMetric> metricList) {
        this.metricList = metricList;
    }

    public List<TaskDataMetric> getMetricList() {
        return metricList;
    }

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

    public void setStartTimeMs(Long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public void setEndTimeMs(Long endTimeMs) {
        this.endTimeMs = endTimeMs;
    }

    public Long getEndTimeMs() {
        return endTimeMs;
    }

    public void setFrequencyHz(BigDecimal frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public BigDecimal getFrequencyHz() {
        return frequencyHz;
    }
}
