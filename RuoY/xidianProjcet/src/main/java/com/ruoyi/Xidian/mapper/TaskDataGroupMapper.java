package com.ruoyi.Xidian.mapper;

import com.ruoyi.Xidian.domain.TaskDataGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskDataGroupMapper  {
    void insertBatch(List<TaskDataGroup> dataGroups);

    TaskDataGroup selectById(Long id);

    void update(TaskDataGroup taskDataGroup);

    List<TaskDataGroup> selectByTaskId(Long taskId);

    void deleteByTaskId(Long id);
}
