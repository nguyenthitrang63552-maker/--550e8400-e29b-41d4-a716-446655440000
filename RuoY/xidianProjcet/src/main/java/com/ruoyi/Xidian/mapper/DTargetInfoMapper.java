package com.ruoyi.Xidian.mapper;

import java.util.List;
import com.ruoyi.Xidian.domain.DTargetInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测试目标信息Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-24
 */
@Mapper
public interface DTargetInfoMapper
{
    /**
     * 查询测试目标信息
     *
     * @param targetId 测试目标信息主键
     * @return 测试目标信息
     */
    public DTargetInfo selectDTargetInfoByTargetId(String targetId);

    /**
     * 查询测试目标信息列表
     *
     * @param dTargetInfo 测试目标信息
     * @return 测试目标信息集合
     */
    public List<DTargetInfo> selectDTargetInfoList(DTargetInfo dTargetInfo);

    /**
     * 新增测试目标信息
     *
     * @param dTargetInfo 测试目标信息
     * @return 结果
     */
    public int insertDTargetInfo(DTargetInfo dTargetInfo);

    /**
     * 修改测试目标信息
     *
     * @param dTargetInfo 测试目标信息
     * @return 结果
     */
    public int updateDTargetInfo(DTargetInfo dTargetInfo);

    /**
     * 删除测试目标信息
     *
     * @param targetId 测试目标信息主键
     * @return 结果
     */
    public int deleteDTargetInfoByTargetId(String targetId);

    /**
     * 批量删除测试目标信息
     *
     * @param targetIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDTargetInfoByTargetIds(String[] targetIds);

    List<String> selectTargetTypeList();
}
