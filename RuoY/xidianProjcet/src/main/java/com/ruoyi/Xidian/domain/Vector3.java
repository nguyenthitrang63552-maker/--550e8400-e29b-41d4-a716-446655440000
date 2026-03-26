package com.ruoyi.Xidian.domain;

import java.math.BigDecimal;

//速度向量
public class Vector3 {
    //经
    private BigDecimal x;
    //纬
    private BigDecimal y;
    //高
    private BigDecimal z;

    // getter/setter
    public Vector3() {
    }
    public Vector3(BigDecimal x, BigDecimal y, BigDecimal z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setX(BigDecimal x) {
        this.x = x;
    }
    public void setY(BigDecimal y) {
        this.y = y;
    }
    public void setZ(BigDecimal z) {
        this.z = z;
    }
    public BigDecimal getX() {
        return x;
    }
    public BigDecimal getY() {
        return y;
    }
    public BigDecimal getZ() {
        return z;
    }
}