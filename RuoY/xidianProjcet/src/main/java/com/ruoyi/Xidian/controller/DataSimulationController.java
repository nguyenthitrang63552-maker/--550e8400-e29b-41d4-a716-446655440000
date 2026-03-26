package com.ruoyi.Xidian.controller;

import com.ruoyi.Xidian.domain.Task;
import com.ruoyi.Xidian.service.SimulationTaskService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/data/simulation")
public class DataSimulationController {
    private final SimulationTaskService simulationTaskService;
    public DataSimulationController(SimulationTaskService simulationTaskService) {
        this.simulationTaskService = simulationTaskService;
    }

    @GetMapping("/task/list")
    public TableDataInfo taskList(Task task) {
        startPage();
        List<Task> tasks = new ArrayList<>();
        try{
            tasks = simulationTaskService.selectTaskList(task);
        }catch (Exception e){
            throw new RuntimeException("获取任务列表失败");
        }
        return new TableDataInfo(tasks, tasks.size());
    }

    @PostMapping("/task/submit")
    public AjaxResult submitTask(Task task) {
        AjaxResult ajaxResult = AjaxResult.success();
        try{
            simulationTaskService.insert(task);
        }catch (Exception e){
            AjaxResult.error("提交任务失败");
        }
        return AjaxResult.success();
    }

    @GetMapping("/task/{id}")
    public AjaxResult taskDetail(@PathVariable Long id) {
        AjaxResult ajaxResult = AjaxResult.success();
        try{
            Task task = simulationTaskService.selectById(id);
            if(task == null){
                throw new RuntimeException("任务不存在");
            }
            ajaxResult.put("data", task);
        }catch (Exception e){
            throw new RuntimeException("获取任务详情失败");
        }
        return ajaxResult;
    }

    @DeleteMapping("/task/{id}")
    public AjaxResult deleteTask(@PathVariable Long id) {
        AjaxResult ajaxResult = AjaxResult.success();
        try{
            simulationTaskService.deleteTask(id);
        }catch (Exception e){
            throw new RuntimeException("删除任务失败");
        }
        return ajaxResult;
    }
}
