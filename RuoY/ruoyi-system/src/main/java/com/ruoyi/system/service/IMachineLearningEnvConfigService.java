package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.MachineLearningEnvConfigDTO;

/**
 * 机器学习环境配置 Service（极简版）
 */
public interface IMachineLearningEnvConfigService
{
    int save(MachineLearningEnvConfigDTO dto);

    MachineLearningEnvConfigDTO get();

    int reset();
}

