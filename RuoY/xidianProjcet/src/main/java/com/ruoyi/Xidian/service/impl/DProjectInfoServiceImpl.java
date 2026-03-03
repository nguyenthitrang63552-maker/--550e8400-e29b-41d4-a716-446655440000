package com.ruoyi.Xidian.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.Xidian.mapper.DProjectInfoMapper;
import com.ruoyi.Xidian.domain.DProjectInfo;
import com.ruoyi.Xidian.service.IDProjectInfoService;

/**
 * 项目信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-23
 */
@Service
public class DProjectInfoServiceImpl implements IDProjectInfoService 
{
    @Autowired
    private DProjectInfoMapper dProjectInfoMapper;

    /**
     * 查询项目信息
     * 
     * @param projectId 项目信息主键
     * @return 项目信息
     */
    @Override
    public DProjectInfo selectDProjectInfoByProjectId(Long projectId)
    {
        return dProjectInfoMapper.selectDProjectInfoByProjectId(projectId);
    }

    /**
     * 查询项目信息列表
     * 
     * @param dProjectInfo 项目信息
     * @return 项目信息
     */
    @Override
    public List<DProjectInfo> selectDProjectInfoList(DProjectInfo dProjectInfo)
    {
        return dProjectInfoMapper.selectDProjectInfoList(dProjectInfo);
    }

    /**
     * 新增项目信息
     * 
     * @param dProjectInfo 项目信息
     * @return 结果
     */
    @Override
    public int insertDProjectInfo(DProjectInfo dProjectInfo)
    {
        dProjectInfo.setCreateTime(DateUtils.getNowDate());
        return dProjectInfoMapper.insertDProjectInfo(dProjectInfo);
    }

    /**
     * 修改项目信息
     * 
     * @param dProjectInfo 项目信息
     * @return 结果
     */
    @Override
    public int updateDProjectInfo(DProjectInfo dProjectInfo)
    {
        dProjectInfo.setUpdateTime(DateUtils.getNowDate());
        return dProjectInfoMapper.updateDProjectInfo(dProjectInfo);
    }

    /**
     * 批量删除项目信息
     * 
     * @param projectIds 需要删除的项目信息主键
     * @return 结果
     */
    @Override
    public int deleteDProjectInfoByProjectIds(Long[] projectIds)
    {
        return dProjectInfoMapper.deleteDProjectInfoByProjectIds(projectIds);
    }

    /**
     * 删除项目信息信息
     * 
     * @param projectId 项目信息主键
     * @return 结果
     */
    @Override
    public int deleteDProjectInfoByProjectId(Long projectId)
    {
        return dProjectInfoMapper.deleteDProjectInfoByProjectId(projectId);
    }

    @Override
    public List<DProjectInfo> selectAllDProjectInfo(){
        return dProjectInfoMapper.selectAllDProjectInfo();
    }
}
