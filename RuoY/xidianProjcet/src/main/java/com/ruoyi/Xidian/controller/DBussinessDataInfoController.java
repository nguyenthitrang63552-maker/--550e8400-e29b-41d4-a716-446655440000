package com.ruoyi.Xidian.controller;

import com.ruoyi.Xidian.domain.DdataInfo;
import com.ruoyi.Xidian.service.IDExperimentInfoService;
import com.ruoyi.Xidian.service.IDdataService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/bussiness")
public class DBussinessDataInfoController extends BaseController {
    @Autowired
    private IDExperimentInfoService dExperimentInfoService;
    @Autowired
    private IDdataService ddataService;

    @GetMapping("/experimentInfoTree")
    public AjaxResult getDExperimentInfoTree()
    {
        return AjaxResult.success(dExperimentInfoService.getExperimentInfoTree());
    }

    @GetMapping("/datalist")
    public TableDataInfo getDDataInfoList(DdataInfo ddataInfo)
    {
        startPage();
        return getDataTable(ddataService.selectDdataInfoList(ddataInfo));
    }
    @GetMapping("/{id}")
    public AjaxResult getDDataInfoByDdataId(@PathVariable Integer id)
    {
        return AjaxResult.success(ddataService.selectDdataInfoByDdataId(id));
    }

    @PostMapping("/insert")
    public AjaxResult insertDDataInfo(@RequestBody DdataInfo ddataInfo)
    {
        return AjaxResult.success(ddataService.insertDdataInfo(ddataInfo));
    }

    @PutMapping("/update")
    public AjaxResult updateDDataInfo(@RequestBody DdataInfo ddataInfo)
    {
        return AjaxResult.success(ddataService.updateDdataInfo(ddataInfo));
    }

    @DeleteMapping("/delete")
    public AjaxResult deleteDdataInfos(@RequestBody List<Integer> ids){
        return AjaxResult.success(ddataService.deleteDdataInfos(ids));
    }
}
