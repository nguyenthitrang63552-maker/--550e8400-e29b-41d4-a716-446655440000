package com.ruoyi.Xidian.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Attitude implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal roll;
    private BigDecimal yaw;
    private BigDecimal pitch;

    public Attitude() {
    }

    public Attitude(BigDecimal roll, BigDecimal yaw, BigDecimal pitch) {
        this.roll = roll;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // getter/setter
    public void setRoll(BigDecimal roll) {
        this.roll = roll;
    }
    public void setYaw(BigDecimal yaw) {
        this.yaw = yaw;
    }
    public void setPitch(BigDecimal pitch) {
        this.pitch = pitch;
    }
    public BigDecimal getRoll() {
        return roll;
    }
    public BigDecimal getYaw() {
        return yaw;
    }
    public BigDecimal getPitch() {
        return pitch;
    }
}