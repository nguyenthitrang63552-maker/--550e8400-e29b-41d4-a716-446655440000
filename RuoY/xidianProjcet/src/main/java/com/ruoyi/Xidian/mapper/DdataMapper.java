package com.ruoyi.Xidian.mapper;

import com.ruoyi.Xidian.domain.DdataInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DdataMapper {
    /**
     * 查询数据列表
     * @param ddataInfo
     * @return
     */
    List<DdataInfo> selectDdataInfoList(DdataInfo ddataInfo);

    Integer insertDdataInfo(DdataInfo ddataInfo);

    Integer updateDdataInfo(DdataInfo ddataInfo);

    Integer deleteDdataInfos(List<Integer> ids);
}
