package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.LayerConfigurationDTO;
import com.ruoyi.system.service.ILayerConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 图层配置Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/layerConfiguration")
public class LayerConfigurationController extends BaseController {

    @Autowired
    private ILayerConfigurationService layerConfigurationService;

    /**
     * 保存图层配置
     */
    @PreAuthorize("@ss.hasPermi('system:layerConfiguration:save')")
    @Log(title = "图层配置", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody LayerConfigurationDTO dto) {
        // 参数校验
        if (StringUtils.isBlank(dto.getField103())) {
            return AjaxResult.error("图层默认透明度不能为空");
        }
        if (StringUtils.isBlank(dto.getField106())) {
            return AjaxResult.error("图层背景色不能为空");
        }
        if (StringUtils.isBlank(dto.getField107())) {
            return AjaxResult.error("全局文字颜色不能为空");
        }
        if (StringUtils.isBlank(dto.getField108())) {
            return AjaxResult.error("全局文字大小不能为空");
        }
        if (dto.getField109() == null || dto.getField109().isEmpty()) {
            return AjaxResult.error("图层启用状态不能为空");
        }
        if (StringUtils.isBlank(dto.getField110())) {
            return AjaxResult.error("图层刷新频率不能为空");
        }
        if (StringUtils.isBlank(dto.getField114())) {
            return AjaxResult.error("缓存过期时间不能为空");
        }

        // 数值校验
        try {
            Integer.parseInt(dto.getField103());
            Integer.parseInt(dto.getField108());
            Integer.parseInt(dto.getField110());
            Integer.parseInt(dto.getField114());
        } catch (NumberFormatException e) {
            return AjaxResult.error("数值格式不正确");
        }

        // 颜色格式校验（简单校验，实际可以更严格）
        if (!dto.getField106().matches("^#[0-9A-Fa-f]{6}$")) {
            return AjaxResult.error("图层背景色格式不正确，应为十六进制颜色值（如：#00eeff）");
        }
        if (!dto.getField107().matches("^#[0-9A-Fa-f]{6}$")) {
            return AjaxResult.error("全局文字颜色格式不正确，应为十六进制颜色值（如：#000000）");
        }

        int result = layerConfigurationService.saveLayerConfiguration(dto);
        return result > 0 ? AjaxResult.success("保存成功") : AjaxResult.error("保存失败");
    }

    /**
     * 获取图层配置
     */
    @PreAuthorize("@ss.hasPermi('system:layerConfiguration:query')")
    @GetMapping("/get")
    public AjaxResult get() {
        LayerConfigurationDTO config = layerConfigurationService.getLayerConfiguration();
        return AjaxResult.success(config);
    }

    /**
     * 重置图层配置为默认值
     */
    @PreAuthorize("@ss.hasPermi('system:layerConfiguration:reset')")
    @Log(title = "图层配置", businessType = BusinessType.UPDATE)
    @PostMapping("/reset")
    public AjaxResult reset() {
        int result = layerConfigurationService.resetLayerConfiguration();
        return result > 0 ? AjaxResult.success("重置成功") : AjaxResult.error("重置失败");
    }
}
