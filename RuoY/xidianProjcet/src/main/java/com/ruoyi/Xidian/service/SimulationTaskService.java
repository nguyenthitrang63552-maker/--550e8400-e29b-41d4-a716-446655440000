package com.ruoyi.Xidian.service;

import com.ruoyi.Xidian.domain.Task;

import java.util.List;

public interface SimulationTaskService {
    List<Task> selectList();

    void insert(Task task);

    List<Task> selectTaskList(Task task);

    Task selectById(Long id);

    void deleteTask(Long id);
}
