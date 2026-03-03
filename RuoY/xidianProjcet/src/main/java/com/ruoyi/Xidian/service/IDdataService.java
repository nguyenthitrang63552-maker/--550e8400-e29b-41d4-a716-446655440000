package com.ruoyi.Xidian.service;

import com.ruoyi.Xidian.domain.DdataInfo;

import java.util.List;

public interface IDdataService {
    /**
     * 查询数据列表
     * @param ddataInfo
     * @return
     */
    List<DdataInfo> selectDdataInfoList(DdataInfo ddataInfo);

    DdataInfo selectDdataInfoByDdataId(Integer id);

    Integer insertDdataInfo(DdataInfo ddataInfo);
    Integer updateDdataInfo(DdataInfo ddataInfo);

    Integer deleteDdataInfos(List<Integer> ids);
}
