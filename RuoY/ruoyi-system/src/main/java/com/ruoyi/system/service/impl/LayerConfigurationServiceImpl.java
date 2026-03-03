//{
//  "cells": [],
//  "metadata": {
//    "language_info": {
//      "name": "python"
//    }
//  },
//  "nbformat": 4,
//  "nbformat_minor": 2
//}
package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.LayerConfigurationDTO;
import com.ruoyi.system.service.ILayerConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 图层配置服务实现类
 *
 * @author ruoyi
 */
@Service
public class LayerConfigurationServiceImpl implements ILayerConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(LayerConfigurationServiceImpl.class);

    // 这里可以使用 Redis 或数据库来存储配置，示例中使用内存存储
    private LayerConfigurationDTO currentConfig = null;

    @Override
    public int saveLayerConfiguration(LayerConfigurationDTO dto) {
        try {
            log.info("保存图层配置: {}", dto);

            // TODO: 这里可以将配置保存到数据库或Redis
            // 示例：保存到内存
            this.currentConfig = dto;

            // 实际项目中可以这样保存：
            // 1. 保存到数据库表
            // 2. 保存到Redis缓存
            // 3. 保存到配置文件

            log.info("图层配置保存成功");
            return 1;
        } catch (Exception e) {
            log.error("保存图层配置失败", e);
            throw new RuntimeException("保存图层配置失败: " + e.getMessage());
        }
    }

    @Override
    public LayerConfigurationDTO getLayerConfiguration() {
        try {
            // 如果内存中没有配置，返回默认配置
            if (currentConfig == null) {
                return getDefaultConfiguration();
            }
            return currentConfig;
        } catch (Exception e) {
            log.error("获取图层配置失败", e);
            throw new RuntimeException("获取图层配置失败: " + e.getMessage());
        }
    }

    @Override
    public int resetLayerConfiguration() {
        try {
            log.info("重置图层配置为默认值");

            // 重置为默认配置
            this.currentConfig = getDefaultConfiguration();

            log.info("图层配置重置成功");
            return 1;
        } catch (Exception e) {
            log.error("重置图层配置失败", e);
            throw new RuntimeException("重置图层配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取默认配置
     *
     * @return 默认配置DTO
     */
    private LayerConfigurationDTO getDefaultConfiguration() {
        LayerConfigurationDTO defaultConfig = new LayerConfigurationDTO();
        defaultConfig.setField103("90");
        defaultConfig.setField105(Arrays.asList("使用透明背景"));
        defaultConfig.setField106("#00eeff");
        defaultConfig.setField107("#000000");
        defaultConfig.setField108("12");
        defaultConfig.setField109(Arrays.asList(
                "启用所有功能图层",
                "飞机雷达图图层",
                "仪表盘图层",
                "ADS-B应答数据面板图层",
                "导航图层"
        ));
        defaultConfig.setField110("10");
        defaultConfig.setField111(Arrays.asList("启用图层悬浮显示名称及状态提示"));
        defaultConfig.setField112(Arrays.asList("图层加载失败时显示弹窗提示"));
        defaultConfig.setField113(Arrays.asList("启用图层数据本地缓存（提升重复加载速度）"));
        defaultConfig.setField114("30");
        return defaultConfig;
    }
}
