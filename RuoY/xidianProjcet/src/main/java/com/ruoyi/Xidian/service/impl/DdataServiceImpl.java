package com.ruoyi.Xidian.service.impl;

import com.ruoyi.Xidian.domain.DdataInfo;
import com.ruoyi.Xidian.mapper.DdataMapper;
import com.ruoyi.Xidian.service.IDdataService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class DdataServiceImpl implements IDdataService {
    @Autowired
    private DdataMapper ddataMapper;

    @Override
    public List<DdataInfo> selectDdataInfoList(DdataInfo ddataInfo) {
        return ddataMapper.selectDdataInfoList(ddataInfo);
    }

    @Override
    public DdataInfo selectDdataInfoByDdataId(Integer id) {
        DdataInfo ddataInfo = new DdataInfo();
        ddataInfo.setId(id);
        return ddataMapper.selectDdataInfoList(ddataInfo).get(0);
    }

    @Override
    public Integer insertDdataInfo(DdataInfo ddataInfo) {
        ddataInfo.setWorkStatus("完成");
        ddataInfo.setSampleFrequency(1000);
        ddataInfo.setCreateBy(SecurityUtils.getUsername());
        ddataInfo.setDataFilePath("/"+ddataInfo.getDataName());
        return ddataMapper.insertDdataInfo(ddataInfo);
    }

    @Override
    public Integer updateDdataInfo(DdataInfo ddataInfo) {
        return ddataMapper.updateDdataInfo(ddataInfo);
    }

    @Override
    public Integer deleteDdataInfos(List<Integer> ids) {
        return ddataMapper.deleteDdataInfos(ids);
    }
}
