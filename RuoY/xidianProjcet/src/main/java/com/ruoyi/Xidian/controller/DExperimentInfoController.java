package com.ruoyi.Xidian.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.Xidian.domain.DExperimentInfo;
import com.ruoyi.Xidian.domain.DProjectInfo;
import com.ruoyi.Xidian.domain.DTargetInfo;
import com.ruoyi.Xidian.domain.TreeTableVo;
import com.ruoyi.Xidian.service.IDExperimentInfoService;
import com.ruoyi.Xidian.service.IDProjectInfoService;
import com.ruoyi.Xidian.service.IDTargetInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;

@RestController
@RequestMapping("/data/info")
public class DExperimentInfoController extends BaseController
{
    @Autowired
    private IDExperimentInfoService dExperimentInfoService;

    @Autowired
    private IDProjectInfoService dProjectInfoService;

    @Autowired
    private IDTargetInfoService dTargetInfoService;

    @PreAuthorize("@ss.hasPermi('data:info:list')")
    @GetMapping("/list")
    public AjaxResult list(TreeTableVo treeTableVo)
    {
        List<TreeTableVo> treeTableVos =new ArrayList<>();
        try {
            treeTableVos = dExperimentInfoService.selectDExperimentInfoTree(treeTableVo);
        }catch (Exception e){
            throw buildSafeException("查询试验信息失败", e);
        }
        return success(treeTableVos);
    }

    @PreAuthorize("@ss.hasPermi('data:info:export')")
    @Log(title = "导出试验信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DExperimentInfo dExperimentInfo)
    {
        try{
            List<DExperimentInfo> list = dExperimentInfoService.selectDExperimentInfoList(dExperimentInfo);
            ExcelUtil<DExperimentInfo> util = new ExcelUtil<DExperimentInfo>(DExperimentInfo.class);
            util.exportExcel(response, list, "试验信息主数据");
        }
        catch (Exception e){
            throw buildSafeException("导出试验信息失败", e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:query')")
    @GetMapping(value = {"/", "/{infoId}"})
    public AjaxResult getInfo(@PathVariable(value = "infoId", required = false) String infoId, @RequestParam String type)
    {
        validateInfoType(type);
        AjaxResult ajax = AjaxResult.success();
        if ("project".equals(type))
        {
            if (infoId == null || infoId.trim().isEmpty())
            {
                throw new ServiceException("项目id不能为空");
            }
            try{
                return success(dProjectInfoService.selectDProjectInfoByProjectId(Long.valueOf(infoId)));
            }
            catch (Exception e){
                throw buildSafeException("查询项目信息失败", e);
            }
        }
        try
        {
            ajax.put("projects", dProjectInfoService.selectAllDProjectInfo());
            ajax.put("targetTypes", dTargetInfoService.selectDTargetInfoList(null));
            if (infoId != null && !infoId.trim().isEmpty())
            {
                ajax.put(AjaxResult.DATA_TAG, dExperimentInfoService.selectDExperimentInfoByExperimentId(infoId));
            }
            return ajax;
        }
        catch (Exception e)
        {
            throw buildSafeException("查询试验信息失败", e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:add')")
    @Log(title = "添加项目或试验信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TreeTableVo treeTableVo)
    {
        validateInfoType(treeTableVo.getType());
        if ("project".equals(treeTableVo.getType()))
        {
            DProjectInfo dProjectInfo = new DProjectInfo();
            dProjectInfo.setProjectName(treeTableVo.getName());
            dProjectInfo.setCreateBy(SecurityUtils.getUsername());
            dProjectInfo.setProjectContentDesc(treeTableVo.getContentDesc());
            try{
                return toAjax(dProjectInfoService.insertDProjectInfo(dProjectInfo));
            }
            catch (Exception e){
                throw buildSafeException("添加项目信息失败", e);
            }
        }

        DExperimentInfo dExperimentInfo = new DExperimentInfo();
        dExperimentInfo.setExperimentId(UUID.randomUUID().toString());
        dExperimentInfo.setExperimentName(treeTableVo.getName());
        dExperimentInfo.setCreateBy(SecurityUtils.getUsername());
        dExperimentInfo.setContentDesc(treeTableVo.getContentDesc());
        dExperimentInfo.setLocation(treeTableVo.getLocation());
        dExperimentInfo.setTargetId(treeTableVo.getTargetId());
        dExperimentInfo.setTargetType(treeTableVo.getTargetType());
        dExperimentInfo.setProjectId(treeTableVo.getParentId());
        dExperimentInfo.setStartTime(treeTableVo.getStartTime());
        dExperimentInfo.setPath(treeTableVo.getPath());
        try{
            return toAjax(dExperimentInfoService.insertDExperimentInfo(dExperimentInfo));
        }
        catch (Exception e){
            throw buildSafeException("添加试验信息失败", e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:edit')")
    @Log(title = "修改项目或试验信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TreeTableVo treeTableVo)
    {
        try{
            return updateInfoByType(treeTableVo.getId(), treeTableVo.getType(), treeTableVo);
        }
        catch (ServiceException e){
            throw e;
        }
        catch (Exception e){
            throw buildSafeException(buildOperateErrorMessage("修改", treeTableVo == null ? null : treeTableVo.getType()), e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:edit')")
    @Log(title = "修改项目或试验信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{infoId}")
    public AjaxResult editById(@PathVariable String infoId, @RequestParam String type, @RequestBody TreeTableVo treeTableVo)
    {
        try{
            return updateInfoByType(infoId, type, treeTableVo);
        }
        catch (ServiceException e){
            throw e;
        }
        catch (Exception e){
            throw buildSafeException(buildOperateErrorMessage("修改", type), e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:remove')")
    @Log(title = "删除项目或试验信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoId}")
    public AjaxResult removeById(@PathVariable String infoId, @RequestParam String type)
    {
        try{
            deleteInfoByType(infoId, type);
            return AjaxResult.success();
        }
        catch (ServiceException e){
            throw e;
        }
        catch (Exception e){
            throw buildSafeException(buildOperateErrorMessage("删除", type), e);
        }
    }

    @PreAuthorize("@ss.hasPermi('data:info:remove')")
    @Log(title = "删除项目或试验信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{experimentIds}/project/{projectIds}")
    public AjaxResult remove(@PathVariable String[] experimentIds, @PathVariable Long[] projectIds)
    {
        List<String> errors = new ArrayList<>();
        boolean hasExperimentIds = experimentIds != null && experimentIds.length > 0;
        boolean hasProjectIds = projectIds != null && projectIds.length > 0;
        if (hasProjectIds || hasExperimentIds)
        {
            if (hasExperimentIds)
            {
                try
                {
                    dExperimentInfoService.deleteDExperimentInfoByExperimentIds(experimentIds);
                }
                catch (Exception e)
                {
                    errors.add("删除试验信息失败");
                }
            }
            if (hasProjectIds)
            {
                try
                {
                    dProjectInfoService.deleteDProjectInfoByProjectIds(projectIds);
                }
                catch (Exception e)
                {
                    errors.add("删除项目信息失败");
                }
            }
            if (!errors.isEmpty())
            {
                return AjaxResult.error(String.join("；", errors));
            }
            return AjaxResult.success();
        }
        return AjaxResult.error("请选择要删除的数据");
    }

    @GetMapping("/experimentInfos")
    public TableDataInfo getExperimentInfos(DExperimentInfo dExperimentInfo)
    {
        startPage();
        try{
            List<DExperimentInfo> dExperimentInfos = dExperimentInfoService.selectDExperimentInfoList(dExperimentInfo);
            return getDataTable(dExperimentInfos);
        }
        catch (Exception e){
            throw buildSafeException("查询试验信息失败", e);
        }
    }

    private AjaxResult updateInfoByType(String infoId, String type, TreeTableVo treeTableVo)
    {
        if (infoId == null || infoId.trim().isEmpty())
        {
            throw new ServiceException("试验信息id不能为空");
        }
        validateInfoType(type);
        treeTableVo.setId(infoId);
        treeTableVo.setType(type);
        if ("project".equals(type))
        {
            try{
                DProjectInfo dProjectInfo = new DProjectInfo();
                dProjectInfo.setProjectId(Long.valueOf(treeTableVo.getId()));
                dProjectInfo.setProjectName(treeTableVo.getName());
                dProjectInfo.setCreateTime(treeTableVo.getCreateTime());
                dProjectInfo.setProjectContentDesc(treeTableVo.getContentDesc());
                dProjectInfo.setPath(treeTableVo.getPath());
                return toAjax(dProjectInfoService.updateDProjectInfo(dProjectInfo));
            }
            catch (Exception e){
                throw buildSafeException("修改项目失败", e);
            }
        }

        DExperimentInfo dExperimentInfo = new DExperimentInfo();
        dExperimentInfo.setExperimentId(treeTableVo.getId());
        dExperimentInfo.setExperimentName(treeTableVo.getName());
        dExperimentInfo.setTargetId(treeTableVo.getTargetId());
        dExperimentInfo.setProjectId(treeTableVo.getParentId());
        dExperimentInfo.setLocation(treeTableVo.getLocation());
        dExperimentInfo.setCreateTime(treeTableVo.getCreateTime());
        dExperimentInfo.setContentDesc(treeTableVo.getContentDesc());
        dExperimentInfo.setStartTime(treeTableVo.getStartTime());
        dExperimentInfo.setPath(treeTableVo.getPath());
        try{
            return toAjax(dExperimentInfoService.updateDExperimentInfo(dExperimentInfo));
        }
        catch (Exception e){
            throw buildSafeException("修改试验失败", e);
        }
    }

    private void deleteInfoByType(String infoId, String type)
    {
        if (infoId == null || infoId.trim().isEmpty())
        {
            throw new ServiceException("试验信息id不能为空");
        }
        validateInfoType(type);
        if ("project".equals(type))
        {
            try{
                dProjectInfoService.deleteDProjectInfoByProjectId(Long.valueOf(infoId));
            }catch (Exception e){
                throw buildSafeException("删除项目失败", e);
            }
            return;
        }
        try{
            dExperimentInfoService.deleteDExperimentInfoByExperimentId(infoId);
        }catch (Exception e){
            throw buildSafeException("删除试验失败", e);
        }
    }

    private void validateInfoType(String type)
    {
        if (!"project".equals(type) && !"experiment".equals(type))
        {
            throw new ServiceException("试验信息类型错误");
        }
    }

    private String buildOperateErrorMessage(String action, String type)
    {
        if ("project".equals(type))
        {
            return action + "项目信息失败";
        }
        if ("experiment".equals(type))
        {
            return action + "试验信息失败";
        }
        return action + "信息失败";
    }

    private ServiceException buildSafeException(String message, Exception e)
    {
        ServiceException serviceException = new ServiceException(message);
        serviceException.initCause(e);
        return serviceException;
    }

    @GetMapping("/path/{experimentId}")
    public AjaxResult getExperimentPathById(@PathVariable String experimentId)
    {
        if (experimentId == null || experimentId.trim().isEmpty())
        {
            throw new ServiceException("试验id不能为空");
        }
        String path = "";
        AjaxResult ajaxResult = AjaxResult.success();
        try{
            path = dExperimentInfoService.getExperimentPath(experimentId);
        }catch (Exception e){
            throw buildSafeException("获取试验路径失败", e);
        }
        ajaxResult.put("data", path);
        return ajaxResult;
    }
}
