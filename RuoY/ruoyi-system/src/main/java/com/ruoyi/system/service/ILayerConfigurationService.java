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
package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.LayerConfigurationDTO;

/**
 * 图层配置服务接口
 *
 * @author ruoyi
 */
public interface ILayerConfigurationService {

    /**
     * 保存图层配置
     *
     * @param dto 图层配置DTO
     * @return 结果
     */
    int saveLayerConfiguration(LayerConfigurationDTO dto);

    /**
     * 获取图层配置
     *
     * @return 图层配置DTO
     */
    LayerConfigurationDTO getLayerConfiguration();

    /**
     * 重置图层配置为默认值
     *
     * @return 结果
     */
    int resetLayerConfiguration();
}

