package com.ruoyi.Xidian.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

public class Task extends BaseEntity {
    private Long id;
    private String taskCode;
    private String taskName;
    private Integer projectId;
    private String experimentID;
    private String targetId;

    private String carrierType;
    private String motionModel;

    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private Vector3 startSpeed;
    private Attitude startAttitude;

    private String dataCategorySummary;
    private String targetType;
    private String status;
    private String path;

    private List<TaskDataGroup> dataGroups;

    public List<TaskDataGroup> getDataGroups() {
        return dataGroups;
    }

    public void setDataGroups(List<TaskDataGroup> dataGroups) {
        this.dataGroups = dataGroups;
    }

    public void addDataGroup(TaskDataGroup dataGroup) {
        dataGroups.add(dataGroup);
    }

    public void removeDataGroup(TaskDataGroup dataGroup) {
        dataGroups.remove(dataGroup);
    }

    public void clearDataGroups() {
        dataGroups.clear();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setMotionModel(String motionModel) {
        this.motionModel = motionModel;
    }

    public String getMotionModel() {
        return motionModel;
    }



    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinateAltitude(BigDecimal altitude) {
        this.startCoordinate.setAltitude(altitude);
    }

    public BigDecimal getStartCoordinateAltitude() {
        return startCoordinate.getAltitude();
    }

    public void setEndCoordinate(Coordinate endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }

    public void setStartSpeed(Vector3 startSpeed) {
        this.startSpeed = startSpeed;
    }

    public Vector3 getStartSpeed() {
        return startSpeed;
    }

    public void setStartAttitude(Attitude startAttitude) {
        this.startAttitude = startAttitude;
    }

    public Attitude getStartAttitude() {
        return startAttitude;
    }

    public void setDataCategorySummary(String dataCategorySummary) {
        this.dataCategorySummary = dataCategorySummary;
    }

    public String getDataCategorySummary() {
        return dataCategorySummary;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setExperimentID(String experimentID) {
        this.experimentID = experimentID;
    }

    public String getExperimentID() {
        return experimentID;
    }
}
